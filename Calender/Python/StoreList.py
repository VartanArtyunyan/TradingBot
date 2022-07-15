from dataclasses import dataclass
import datetime
import json
from locale import currency
import Connection
import Calculation
from Client import Client

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

    def filterOldEvents(self):
        for nextEvent in self.list_news:
            next_time =  Calculation.DateStringToObject(nextEvent["dateUtc"])
            time_compared = next_time < datetime.datetime.utcnow()              #true wenn jetztige Zeit nach der Eventzeit
            
            if time_compared and (nextEvent["isSpeech"] or nextEvent["isReport"]):
                self.list_news.remove(nextEvent)
                continue
            elif time_compared and nextEvent["actual"] is not None:
                self.list_news.remove(nextEvent)
                
            
                
    def EventLoop(self):
        pre_string = "order"
        for nextEvent in self.list_news:
            #print(nextEvent["name"])
            update = Connection.checkEvent(nextEvent)
            #print(update["actual"])
            if update["actual"] is not None:
                print(datetime.datetime.now())
                print("new actual:" + str (nextEvent))
                self.handleNextEvent(update, pre_string)
                self.list_news.remove(nextEvent)
    
    def upcoming_events(self):
        pre_string = "upcoming"
        for nextEvent in self.list_news:
            next_time =  Calculation.DateStringToObject(nextEvent["dateUtc"])
           
            if Calculation.breakTimer(next_time) > datetime.timedelta(minutes = 10):
                continue
            elif next_time > datetime.datetime.utcnow() and nextEvent["isTentative"] is False:       #isTentative = True -> Release der Nachricht ist unklar und entspricht nicht der hinterlegten Zeit
                print(datetime.datetime.now())
                print("Upcoming: " + str (nextEvent))
                self.handleNextEvent(nextEvent, pre_string)
                
                nextEvent["isTentative"] = True     #Nachricht wurde gesendet. Verhindert das erneutige Senden und Auslösen eines upcoming-Trades
                
            


    def handleNextEvent(self, event, pre_string):
        volatility = event["volatility"]
        core = None
        currency = event["currencyCode"]
        eventName = event["name"]
        country_code = event["countryCode"]
        
        if pre_string == "order":
            factor = Calculation.calculate(event)
            longShort = Calculation.longShort(event)
            core = {"name":eventName,"countryCode":country_code,"instrument":None,"volatility":volatility,"factor":factor, "longShort":longShort}
        else:
            time = (Calculation.DateStringToObject(event["dateUtc"]) + datetime.timedelta(hours=1)).strftime("%Y-%m-%dT%H:%M:%S.%fZ")
            core = {"name":eventName,"countryCode":country_code,"instrument":None,"volatility":volatility,"time":time}
        
        
        for instrument in self.list_pairs["instrumente"]:
            
            x = instrument.split("/")
            sending_str = core

            if currency not in instrument:
                continue
            elif x.index(currency) == 1 and pre_string == "order":
                sending_str["longShort"] = not sending_str["longShort"]
                

            sending_str["instrument"] = instrument
            sending_str = str(sending_str)
            sending_str = "{" + f"\'{pre_string}\':{sending_str}" + "}"
            print("send: " + str(sending_str))
            self.client.send(sending_str)


    def getData(self):
            return self.list_news




