from ast import List
import datetime
import json
import time
import Connection
import ReaderWriter
import Calculation
from Client import Client
from StoreList import StoreList
    



#INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY"]}'




file_name = 'jsonCalenderToday.json'



income_json = Connection.start()
#list_pairs = json.loads(INSTRUMENTS)
cl = Client()

list_pairs = json.loads(cl.read())





#Daten können bei Bedarf aus Dokument wieder ausgelesen werden
ReaderWriter.writeInDocument(income_json, file_name)
list_news = ReaderWriter.openJsonFile(file_name)



List_Storage = StoreList(list_news, list_pairs, cl)

List_Storage.filterSpeechAndReport()
List_Storage.filterSpeechAndReport()
List_Storage.filterSpeechAndReport()
print(len(List_Storage.list_news))
List_Storage.filterOldEvents()
List_Storage.filterOldEvents()
List_Storage.filterOldEvents()
List_Storage.filterOldEvents()
print(len(List_Storage.list_news))

while (bool(List_Storage.list_news)):
    List_Storage.EventLoop()
    List_Storage.upcoming_events()


print("Finished all events")
#exit()



    




