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
    cl = None

    def __init__(self, list_news, list_pairs, cl):
        self.list_news = list_news
        self.list_pairs = list_pairs
        self.cl = cl

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
                print("update")
                self.handleNextEvent(update, pre_string)
                self.list_news.remove(nextEvent)
    
    def upcoming_events(self):
        pre_string = "upcoming"
        for nextEvent in self.list_news:
            next_time =  Calculation.DateStringToObject(nextEvent["dateUtc"])
            if Calculation.breakTimer(next_time) > datetime.timedelta(minutes = 10):
                break
            elif next_time > datetime.datetime.now() and nextEvent["isTentative"] is False:       #isTentative = True -> Release der Nachricht ist unklar und entspricht nicht der hinterlegten Zeit
                """ upcoming_event = nextEvent["name"] + " " + nextEvent["dateUtc"] + " " + nextEvent["currencyCode"]
                print(f"{upcoming_event}") """
                print("upcoming:" + str(nextEvent))
                self.handleNextEvent(nextEvent, pre_string)
                nextEvent["isTentative"] = True     #Nachricht wurde gesendet. Verhindert das erneutige Senden und Auslösen eines upcoming-Trades


    def handleNextEvent(self, event, pre_string):
        volatility = "volatility:" + str(event["volatility"])
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
            sending_str = f"{pre_string}:[{core}]"
            
            if currency not in instrument:
                break
            elif x.index(currency) == 1 and pre_string == "order":
                reverse_longShort = core
                reverse_longShort["longShort"] = not reverse_longShort["longShort"]
                sending_str = f"{pre_string}:[{reverse_longShort}]"

            else:
                    continue
            print("Ich sende jetzt das hier (hoffentlich): " + sending_str)
            self.cl.send(sending_str)

    def getData(self):
            return self.list_news




