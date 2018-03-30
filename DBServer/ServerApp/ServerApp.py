'''
Created on 29.03.2018

@author: krems
Server class which connects to sql database
'''
from TCPServer.TCPServer import TCPServer
import asyncore
from DBConnector.DBConnector import DBConnector
from enum import Enum


# handles database requests and echos the result
class EchoHandler(asyncore.dispatcher_with_send):
    
    def __init__(self, sock):
        asyncore.dispatcher_with_send.__init__(self, sock)
        self.db = DBConnector('root', 'root')
        self.db.connect()

    def handle_read(self):
        data = self.recv(16384)
        if data:
            CID, sqlCommand, imageData = self.parser(data)
            if commandID(CID) == commandID.getData:
                self.send(self.getData(sqlCommand))
                
    commandIDBytes = 4
    commandBytes = 64
        
    # get data from database
    def getData(self, SQLcommand):
        c = self.db.connection.cursor()
        c.execute(SQLcommand)
        tup = c.fetchall()
        # serialize all tuples received from database
        out = ''
        for k in range(0, len(tup)):
            out += self.serialize(tup[k])
        # return in bytes
        return out.encode()
    
    # serialize a tuple from database into string
    def serialize(self, inTup):
        out = '('
        for j in range(0, len(inTup)):
            out += str(inTup[j])
            out += ','
        out = out[0:-1]
        out += ')'
        return out
    
    def setData(self, SQLCommand):
        pass
    
    def uploadImage(self, image, SQLCommand):
        pass
    
    def downloadImage(self, SQLCommand):
        pass
        
    # parses the commandstring from client
    def parser(self, inString):
        commandString = inString
        
        commandID = commandString[0:self.commandIDBytes]
        commandID = int(commandID, 2)
        commandString = commandString[self.commandIDBytes:len(commandString)]
        if (len(commandString) >= self.commandBytes):
            sqlCommand = commandString[0:self.commandBytes]
            imageData = commandString[self.commandBytes:len(commandString)]
            return commandID, sqlCommand.decode("utf-8") , imageData
        else:
            return commandID, commandString.decode("utf-8") , ''


class srv(TCPServer):

    def __init__(self, port):
        TCPServer.__init__(self, port)

    def handle_accept(self):
        pair = self.accept()
        if pair is not None:
            sock, addr = pair
            print('Incoming connection from %s', repr(addr))
            handler = EchoHandler(sock)

            
class commandID(Enum):
    getData = 0
    setData = 1
    uploadImage = 2
    downloadImage = 3

