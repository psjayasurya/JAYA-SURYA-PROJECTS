import http.server
import socketserver

PORT = 8002
Handler = http.server.SimpleHTTPRequestHandler
with socketserver.TCPServer(("", PORT), Handler) as httpd:
    print(f"Serving file1.html at http://localhost:{PORT}/index.html")
    httpd.serve_forever()