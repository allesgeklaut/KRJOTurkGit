'''
Created on 29.03.2018

@author: krems
'''
import TCPServer.TCPServer as TCPSrv

port = 5005
srv = TCPSrv.TCPServer(port)
srv.open()
