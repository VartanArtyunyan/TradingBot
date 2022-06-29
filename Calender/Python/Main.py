import datetime
import json
import time
import Connection
import ReaderWriter
from Client import Client
from StoreList import StoreList
from threading import Event
    

def pause_timer():
    next_day = (datetime.datetime.utcnow() + datetime.timedelta(days=1)).replace(hour=0, minute=0, second=0, microsecond=0)
    seconds = (next_day - datetime.datetime.utcnow()).total_seconds()
    return seconds



#INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY"]}'
#list_pairs = json.loads(INSTRUMENTS)


cl = Client()
list_pairs = json.loads(cl.read())


while(True):
    print("I'm awake, today is " + str(datetime.date.today()))
    file_name = "Events" + str(datetime.date.today()) + ".json"
    income_json = Connection.start()

    #Daten können bei Bedarf aus Dokument wieder ausgelesen werden
    ReaderWriter.writeInDocument(income_json, file_name)
    list_news = ReaderWriter.openJsonFile(file_name)



    List_Storage = StoreList(list_news, list_pairs, cl)

    #List_Storage.filterSpeechAndReport()   kann jetzt weggelassen werden, da filter in Schleife aktiv und auch speach/report jetzt upcoming kommen

    while (bool(List_Storage.list_news)):
        List_Storage.filterOldEvents()

        time.sleep(5)
        #Event.wait()

        List_Storage.EventLoop()
        time.sleep(5)
        List_Storage.upcoming_events()


    print("Finished all events for today")
    print("Tt's time to sleep")
    time.sleep(pause_timer())



    




