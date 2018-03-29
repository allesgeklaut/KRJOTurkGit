'''
Created on 29.03.2018

@author: krems
'''
import socket


class TCPServer(object):

    def __init__(self, port):
        self.TCP_IP = '127.0.0.1'
        self.TCP_PORT = port
        self.BUFFER_SICE = 1024

    def open(self):
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.bind((self.TCP_IP, self.TCP_PORT))
        self.s.listen(1)
        print('\n Server started...')
        
        self.conn, addr = self.s.accept()
        print('\n Connection address: ', addr)
        while True:
            data = self.conn.recv(self.BUFFER_SICE)
            if not data: break
            print('\n received data: ', data) 
            # TODO handle incoming commands correct
            self.conn.send(data)
        self.conn.close()

    def close(self):
        self.conn.close()

