import json
import psycopg2
import re

def get_db_connection():
    return psycopg2.connect(
        host="localhost",
        database="virtual_toor",
        user="postgres",
        password="06092003",
        port=5433  # Ensure this matches your PostgreSQL port
    )

# Load and clean JS file
with open("data.js", "r", encoding="utf-8") as f:
    js_content = f.read()

# Remove the var APP_DATA = and ending ;
json_str = re.sub(r"^var APP_DATA\s*=\s*", "", js_content.strip(), flags=re.DOTALL)
json_str = json_str.rstrip(";")

# Remove JavaScript trailing commas ( ,] or ,} )
json_str = re.sub(r",\s*([}\]])", r"\1", json_str)

# Now parse
app_data = json.loads(json_str)

# Insert hotspots
conn = get_db_connection()
cur = conn.cursor()

for scene in app_data["scenes"]:
    scene_id = scene["id"]
    for hotspot in scene.get("infoHotspots", []):
        cur.execute("""
            INSERT INTO info_hotspots (scene_id, yaw, pitch, title, text)
            VALUES (%s, %s, %s, %s, %s)
        """, (
            scene_id,
            hotspot["yaw"],
            hotspot["pitch"],
            hotspot["title"],
            hotspot["text"]
        ))

conn.commit()
cur.close()
conn.close()

print("✅ All infoHotspots inserted into database")
