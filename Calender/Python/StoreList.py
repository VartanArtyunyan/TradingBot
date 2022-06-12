from dataclasses import dataclass
import datetime
import json
import Connection
import Calculation
import Settings
#import Client as cl

class StoreList:
    list_news = None
    list_pairs = None

    def __init__(self, list_news, list_pairs):
        self.list_news = list_news
        self.list_pairs = list_pairs

    def filterSpeechAndReport(self):
        for nextEvent in self.list_news:
            if nextEvent["isSpeech"] or nextEvent["isReport"]:
                self.list_news.remove(nextEvent)
                
    def EventLoop(self):
        for nextEvent in self.list_news:
            print(nextEvent["name"])
            update = Connection.checkEvent(nextEvent)
            print(update["actual"])
            if update["actual"] is not None:
                self.handleNextEvent(update)
                self.list_news.remove(nextEvent)
    
    def upcoming_events(self):
        #next_event = self.list_news[0]["name"] + " " + self.list_news[0]["dateUtc"] + " " + self.list_news[0]["currencyCode"]
        #least_upcoming_time = None
        for nextEvent in self.list_news:
            next_time =  Calculation.DateStringToObject(nextEvent["dateUtc"])
            if next_time > datetime.datetime.now() and nextEvent["isTentative"] is False:       #isTentative = True -> Release der Nachricht ist unklar und entspricht nicht der hinterlegten Zeit
                upcoming_event = '{"upcoming":  {"instrument": ' + None,"volatility": None,"time":None}}
                nextEvent["name"] + " " + nextEvent["dateUtc"] + " " + nextEvent["currencyCode"]
                print(f"{upcoming_event}")
                #cl.send(upcoming_event)
                nextEvent["isTentative"] = True     #Nachricht wurde gesendet. Verhindert das erneutige Senden und Auslösen eines Trades
        
    @staticmethod
    def handleNextEvent(self, event):
        calculated_traits = Calculation.calculate(event)
        for instrument in self.list_pairs["instrumente"]:
            print(instrument)
            print(f"instrument:{instrument}{calculated_traits}")
            #sending = f"instrument:{instrument},factor:{factor},volatility:{volatility},longShort:{longShort}"
            #cl.send(sending)

    def getData(self):
            return self.list_news




"""  def EventLoop(self):
        for nextEvent in self.data:
            update = Connection.checkEvent(nextEvent)
            if update["actual"] is not None:
                Calculation.calculate(nextEvent)
                list.remove(nextEvent) """
    
    
    

        
""" nextEventTime = DateStringToObject(nextEvent["dateUtc"])
        time.sleep(breakTimer(nextEventTime))
        timedelta = breakTimer(nextEventTime)
        print(timedelta.total_seconds()<0)
        print(f"{timedelta} :" + nextEvent["name"]) """
