from dataclasses import dataclass
import datetime
import json
from locale import currency
import Connection
import Calculation
#from Client import Client

class StoreList:
    list_news = None
    list_pairs = None
    client = None
    
    def __init__(self, list_news, list_pairs, client):
        self.list_news = list_news
        self.list_pairs = list_pairs
        self.client = client

    def filterSpeechAndReport(self):
        for nextEvent in self.list_news:
            if nextEvent["isSpeech"] or nextEvent["isReport"]:
                self.list_news.remove(nextEvent)
                
    def EventLoop(self):
        pre_string = "order"
        for nextEvent in self.list_news:
            #print(nextEvent["name"])
            update = Connection.checkEvent(nextEvent)
            #print(update["actual"])
            if update["actual"] is not None:
                print(nextEvent)
                self.handleNextEvent(update, pre_string)
                self.list_news.remove(nextEvent)
    
    def upcoming_events(self):
        pre_string = "upcoming"
        for nextEvent in self.list_news:
            next_time =  Calculation.DateStringToObject(nextEvent["dateUtc"])
           
            if Calculation.breakTimer(next_time) > datetime.timedelta(minutes = 10):
                continue
            elif next_time > datetime.datetime.now() and nextEvent["isTentative"] is False:       #isTentative = True -> Release der Nachricht ist unklar und entspricht nicht der hinterlegten Zeit
                self.handleNextEvent(nextEvent, pre_string)
                nextEvent["isTentative"] = True     #Nachricht wurde gesendet. Verhindert das erneutige Senden und Auslösen eines upcoming-Trades
                
            


    def handleNextEvent(self, event, pre_string):
        volatility = event["volatility"]
        core = None
        currency = event["currencyCode"]
        
        if pre_string == "order":
            factor = Calculation.calculate(event)
            longShort = Calculation.longShort(event)
            core = {"Instrument": None,"volatility": volatility,"factor": factor, "longShort": longShort}
        else:
            time = event["dateUtc"]
            core = {"Instrument": None,"volatility": volatility,"time": time}
        
        
        for instrument in self.list_pairs["instrumente"]:
            
            x = instrument.split("/")
            sending_str = core

            if currency not in instrument:
                continue
            elif x.index(currency) == 1 and pre_string == "order":
                sending_str["longShort"] = not sending_str["longShort"]
                

            sending_str["Instrument"] = instrument
            sending_str = str(sending_str).replace("{", "[").replace("}", "]")
            sending_str = "{" + f"'{pre_string}':{sending_str}" + "}"
            print("send: " + str(sending_str))
            self.client.send(sending_str)


    def getData(self):
            return self.list_news




