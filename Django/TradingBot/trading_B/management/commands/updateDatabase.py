from operator import ge
import re
from time import time
from xmlrpc.client import boolean
from django.core.management.base import BaseCommand
from requests import request
from trading_B.models import Signal, Calendar, Upcoming, Random, Balance
import json
import jsonData
import os
from django.db import models
import json

fileIsEmpty = False

""" Checkt ob die basedata datei leer ist """


def fileEmpty():
    if os.path.getsize('jsonData/basedata.txt') == 0:
        return True
    else:
        return False


""" Liest die Daten aus der basedata datei  """


def read():
    if fileIsEmpty == False:
        x = open('jsonData/basedata.txt', mode='r')
        lines = x.readlines()
        arr = []
        for line in lines:
            arr.append(line.rstrip('\n'))
        arr[:] = [item for item in arr if item != '']
        x.close()
        y = json.dumps([json.loads(JSON_STRING) for JSON_STRING in arr])
        json_object = (json.loads(y))
        return json_object


""" Speichert und Updated die Daten in der Datenbank  """


def saveStuff():
    basedata = read()
    for vals in basedata:
        for val in vals:
            if(val == "signal"):
                a = vals["signal"]["id"]
                b = vals["signal"]["instrument"]
                c = vals["signal"]["lastTime"]
                d = vals["signal"]["buyingPrice"]
                e = vals["signal"]["lastPrice"]
                f = vals["signal"]["takeProfit"]
                g = vals["signal"]["stopLoss"]
                h = vals["signal"]["macd"]
                i = vals["signal"]["macdTriggered"]
                j = vals["signal"]["parabolicSAR14"]
                k = vals["signal"]["ema200"]
                l = vals["signal"]["sma20"]
                m = vals["signal"]["sma50"]
                n = vals["signal"]["atr14"]
                o = "signal"
                req = Signal(id=a, instrument=b, lastTime=c, buyingPrice=d,
                             lastPrice=e, takeProfit=f, stopLoss=g, macd=h, macdTriggered=i, parabolicSAR14=j, ema200=k, sma20=l, sma50=m, atr14=n, typ=o)
                req.save()

            if(val == "calendar"):
                a = vals["calendar"]["id"]
                b = vals["calendar"]["instrument"]
                c = vals["calendar"]["factor"]
                d = vals["calendar"]["longShort"]
                e = vals["calendar"]["name"]
                f = vals["calendar"]["countryCode"]
                h = vals["calendar"]["buyingPrice"]
                j = "calendar"

                req = Calendar(id=a, instrument=b, factor=c, longShort=d,
                               name=e, countryCode=f, buyingPrice=h, typ=j)
                req.save()

            if(val == "upcoming"):
                a = vals["upcoming"]["id"]
                b = vals["upcoming"]["instrument"]
                c = vals["upcoming"]["volatility"]
                d = vals["upcoming"]["time"]
                e = vals["upcoming"]["name"]
                f = vals["upcoming"]["countryCode"]
                h = vals["upcoming"]["buyingPrice"]
                i = "upcoming"

                req = Upcoming(id=a, instrument=b, volatility=c, time=d,
                               name=e, countryCode=f, buyingPrice=h, typ=i)
                req.save()

            if(val == "random"):
                b = vals["random"]["instrument"]
                c = vals["random"]["buyingPrice"]
                e = vals["random"]["takeProfit"]
                f = vals["random"]["stopLoss"]
                h = "random"

                req = Random(instrument=b, buyingPrice=c,
                             takeProfit=e, stopLoss=f, typ=h)
                req.save()

            if(val == "update"):
                id = vals["update"]["id"]
                pl = vals["update"]["realizedPL"]
                if Signal.objects.filter(pk=id).exists():
                    try:
                        Signal_ID = Signal.objects.get(pk=id)
                        Signal_ID.realizedPL = pl
                        Signal_ID.save()
                    except:
                        print("Error occured, please investigate")
                if Calendar.objects.filter(pk=id).exists():
                    try:
                        Calender_ID = Calendar.objects.get(pk=id)
                        Calender_ID.realizedPL = pl
                        Calender_ID.save()
                    except:
                        print("Error occured, please investigate")

                if Upcoming.objects.filter(pk=id).exists():
                    try:
                        Upcoming_ID = Upcoming.objects.get(pk=id)
                        Upcoming_ID.realizedPL = pl
                        Upcoming_ID.save()
                    except:
                        print("Error occured, please investigate")

                if Random.objects.filter(pk=id).exists():
                    try:
                        Random_ID = Random.objects.get(pk=id)
                        Random_ID.realizedPL = pl
                        Random_ID.save()
                    except:
                        print("Error occured, please investigate")


""" Leert die basedata datei  """


def removeFileContent():
    x = open('jsonData/basedata.txt', mode='w')
    x.truncate()


class Command(BaseCommand):
    def handle(self, *args, **options):
        fileIsEmpty = fileEmpty()
        if not fileIsEmpty:
            saveStuff()
            removeFileContent()
