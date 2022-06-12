import socket
import time


class Client():
    HEADER = 64
    PORT = 12000
    FORMAT = 'utf-8'
    DISCONNECT_MESSAGE = "exit"
    SERVER = "localhost"
    ADDR = (SERVER, PORT)

    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect(ADDR)

    def send(self,msg):
        message = msg.encode(self.FORMAT)
        self.client.sendall(message)
    

    def read(self):
     #  input = None
    #  while input is None:
      #    print(input)
      input = self.client.recv(51200).decode(self.FORMAT)
      print(input)
      return input






""" connect = True
while connect == True:
    input_msg = ""
    if input_msg is not "":
        send(input_msg)
        input_msg = ""
    if(input_msg == DISCONNECT_MESSAGE):
        send(DISCONNECT_MESSAGE)
        connect = False

    data = client.recv(51200).decode(FORMAT)
    if data is not None:
        read(data)
    else: 
        input_msg == DISCONNECT_MESSAGE
        connect = False
    print(data)

print("[SYSTEM DISCONNECTED]")



time.sleep(2000) """








""" socketObject = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        
try:
    socketObject.connect(("localhost", 12000))
    receiveJsonData = socketObject.recv(51200)
    print(receiveJsonData)
    socketObject("okay")
except socket.error as e:
    print(str(e)) 

time.sleep(2000)

def close():
    socketObject.close()

def getJson():
    return receiveJsonData

def buy(jsonData):
    socketObject.sendall("{jsonData} + /n") 
 """




















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

"""

 



