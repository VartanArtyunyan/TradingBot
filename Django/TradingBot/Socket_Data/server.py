import socket
import threading
import sys


# Get Message

def recv_msg():
    while True:
        recv_msg = conn.recv(1024)
        if not recv_msg:
            sys.exit(0)
        recv_msg = recv_msg.decode()
        print(recv_msg)


# Main

host = socket.gethostname()
port = 8091

s = socket.socket()
s.bind((host, port))
s.listen(1)

print("Waiting for connections")
conn, addr = s.accept()

print("Client has connected")

# thread has to start before other loop
t = threading.Thread(target=recv_msg)
t.start()

# test
