from urllib import response
import requests
import apii.defs as defs
import json


session = requests.Session()

response = session.get(
    f"{defs.OANDA_URL}/accounts/{defs.ACCOUNT_ID}", headers=defs.SECURE_HEADER)

stuff = json.loads(response.content)
account = stuff['account']
balance = account['balance']
