import re
from xmlrpc.client import boolean
from django.core.management.base import BaseCommand
from trading_B.models import Signal
import json
import jsonData
import os


fileIsEmpty = False


def fileEmpty():
    if os.path.getsize('jsonData/basedata.json') == 0:
        return True
    else:
        return False


def read():
    if fileIsEmpty == False:
        x = open('jsonData/basedata.json', mode='r')
        text = x.read()
        x.close()
        y = json.loads(text)
        return y


def saveStuff():
    basedata = read()
    for vals in basedata:
        if(vals == "signal"):
            a = basedata["signal"]["id"]
            b = basedata["signal"]["instrument"]
            c = basedata["signal"]["lastTime"]
            d = basedata["signal"]["buyingPrice"]
            e = basedata["signal"]["lastPrice"]
            f = basedata["signal"]["takeProfit"]
            g = basedata["signal"]["stopLoss"]
            h = basedata["signal"]["macd"]
            i = basedata["signal"]["macdTriggered"]
            j = basedata["signal"]["parabolicSAR14"]
            k = basedata["signal"]["ema200"]
            l = basedata["signal"]["sma"]
            m = basedata["signal"]["atr"]
            n = basedata["signal"]["rsi"]
            req = Signal(id=a, instrument=b, lastTime=c, buyingPrice=d,
                         lastPrice=e, takeProfit=f, stopLoss=g, macd=h, macdTriggered=i, parabolicSAR=j, ema=k, sma=l, atr=m, rsi=n)
            req.save()

        if(vals == "calendar"):
            print(2)
        if(vals == "upcoming"):
            print(3)
        if(vals == "random"):
            print(4)
        if(vals == "update"):
            print(5)


def removeFileContent():
    x = open('jsonData/basedata.json', mode='w')
    x.truncate()


class Command(BaseCommand):

    def handle(self, *args, **options):
        print("Command")
        fileIsEmpty = fileEmpty()
        if not fileIsEmpty:
            saveStuff()
            removeFileContent()
