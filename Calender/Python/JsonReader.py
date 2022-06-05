import json
from tkinter import E

def read():
    x = open('jsonCalender2.json')
    text = x.read()
    x.close()
    y = json.loads(text)
    return y


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
