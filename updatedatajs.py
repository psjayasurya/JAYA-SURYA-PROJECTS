from flask import Flask, jsonify
import psycopg2

app = Flask(__name__)

def get_hotspots():
    conn = psycopg2.connect(
        dbname="virtual_toor",
        user="postgres",
        password="06092003",
        host="localhost"
    )
    cur = conn.cursor()
    cur.execute("SELECT scene_id, yaw, pitch, title, text FROM info_hotspots")
    rows = cur.fetchall()
    cur.close()
    conn.close()

    hotspots = {}
    for scene_id, yaw, pitch, title, text in rows:
        if scene_id not in hotspots:
            hotspots[scene_id] = []
        hotspots[scene_id].append({
            "yaw": yaw,
            "pitch": pitch,
            "title": title,
            "text": text
        })
    return hotspots

@app.route("/hotspots")
def hotspots():
    return jsonify(get_hotspots())

if __name__ == "__main__":
    app.run(debug=True)
