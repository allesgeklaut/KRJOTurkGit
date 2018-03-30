'''
Created on 29.03.2018

@author: krems
'''
import socket
import asyncore


class TCPServer(asyncore.dispatcher):

    def __init__(self, port):
        asyncore.dispatcher.__init__(self)
        self.TCP_IP = '127.0.0.1'
        self.TCP_PORT = port
        self.open()

    def open(self):
        self.create_socket(socket.AF_INET, socket.SOCK_STREAM)
        self.bind((self.TCP_IP, self.TCP_PORT))
        self.listen(5)  # TODO find out what the int stands for
        print('\n Server started...')   

    def close(self):
        pass

