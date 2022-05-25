
from matplotlib.pyplot import close
import requests
import defs
import json


session = requests.Session()

response = session.get(
    f"{defs.OANDA_URL}/accounts/{defs.ACCOUNT_ID}", headers=defs.SECURE_HEADER)


trades_response = session.get(
    f"{defs.OANDA_URL}/accounts/{defs.ACCOUNT_ID}/transactions/idrange?from=4&to=68", headers=defs.SECURE_HEADER)


account_data = json.loads(response.content)
stuff = json.loads(trades_response.content)
daily_dict = []

json_string = json.dumps(account_data)

for i in stuff['transactions']:
    if i['type'] == 'DAILY_FINANCING':
        daily_dict.append(i)

daily_dict = json.dumps(daily_dict)

with open("transaction.json", "w") as outfile:
    outfile.write(daily_dict)
    close

with open("account.json", "w") as outfile:
    outfile.write(json_string)
    close
