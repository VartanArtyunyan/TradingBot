from concurrent.futures import thread
import socket, select, sys
import json
import time

HOST = socket.gethostname()
PORT = 12000
client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client_socket.connect(("localhost", 5000))
data = "Hello"

""" data = {
        "id": "4bda5381-c1e9-48e8-8d27-b764675b0a52",
        "eventId": "891ff107-c21f-4952-b749-6d8c17944f60",
        "dateUtc": "2022-06-03T07:55:00Z",
        "periodDateUtc": "2022-05-01T00:00:00Z",
        "periodType": "MONTH",
        "actual": null,
        "revised": null,
        "consensus": 54.6,
        "ratioDeviation": null,
        "previous": 54.6,
        "isBetterThanExpected": null,
        "name": "S&P Global/BME PMI Gesamtindex",
        "countryCode": "DE",
        "currencyCode": "EUR",
        "unit": null,
        "potency": "ZERO",
        "volatility": "MEDIUM",
        "isAllDay": false,
        "isTentative": false,
        "isPreliminary": false,
        "isReport": false,
        "isSpeech": false,
        "lastUpdated": 1653658493
    }
 """

""" while 1:
    data = client_socket.recv(512)
    if ( data == 'q' or data == 'Q'):
        client_socket.close()
        break;
    else:
        print "RECIEVED:" , data
        data = raw_input ( "SEND( TYPE q or Q to Quit):" )
        if (data != 'Q' and data != 'q'):
            client_socket.send(data)
        else:
            client_socket.send(data)
            client_socket.close()
            break; """


try:
    # Connect to server and send data
    client_socket.connect((HOST, PORT))
    #client_socket.sendall(bytes(json.dumps(data), 'UTF-8'))
    while True:
        client_socket.send("{data} + /n")
        time.sleep(100)

    # Receive data from the server and shut down
    #data = received = json.loads(client_socket.recv(1024).decode('UTF-8'))
finally:
    client_socket.close()







""" TCP_IP = '0.0.0.0'
TCP_PORT = 62

BUFFER_SIZE = 1024
MESSAGE = "Hello, Server. Are you ready?\n"

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, 5000))
s.send(MESSAGE)
socket_list = [sys.stdin, s]

while 1:
    read_sockets, write_sockets, error_sockets = select.select(socket_list, [], [])


    for sock in read_sockets:
        # incoming message from remote server
        if sock == s:
            data = sock.recv(4096)
            if not data:
                print('\nDisconnected from server')
                sys.exit()
            else:
                sys.stdout.write("\n")
                message = data.decode()
                sys.stdout.write(message)
                sys.stdout.write('<Me> ')
                sys.stdout.flush()

        else:
            msg = sys.stdin.readline()
            s.send(bytes(msg))
            sys.stdout.write('<Me> ')
            sys.stdout.flush() """


""" 

class Client:
   

    def __init__(self, sock=None):
        if sock is None:
            self.sock = socket.socket(
                            socket.AF_INET, socket.SOCK_STREAM)
        else:
            self.sock = sock

    def connect(self, host, port):
        self.sock.connect((host, port))

    def mysend(self, msg):
        totalsent = 0
        while totalsent < MSGLEN:
            sent = self.sock.send(msg[totalsent:])
            if sent == 0:
                raise RuntimeError("socket connection broken")
            totalsent = totalsent + sent

    def myreceive(self):
        chunks = []
        bytes_recd = 0
        while bytes_recd < MSGLEN:
            chunk = self.sock.recv(min(MSGLEN - bytes_recd, 2048))
            if chunk == b'':
                raise RuntimeError("socket connection broken")
            chunks.append(chunk)
            bytes_recd = bytes_recd + len(chunk)
        return b''.join(chunks) """