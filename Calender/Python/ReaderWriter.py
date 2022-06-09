import json

def writeInDocument(data, file_name):
    with open(file_name, "w") as f:
        f.write(data)
  
    
def openJsonFile(file_name):
    x = open(file_name)
    text = x.read()
    x.close()
    return json.loads(text)

""" importantValues = []

for i in y:
    werte = {}

    aa = y[i]["id"]
    a = y[i]["name"]
    b = y[i]["dateUtc"]
    c = y[i]["countryCode"]
    d = y[i]["currencyCode"]
    e = y[i]["volatility"]
    f = y[i]["isBetterThanExpected"]
    g = y[i]["previous"]
    h = y[i]["actual"]
    i = y[i]["consensus"]
    werte.update(aa)
    importantValues.append(werte)

print(importantValues) """
