from flask import Flask, jsonify

app = Flask(__name__)

@app.route("/")
def hello():
    return "Hello Flask, 김민수!"

@app.route("/api/ping")
def ping():
    return jsonify({"message": "pong", "by": "giga-chad"})

if __name__ == "__main__":
    app.run(debug=True)
