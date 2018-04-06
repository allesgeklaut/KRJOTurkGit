'''
Created on 29.03.2018

@author: krems
Server class which connects to sql database
'''
from TCPServer.TCPServer import TCPServer
import asyncore
from DBConnector.DBConnector import DBConnector
from enum import Enum
from _mysql_exceptions import MySQLError
import numpy as np
import cv2
import os.path


# handles database requests and echos the result
class EchoHandler(asyncore.dispatcher_with_send):
    
    def __init__(self, sock):
        asyncore.dispatcher_with_send.__init__(self, sock)
        self.db = DBConnector('root', 'root')
        self.db.connect()
        self.headerSize = 4  # size of data size header and CID header
        self.commandBytes = 64  # size of SQL Command placeholder
        self.dataBuffer = b""  
        self.dataSize = 0        

    # gets called on new data 
    def handle_read(self):
        data = self.recv(1024)
        rawData = None
        status = None
        if data:
            if self.dataSize == 0:
                self.dataSize = self.parseDataSize(data[0:self.headerSize])
                self.dataBuffer += data[self.headerSize:len(data)]
            else:
                self.dataBuffer += data
            
            if len(self.dataBuffer) >= self.dataSize:
                CID, command, imageData = self.parser(self.dataBuffer)
                if commandID(CID) == commandID.getData:
                    status, rawData = self.getData(command)
                elif commandID(CID) == commandID.setData:
                    status, rawData = self.setData(command)
                elif commandID(CID) == commandID.uploadImage:
                    status, rawData = self.uploadImage(imageData, command)
                elif commandID(CID) == commandID.downloadImage:
                    status, rawData = self.downloadImage(command)
                rawData = str(status).encode() + rawData
                
                # header with size for echo  
                dataLength = len(rawData)
                dl = [0] * self.headerSize
                dl[0] = dataLength & 0xff
                dl[1] = (dataLength >> 8) & 0xff
                dl[2] = (dataLength >> 16) & 0xff
                dl[3] = (dataLength >> 24) & 0xff 
                dl = list(reversed(dl))
                BDataLength = bytes(dl)
                
                # send back data with header
                self.send(BDataLength + rawData)
        
    # get data from database
    def getData(self, SQLcommand):
        try:
            c = self.db.connection.cursor()
            c.execute(SQLcommand)
            tup = c.fetchall()
            # serialize all tuples received from database
            out = ''
            for k in range(0, len(tup)):
                out += self.serialize(tup[k])
            # return in bytes
            c.close()
            return 0, out.encode()
        except MySQLError as e:
            return -1, b"MYSQL error: " + repr(e).encode()
        except:
            return -1, ''
    
    def setData(self, SQLcommand):
        try:
            c = self.db.connection.cursor()
            c.execute(SQLcommand)
            self.db.connection.commit()
            c.close()
            return 0, b""
        except MySQLError as e:
            return -1, b"MYSQL error: " + repr(e).encode()
        except:
            return -1, b""
        
    def uploadImage(self, image, command):
        # saves foto on filesystem; saves nothing into database
        try:
            fotoName = command
            nparr = np.fromstring(image, np.uint8)
            img_np = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
            path = os.path.join("ServerData", fotoName + ".jpg")
            cv2.imwrite(path, img_np)
            return 0, fotoName.encode()
        except ... as e: 
            return -1, b"Error: " + repr(e).encode()

    def downloadImage(self, command):
        try:
            fotoName = command
            path = os.path.join("ServerData", fotoName + ".jpg")
            img_np = cv2.imread(path)
            _, img_array = cv2.imencode(".jpg", img_np)  
            return 0, np.ndarray.tobytes(img_array)
        except ... as e:
            return -1, b"Error: " + repr(e).encode()

    # serialize a tuple from database into string
    def serialize(self, inTup):
        out = '('
        for j in range(0, len(inTup)):
            out += str(inTup[j])
            out += ','
        out = out[0:-1]
        out += ')'
        return out
        
    # parses the commandstring from client
    def parser(self, inString):
        commandString = inString
        
        commandID = commandString[0:self.headerSize]
        commandID = int.from_bytes(commandID, byteorder='big')
        commandString = commandString[self.headerSize:len(commandString)]
        if (len(commandString) >= self.commandBytes):
            sqlCommand = commandString[0:self.commandBytes]
            imageData = commandString[self.commandBytes:len(commandString)]
            return commandID, sqlCommand.decode().strip("\00x") , imageData
        else:
            return commandID, commandString.decode().strip() , ''
        
    def parseDataSize(self, inString):
        return int.from_bytes(inString, byteorder='big')


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

