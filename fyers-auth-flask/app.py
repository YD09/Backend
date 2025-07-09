from flask import Flask, redirect, request
import urllib.parse
import requests

app = Flask(__name__)

CLIENT_ID = "6OATFNM621-100"
SECRET_KEY = "OIUA3PVTIU"
REDIRECT_URI = "http://127.0.0.1:5000/callback"
BASE_URL = "https://api.fyers.in"

@app.route("/login")
def login():
    auth_url = (
        "https://api-t1.fyers.in/api/v3/generate-authcode?"
        f"client_id={CLIENT_ID}&redirect_uri="
        f"{urllib.parse.quote(REDIRECT_URI, safe='')}"
        "&response_type=code&state=xyz"
    )

    return redirect(auth_url)

@app.route("/callback")
def callback():
    auth_code = request.args.get("code")
    if not auth_code:
        return " No auth code received"

    # Show code and instructions
    return f" Auth code received: <b>{auth_code}</b><br><br>Send it to your Spring Boot app to get access token."
if __name__ == "__main__":
    app.run(debug=True)
