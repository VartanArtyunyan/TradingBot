from ast import List
import datetime
import json
import time
import Connection
import ReaderWriter
import Calculation
#import Client
import StoreList
    


INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY"]}'
#JSON_FORMAT =  f"instrument" + {instrument}, "factor" + {factor}, "volatility" + {volatility}, longShort + {longShort}}"

""" class StoreList:
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

        for nextEvent in self.list_news:
            next_time =  Calculation.DateStringToObject(nextEvent["dateUtc"])
            if next_time > datetime.datetime.now():
                upcoming_event = nextEvent["name"] + " " + nextEvent["dateUtc"] + " " + nextEvent["currencyCode"]
                print(f"{upcoming_event}")
                cl.send(upcoming_event)
        
    @staticmethod
    def handleNextEvent(event):
        calculated_traits = Calculation.calculate(event)
        for instrument in list_pairs["instrumente"]:
            print(instrument)
            print(f"instrument:{instrument}{calculated_traits}")
            #sending = f"instrument:{instrument},factor:{factor},volatility:{volatility},longShort:{longShort}"
            #cl.send(sending)
    
    
    def getData(self):
            return self.list_news """





file_name = 'jsonCalender4.json'

income_json = Connection.start()
list_pairs = json.loads(INSTRUMENTS)
#list_pairs = json.loads(cl.read())



#Daten können bei Bedarf aus Dokument wieder ausgelesen werden
ReaderWriter.writeInDocument(income_json, file_name)
list_news = ReaderWriter.openJsonFile(file_name)



List_Storage = StoreList.StoreList(list_news, list_pairs)


List_Storage.filterSpeechAndReport()


List_Storage.upcoming_events()



#while (bool(a.data)):
#a.EventLoop()


#exit()

""" with open('afterLoop.json', 'w') as f:
    json.dump(a.getData(), f) """


    




