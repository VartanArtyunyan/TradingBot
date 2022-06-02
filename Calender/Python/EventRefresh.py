from encodings import utf_8
import http.client
import gzip
import json
from pickle import FALSE, TRUE
from xmlrpc.client import gzip_decode
from datetime import date, timedelta
import datetime

def checkEvent(event):
    conn = http.client.HTTPSConnection("calendar-api.fxstreet.com")
    payload = ""
    headers = {
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
    id = event["id"]
    #id = "f879e41a-12d2-4d35-8213-6fb9ebb207a0"
    #eventId = "39da71dd-6a75-4669-adbf-36819ba1089a"
    URL = f"https://calendar-api.fxstreet.com/de/api/v1/eventDates/{id}"
    

    #aktuell = FALSE

    #while not aktuell:
    conn.request("GET", URL, payload, headers)
    res = conn.getresponse()    #responseCode / HTTP status < 400 anlegen + Exception schmeißen
    data = res.read()
    x = gzip_decode(data)
    s = x.decode('utf-8')
    jsonEvent = json.loads(s)
    return jsonEvent
    
    """ if jsonEvent["actual"] != "null":
        return jsonEvent
    else: return FALSE """

    
    #aktuell = TRUE

        #f = open("EventRefreshed.json", "w")
        #f.write(s)
        #f.close
