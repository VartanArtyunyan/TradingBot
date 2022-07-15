
import socket
import threading
import sys
import os


def recv_msg():
    while True:
        recv_msg = s.recv(1024)
        if not recv_msg:
            sys.exit(0)
        recv_msg = recv_msg.decode()
        print(recv_msg)
        f = open("../jsonData/basedata.txt", "a")
        f.write(recv_msg)


# Main
host = 'localhost'
port = 12001

s = socket.socket()
s.connect((host, port))

print("Connected to the server")

recv_msg()
