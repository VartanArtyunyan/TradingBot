import socket
import sys
import socket
import threading
from datetime import datetime
import time


"""
# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

# Bind the socket to the port
server_address = ('localhost', 12000)
print ('starting up on %s port %s' % server_address)
sock.bind(server_address)

# Listen for incoming connections
sock.listen(1)

while True:
    # Wait for a connection
    print ('waiting for a connection')
    connection, client_address = sock.accept()

    try:
        print ('connection from', client_address)

        # Receive the data in small chunks and retransmit it
        while True:
            data = connection.recv(16)
            print ('received "%s"' % data)
            if data:
                print ('sending data back to the client')
                connection.sendall(data)
            else:
                print ('no more data from', client_address)
                break

    finally:
        # Clean up the connection
        connection.close() """

 # Create a server socket



HEADER = 64
PORT = 12000
SERVER = "localhost" #socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = 'utf-8'
DISCONNECT_MESSAGE = "exit"
CONFIRM_MESSAGE = ("Message Received.").encode(FORMAT)



class MessageHandler(object):

    def read_message(self, conn, addr, msg):
        print(f"Address {addr}: Message: {msg}")
        return CONFIRM_MESSAGE

def handle_client(conn, addr):
    print(f"\n[NEW CONNECTION] {addr} connected.")
    mh = MessageHandler()
    connected = True
    while connected:
        msg_length = conn.recv(HEADER).decode(FORMAT)
        if msg_length:
            msg_length = int(msg_length)
            print(msg_length)
            msg = conn.recv(msg_length).decode(FORMAT)
            print(msg)
            if msg == DISCONNECT_MESSAGE:
                connected = False
            return_msg = mh.read_message(conn, addr, msg)
            if(return_msg != None):
                conn.send(return_msg)
        conn.send(return_msg)
        
    conn.close()


if __name__ == "__main__":
    server = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    #server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server.bind(ADDR)
    print("[STARTING] server is starting...")
    server.listen()  # We can use an argument that is an int and limitates the number of connections
    print(f"[LISTNING] Server is listening on {SERVER}")

    while True:
        conn, addr = server.accept()
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()