
import socket
import threading
import sys
<<<<<<< HEAD:Django/TradingBot/trading_B/client.py
import os
=======


# Get Message
>>>>>>> 80316a51da5074e78d880951e99383788f81f63f:Django/TradingBot/Socket_Data/client.py

def recv_msg():
    while True:
        recv_msg = s.recv(1024)
        if not recv_msg:
            sys.exit(0)
        recv_msg = recv_msg.decode()
        print(recv_msg)
        f = open("../jsonData/basedata.json", "w")
        f.write(recv_msg)



# Main

host = 'localhost'
port = 12001

s = socket.socket()
s.connect((host, port))

print("Connected to the server")

recv_msg()
