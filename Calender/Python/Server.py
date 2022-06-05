
import socket


# Create a server socket

serverSocket = socket.socket()

print("Server socket created")


# Associate the server socket with the IP and Port

ip = "localhost"
port = 35491
serverSocket.bind((ip, port))
print("Server socket bound with with ip {} port {}".format(ip, port))

serverSocket.listen()       # Make the server listen for incoming connections

count = 0           # Server incoming connections "one by one"

while(True):

    (clientConnection, clientAddress) = serverSocket.accept()

    count = count + 1

    print("Accepted {} connections so far".format(count))

    # read from client connection

    while(True):
        data = clientConnection.recv(1024)
        print(data)

