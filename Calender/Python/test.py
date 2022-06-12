""" import Client
import socket
import time



cl = Client.Client()
print(cl.read())
time.sleep(3)
cl.send("hello")

time.sleep(3) """


import json
import Settings

x = "order":["instrument":None,"factor":None,"volatility":None,"longShort":None]}

# convert into JSON:
y = json.dumps(x)

# the result is a JSON string:
print(y)
