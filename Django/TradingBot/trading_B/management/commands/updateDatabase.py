from operator import ge
import re
from xmlrpc.client import boolean
from django.core.management.base import BaseCommand
from requests import request
from trading_B.models import Signal, Calendar, Upcoming, Random
import json
import jsonData
import os
from django.db import models


fileIsEmpty = False


def fileEmpty():
    if os.path.getsize('jsonData/basedata.txt') == 0:
        return True
    else:
        return False


def read():
    if fileIsEmpty == False:
        x = open('jsonData/basedata.txt', mode='r')
        lines = x.readlines()
        arr = []
        for line in lines:
            arr.append(line.replace('\n', ''))
        x.close()
        y = json.dumps([json.loads(JSON_STRING)
                       for JSON_STRING in arr])
        json_object = (json.loads(y))
        return json_object


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
                j = vals["signal"]["parabolicSAR"]
                k = vals["signal"]["ema"]
                l = vals["signal"]["sma"]
                m = vals["signal"]["atr"]
                n = vals["signal"]["rsi"]
                o = vals["signal"]["realizedPL"]
                req = Signal(id=a, instrument=b, lastTime=c, buyingPrice=d,
                             lastPrice=e, takeProfit=f, stopLoss=g, macd=h, macdTriggered=i, parabolicSAR=j, ema=k, sma=l, atr=m, rsi=n, realizedPL=o)
                req.save()

            if(val == "calendar"):
                a = vals["calendar"]["id"]
                b = vals["calendar"]["instrument"]
                c = vals["calendar"]["factor"]
                d = vals["calendar"]["longShort"]
                e = vals["calendar"]["name"]
                f = vals["calendar"]["countryCode"]
                g = vals["calendar"]["realizedPL"]
                h = vals["calendar"]["buyingPrice"]
                i = vals["calendar"]["time"]

                req = Calendar(id=a, instrument=b, factor=c, longShort=d,
                               name=e, countryCode=f, realizedPL=g, buyingPrice=h, time=i)
                req.save()

            if(val == "upcoming"):
                a = vals["upcoming"]["id"]
                b = vals["upcoming"]["instrument"]
                c = vals["upcoming"]["volatility"]
                d = vals["upcoming"]["time"]
                e = vals["upcoming"]["name"]
                f = vals["upcoming"]["countryCode"]
                g = vals["upcoming"]["realizedPL"]
                h = vals["upcoming"]["buyingPrice"]
                req = Upcoming(id=a, instrument=b, volatility=c, time=d,
                               name=e, countryCode=f, realizedPL=g, buyingPrice=h)
                req.save()

            if(val == "random"):
                a = vals["random"]["id"]
                b = vals["random"]["instrument"]
                c = vals["random"]["buyingPrice"]
                d = vals["random"]["time"]
                e = vals["random"]["takeProfit"]
                f = vals["random"]["stopLoss"]
                g = vals["random"]["sellingPrice"]
                h = vals["random"]["realizedPL"]
                req = Random(id=a, instrument=b, buyingPrice=c, time=d,
                             takeProfit=e, stopLoss=f, sellingPrice=g, realizedPL=h)
                req.save()

            if(val == "update"):
                id = vals["update"]["id"]
                typ = vals["update"]["type"]
                pl = vals["update"]["realizedPL"]

                if typ == "signal":
                    try:
                        Signal_ID = Signal.objects.get(pk=id)
                        Signal_ID.realizedPL = pl
                        Signal_ID.save()
                    except:
                        print("Error occured, please investigate")
                if typ == "calendar":
                    try:
                        Calender_ID = Calendar.objects.get(pk=id)
                        Calender_ID.realizedPL = pl
                        Calender_ID.save()
                    except:
                        print("Error occured, please investigate")

                if typ == "upcoming":
                    try:
                        Upcoming_ID = Upcoming.objects.get(pk=id)
                        Upcoming_ID.realizedPL = pl
                        Upcoming_ID.save()
                    except:
                        print("Error occured, please investigate")

                if typ == "random":
                    try:
                        Random_ID = Random.objects.get(pk=id)
                        Random_ID.realizedPL = pl
                        Random_ID.save()
                    except:
                        print("Error occured, please investigate")


def removeFileContent():
    x = open('jsonData/basedata.txt', mode='w')
    x.truncate()


class Command(BaseCommand):
    def handle(self, *args, **options):
        print("Command")
        fileIsEmpty = fileEmpty()
        if not fileIsEmpty:
            saveStuff()
            removeFileContent()
