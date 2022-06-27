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




file_name = 'jsonCalender5.json'



income_json = Connection.start()
#list_pairs = json.loads(INSTRUMENTS)
cl = Client()

list_pairs = json.loads(cl.read())





#Daten können bei Bedarf aus Dokument wieder ausgelesen werden
ReaderWriter.writeInDocument(income_json, file_name)
list_news = ReaderWriter.openJsonFile(file_name)



List_Storage = StoreList(list_news, list_pairs)

List_Storage.filterSpeechAndReport()
List_Storage.filterOldEvents()


while (bool(List_Storage.list_news)):
    List_Storage.EventLoop()
    List_Storage.upcoming_events()


print("Finished all events")
#exit()



    




