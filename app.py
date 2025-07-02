from flask import Flask, request, jsonify, render_template, send_file
import ee
import logging
import time
from functools import wraps
import requests
import io
from flask_cors import CORS

app = Flask(__name__)
CORS(app)


# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize Earth Engine
try:
    ee.Initialize(project='sunlit-velocity-462005-v1')
except Exception as e:
    logger.warning("Earth Engine not initialized. Authenticating...")
    ee.Authenticate()
    ee.Initialize(project='sunlit-velocity-462005-v1')


def ee_retry(max_retries=5, initial_delay=1.0, backoff_factor=2.0):
    """Decorator to handle Earth Engine API retries with exponential backoff"""
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            retries = 0
            delay = initial_delay
            last_exception = None

            while retries < max_retries:
                try:
                    return func(*args, **kwargs)
                except ee.EEException as e:
                    if "429" in str(e):
                        retries += 1
                        logger.warning(f"Rate limited. Retry {retries}/{max_retries} in {delay} seconds...")
                        time.sleep(delay)
                        delay *= backoff_factor
                        last_exception = e
                    else:
                        raise
            raise last_exception if last_exception else Exception("Max retries exceeded")
        return wrapper
    return decorator


#@app.route('/')
#def index():
#    return render_template('index.html')


@app.route('/ndvi', methods=['POST'])
@ee_retry(max_retries=5, initial_delay=1.0, backoff_factor=2.0)
def get_ndvi():
    try:
        data = request.get_json()
        if not data or 'geometry' not in data:
            return jsonify({'error': 'Missing geometry in request'}), 400

        coords = data.get('geometry')
        if not isinstance(coords, list) or len(coords) < 3:
            return jsonify({'error': 'Invalid geometry format'}), 400

        region = ee.Geometry.Polygon([coords]).centroid().buffer(500).bounds()

        collection = ee.ImageCollection('COPERNICUS/S2_SR') \
            .filterBounds(region) \
            .filterDate('2019-01-01', '2024-12-31') \
            .filter(ee.Filter.lt('CLOUDY_PIXEL_PERCENTAGE', 40)) \
            .map(lambda img: img.addBands(
                img.normalizedDifference(['B8', 'B4']).rename('NDVI')
            ))

        start = ee.Date('2019-01-01')
        months = ee.List.sequence(0, 71)

        monthly = ee.ImageCollection.fromImages(
            months.map(lambda m: ee.Algorithms.If(
                collection
                    .filterDate(start.advance(m, 'month'), start.advance(ee.Number(m).add(1), 'month'))
                    .size().gt(0),
                ee.Image(
                    collection
                        .filterDate(start.advance(m, 'month'), start.advance(ee.Number(m).add(1), 'month'))
                        .select('NDVI')
                        .mean()
                        .set('system:time_start', start.advance(m, 'month').millis())
                ),
                ee.Image().set('system:time_start', start.advance(m, 'month').millis())
            ))
        )

        chart = monthly.map(lambda img: ee.Feature(None, {
            'date': ee.Date(img.get('system:time_start')).format('YYYY-MM'),
            'ndvi': ee.Algorithms.If(
                img.bandNames().contains('NDVI'),
                img.reduceRegion(
                    reducer=ee.Reducer.mean(),
                    geometry=region,
                    scale=20,
                    maxPixels=1e9
                ).get('NDVI'),
                None
            )
        }))

        features = chart.getInfo()['features']
        result = []
        for f in features:
            props = f['properties']
            if props.get('ndvi') is not None:
                result.append({
                    'month': props['date'],
                    'ndvi': props['ndvi']
                })

        return jsonify({
            'features': result,
            'stats': {
                'total_months': len(result),
                'missing_months': 72 - len(result)
            }
        })

    except ee.EEException as e:
        logger.error(f"Earth Engine error: {str(e)}")
        return jsonify({'error': str(e)}), 500
    except Exception as e:
        logger.error(f"Unexpected error: {str(e)}")
        return jsonify({'error': 'An unexpected error occurred'}), 500
    
@app.route('/landcover', methods=['POST'])
@ee_retry()
def get_land_cover():
    try:
        data = request.get_json()
        coords = data.get('geometry')
        year = int(data.get('year', 2023))

        if not coords or not isinstance(coords, list):
            return jsonify({'error': 'Invalid geometry'}), 400

        region = ee.Geometry.Polygon([coords])
        modis = ee.ImageCollection('MODIS/061/MCD12Q1') \
            .filter(ee.Filter.calendarRange(year, year, 'year')) \
            .first().select('LC_Type1').clip(region)

        hist = modis.reduceRegion(
            reducer=ee.Reducer.frequencyHistogram(),
            geometry=region,
            scale=500,
            maxPixels=1e13
        ).get('LC_Type1')

        result = {}
        hist_dict = hist.getInfo() if hist else {}

        palette = [
            '#1c0dff', '#05450a', '#086a10', '#54a708', '#78d203', '#009900',
            '#c6b044', '#dcd159', '#dade48', '#fbff13', '#b6ff05', '#27ff87',
            '#c24f44', '#a5a5a5', '#ff6d4c', '#69fff8', '#f9ffa4', '#ffffff'
        ]

        class_names = [
            'Water Bodies', 'Evergreen Needleleaf Forests', 'Evergreen Broadleaf Forests',
            'Deciduous Needleleaf Forests', 'Deciduous Broadleaf Forests', 'Mixed Forests',
            'Closed Shrublands', 'Open Shrublands', 'Woody Savannas', 'Savannas',
            'Grasslands', 'Permanent Wetlands', 'Croplands', 'Urban and Built-up Lands',
            'Cropland/Natural Vegetation Mosaics', 'Permanent Snow and Ice', 'Barren',
            'Unclassified'
        ]

        PIXEL_AREA_M2 = 500 * 500
        for k, count in hist_dict.items():
            idx = int(k)
            label = class_names[idx] if idx < len(class_names) else f'Class {idx}'
            result[label] = {
                'count': count,
                'area_m2': count * PIXEL_AREA_M2,
                'color': palette[idx] if idx < len(palette) else "#000000"
            }

        return jsonify({'year': year, 'landcover': result})

    except Exception as e:
        
        return jsonify({'error': str(e)}), 500

@app.route('/modis_tiles/<year>/<country>/<int:z>/<int:x>/<int:y>.png')
@ee_retry()
def get_modis_tile_by_country(year, country, z, x, y):
    try:
        year = int(year)
        countries = ee.FeatureCollection('USDOS/LSIB_SIMPLE/2017')
        country_geom = countries.filter(ee.Filter.eq('country_na', country)).geometry()

        modis = ee.ImageCollection('MODIS/061/MCD12Q1') \
            .filter(ee.Filter.calendarRange(year, year, 'year')) \
            .first().select('LC_Type1') \
            .clip(country_geom)

        vis_params = {
            'min': 0,
            'max': 17,
            'palette': [
                '#1c0dff', '#05450a', '#086a10', '#54a708', '#78d203', '#009900',
                '#c6b044', '#dcd159', '#dade48', '#fbff13', '#b6ff05', '#27ff87',
                '#c24f44', '#a5a5a5', '#ff6d4c', '#69fff8', '#f9ffa4', '#ffffff'
            ]
        }

        map_id_dict = modis.getMapId(vis_params)
        tile_url = map_id_dict['tile_fetcher'].url_format.replace('{z}', str(z)).replace('{x}', str(x)).replace('{y}', str(y))

        response = requests.get(tile_url)
        return send_file(io.BytesIO(response.content), mimetype='image/png')

    except Exception as e:
        logging.error(f"MODIS tile error: {e}")
        return jsonify({'error': str(e)}), 500
    



if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)