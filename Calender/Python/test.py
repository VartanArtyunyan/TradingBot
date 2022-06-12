import Client
import socket
import time










cl = Client.Client()
print(cl.read())
cl.send("hello\n")

time.sleep(30)