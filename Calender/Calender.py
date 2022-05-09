from encodings import utf_8
import http.client
import gzip
from xmlrpc.client import gzip_decode

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
URL = "/en/api/v1/eventDates/2022-05-03T10:26:23Z/2022-05-05T12:26:23Z?volatilities=NONE&volatilities=LOW&volatilities=MEDIUM&volatilities=HIGH&volatilities=NONE&volatilities=LOW&volatilities=MEDIUM&volatilities=HIGH&countries=US&countries=UK&countries=EMU&countries=DE&countries=CN&countries=JP&countries=CA&countries=AU&countries=NZ&countries=CH&countries=FR&countries=IT&countries=ES&countries=UA&countries=US&countries=UK&countries=EMU&countries=DE&countries=CN&countries=JP&countries=CA&countries=AU&countries=NZ&countries=CH&countries=FR&countries=IT&countries=ES&categories=8896AA26-A50C-4F8B-AA11-8B3FCCDA1DFD&categories=FA6570F6-E494-4563-A363-00D0F2ABEC37&categories=C94405B5-5F85-4397-AB11-002A481C4B92&categories=E229C890-80FC-40F3-B6F4-B658F3A02635&categories=24127F3B-EDCE-4DC4-AFDF-0B3BD8A964BE&categories=DD332FD3-6996-41BE-8C41-33F277074FA7&categories=7DFAEF86-C3FE-4E76-9421-8958CC2F9A0D&categories=1E06A304-FAC6-440C-9CED-9225A6277A55&categories=33303F5E-1E3C-4016-AB2D-AC87E98F57CA&categories=9C4A731A-D993-4D55-89F3-DC707CC1D596&categories=91DA97BD-D94A-4CE8-A02B-B96EE2944E4C&categories=E9E957EC-2927-4A77-AE0C-F5E4B5807C16&categories=8896AA26-A50C-4F8B-AA11-8B3FCCDA1DFD&categories=FA6570F6-E494-4563-A363-00D0F2ABEC37&categories=C94405B5-5F85-4397-AB11-002A481C4B92&categories=E229C890-80FC-40F3-B6F4-B658F3A02635&categories=24127F3B-EDCE-4DC4-AFDF-0B3BD8A964BE&categories=DD332FD3-6996-41BE-8C41-33F277074FA7&categories=7DFAEF86-C3FE-4E76-9421-8958CC2F9A0D&categories=1E06A304-FAC6-440C-9CED-9225A6277A55&categories=33303F5E-1E3C-4016-AB2D-AC87E98F57CA&categories=9C4A731A-D993-4D55-89F3-DC707CC1D596&categories=91DA97BD-D94A-4CE8-A02B-B96EE2944E4C&categories=E9E957EC-2927-4A77-AE0C-F5E4B5807C16"
conn.request("GET", URL, payload, headers)
#conn.request("GET", "/en/api/v1/eventDates/2022-05-04", payload, headers)

res = conn.getresponse()
data = res.read()
x = gzip_decode(data)
s = x.decode('utf-8')
f = open("jsonCalender2.txt", "w")
f.write(s)
f.close

#print(x)