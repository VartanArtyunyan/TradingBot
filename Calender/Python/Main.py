from ast import List
import datetime
import json
import time
import Connection
import ReaderWriter
import Calculation
from Client import Client
from StoreList import StoreList
    


<<<<<<< HEAD

INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY"]}'
=======
#INSTRUMENTS = '{"instrumente":["EUR/USD","GBP/CHF","EUR/JPY", "CHF/GBP"]}'
>>>>>>> e4407bb93312e98f30e5ab1623813716525f55b3
#JSON_FORMAT =  f"instrument" + {instrument}, "factor" + {factor}, "volatility" + {volatility}, longShort + {longShort}}"



file_name = 'jsonCalender1.json'

income_json = Connection.start()


#list_pairs = json.loads(INSTRUMENTS)
cl = Client()
<<<<<<< HEAD
cl.read()
list_pairs = json.loads(INSTRUMENTS)

print(list_pairs)
=======
list_pairs = json.loads(cl.read())

>>>>>>> e4407bb93312e98f30e5ab1623813716525f55b3



#Daten können bei Bedarf aus Dokument wieder ausgelesen werden
#ReaderWriter.writeInDocument(income_json, file_name)
list_news = ReaderWriter.openJsonFile(file_name)


<<<<<<< HEAD

=======
>>>>>>> e4407bb93312e98f30e5ab1623813716525f55b3
List_Storage = StoreList(list_news, list_pairs, cl)


List_Storage.filterSpeechAndReport()

while (bool(List_Storage.list_news)):
    List_Storage.EventLoop()
    List_Storage.upcoming_events()
    #break

<<<<<<< HEAD
=======
print("fertig")
>>>>>>> e4407bb93312e98f30e5ab1623813716525f55b3
#exit()



    




