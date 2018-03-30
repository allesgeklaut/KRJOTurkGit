'''
Created on 30.03.2018

@author: krems
'''
import MySQLdb


class DBConnector(object):

    # Singleton Stuff
    class __DBConnector(object):

        def __init__(self, USERNAME, PWD, URL):
            self.URL = URL
            self.USERNAME = USERNAME
            self.PWD = PWD
        
    instance = None
    connection = None
    
    def __init__(self, USERNAME, PWD, URL=None):
        
        if not DBConnector.instance:
            self.instance = DBConnector.__DBConnector(USERNAME, PWD, URL)
        else:
            self.instance.URL = URL
            self.instance.USERNAME = USERNAME
            self.instance.PWD = PWD

    def __getattr__(self, name):
        return getattr(self.instance, name)
    
    # DB Connection
    def connect(self):
        self.connection = MySQLdb.connect(user=self.instance.USERNAME,
                                          passwd=self.instance.PWD, db='OADTurk')     
