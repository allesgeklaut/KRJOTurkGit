'''
Created on 29.03.2018

@author: krems
Test client or the TCP Server
'''

import socket

TCP_IP = "127.0.0.1"
TCP_PORT = 8080
BUFFER_SICE = 1024
MESSAGE = str('{0:04b}'.format(0)) + "SELECT * FROM learning_units            "
MESSAGE = MESSAGE.encode()

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((TCP_IP, TCP_PORT))
s.send(MESSAGE)

data = s.recv(BUFFER_SICE)

print('received data: ', data)

s.close()
