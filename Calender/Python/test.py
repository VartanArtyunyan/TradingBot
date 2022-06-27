""" import json


event = {
    "id": "ff1f3d8f-9bd5-4447-ac57-c8a8efd88935",
    "eventId": "015035d6-7dfe-4bb0-a138-5362dbcdf309",
    "dateUtc": "2022-06-13T14:00:00Z",
    "periodDateUtc": "2022-04-01T00:00:00Z",
    "periodType": "MONTH",
    "actual": -1.0,
    "revised": None,
    "consensus": 0.2,
    "ratioDeviation": -2.00669,
    "previous": -0.2,
    "isBetterThanExpected": False,
    "name": "NS verarbeitendes Gewerbe (Monat)",
    "countryCode": "UK",
    "currencyCode": "GBP",
    "unit": "%",
    "potency": "ZERO",
    "volatility": "MEDIUM",
    "isAllDay": False,
    "isTentative": False,
     "isPreliminary": False,
    "isReport": False,
    "isSpeech": False,
    "lastUpdated": 1655100424
}

#ev = json.loads(event)

INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY", "CHF/GBP"]}'
list_pairs = json.loads(INSTRUMENTS)

pre_string = 'order'


def handleNextEvent(list_pairs, event, pre_string,):
        volatility = event["volatility"]
        core = None
        currency = event["currencyCode"]
        
        if pre_string == "order":
            factor = 1,7    #Calculation.calculate(event)
            longShort = True    #Calculation.longShort(event)
            core = {"Instrument": None,"volatility": volatility,"factor": factor, "longShort": longShort}
        else:
            time = event["dateUtc"]
            core = {"Instrument": None,"volatility": volatility,"time": time}
        

        for instrument in list_pairs["instrumente"]:
            
            x = instrument.split("/")
            sending_str = core

            if currency not in instrument:
                print("currency break:" + str (currency))
                continue
            elif x.index(currency) == 1 and pre_string == "order":
                sending_str["longShort"] = not sending_str["longShort"]
                
            

            sending_str["Instrument"] = instrument
            sending_str = str(sending_str).replace("{", "[").replace("}", "]")
            sending_str = "{" + f"'{pre_string}':{sending_str}" + "}"
        
            #self.client.send(sending_str)


x = handleNextEvent(list_pairs, event, pre_string)
print(x) """
import datetime

now = datetime.datetime.utcnow()

out = (now + datetime.timedelta(hours=1)).strftime("%Y-%m-%dT%H:%M:%S.%fZ")

print(now)
print(out)
