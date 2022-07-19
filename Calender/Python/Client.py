import socket
from threading import *
import time

class Client:
    HEADER = 64
    HOST = "localhost"
    PORT = 12000
    FORMAT = 'utf-8'


    ADDR = (HOST, PORT)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect(ADDR)

    def send(self, msg):
        msg = f"{msg} \n"
        message = msg.encode(self.FORMAT)
        self.client.send(message)
        

    def read(self):
        input = self.client.recv(51200).decode(self.FORMAT)
        return input





















""" import queue
import socket
import struct
import threading
import time


class ThreadedClient(threading.Thread):
    
    def __init__(self, host, port):
        threading.Thread.__init__(self)
        #set up queues
        self.send_q = queue.Queue(maxsize = 10)
        #declare instance variables       
        self.host = host
        self.port = port
        #connect to socket
        self.s = socket.socket()
        self.s.connect((self.host, self.port))
        self.s.settimeout(.1)
        self.start_listen()


    #LISTEN
    def listen(self):
        while True: #loop forever
            try:
                print ('checking for message...')
                # stops listening if there's a message to send
                if self.send_q.empty() == False:
                    self.send_message()
                else:
                    print ('no message')
                print ('listening...')
                message = self.s.recv(4096)
                print ('RECEIVED: ' + message)
            except socket.timeout:
                pass

    def run(self):
        print("started")



    def start_listen(self):
        t = threading.Thread(target = self.listen)
        t.start()
        #t.join()
        print ('started listen')


    #ADD MESSAGE
    def add_message(self, msg):
        #put message into the send queue
        self.send_q.put(msg)
        print ('ADDED MSG: ' + msg)
        #self.send_message()

    #SEND MESSAGE
    def send_message(self):
        #send message
        msg = self.get_send_q()
        if msg == "empty!":
            print ("nothing to send")
        else:
            self.s.sendall(msg)
            print ('SENDING: ' + msg)
            #restart the listening
            #self.start_listen()


    #SAFE QUEUE READING
    #if nothing in q, prints "empty" instead of stalling program
    def get_send_q(self):       
        if self.send_q.empty():
            print ("empty!")
            return "empty!"
        else:
            msg = self.send_q.get()
            return msg

#if __name__ == '__main__':
port = 8001
address = 'localhost'
s = ThreadedClient(address, port)
s.start()
print('Server started, port: ', port)

s.add_message('hello world')
s.start_listen()
s.add_message('hello world')


time.sleep(2000)
 """



