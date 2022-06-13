from asyncore import write
from encodings import utf_8
import http.client
import gzip
import json
import time
#from pickle import FALSE
from xmlrpc.client import gzip_decode
from datetime import date, timedelta


CONN = http.client.HTTPSConnection("calendar-api.fxstreet.com")
PAYLOAD = ""
HEADERS = {
    'User-Agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0",
    'Accept': "application/json",
    'Accept-Language': "en-US,en;q=0.5",
    'Accept-Encoding': "gzip, deflate, br",
    'Referer': "https://www.fxstreet.de.com/",
    'Origin': "https://www.fxstreet.de.com",
    'DNT': "1",
    'Connection': "keep-alive",
    'Sec-Fetch-Dest': "empty",
    'Sec-Fetch-Mode': "cors",
    'Sec-Fetch-Site': "cross-site",
    'Pragma': "no-cache",
    'Cache-Control': "no-cache"
    }






def handleConnection(URL):

    CONN.request("GET", URL, PAYLOAD, HEADERS)
    res = CONN.getresponse()
    while True:
        if res.status == 200:      
            data = res.read()
            x = gzip_decode(data)
            s = x.decode('utf-8')
            return(s)
        else:
            print("no connection or no events")
            time.sleep(5)


def start():
    

    """ URL = "/de/api/v1/eventDates/2022-05-09T10:02:42Z/2022-05-11T12:02:42Z"
    gerade = datetime.datetime.now()
    bis = (gerade - timedelta(hours=2)).strftime("%H:%M:%S") #Zeitverschiebung nach UTC
    von = (gerade - timedelta(hours=5)).strftime("%H:%M:%S")
    dat = "2022-05-09" """

    dat = date.today()
    #dat = "2022-06-13"
    von = "00:00:00"
    bis = "23:59:59"

    URL = f"https://calendar-api.fxstreet.com/de/api/v1/eventDates/{dat}T{von}Z/{dat}T{bis}Z?=&volatilities=HIGH&volatilities=MEDIUM"

    return handleConnection(URL)

    

    


def checkEvent(event):
    
    id = event["id"]
    #id = "f879e41a-12d2-4d35-8213-6fb9ebb207a0"
    #eventId = "39da71dd-6a75-4669-adbf-36819ba1089a"
    URL = f"https://calendar-api.fxstreet.com/de/api/v1/eventDates/{id}"
    

    #aktuell = FALSE
    #while not aktuell:
   
    s = handleConnection(URL)
    jsonEvent = json.loads(s)
    return jsonEvent



start()



