'''
Created on 29.03.2018

@author: krems
'''
from ServerApp.ServerApp import srv
import asyncore

server = srv(8080)
asyncore.loop()

