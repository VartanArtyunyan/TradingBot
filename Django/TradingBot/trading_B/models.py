from calendar import c
from django.db import models
import uuid

# Create your models here.


class sd(models.Model):
    instrument = models.CharField(max_length=10)
    lastTime = models.CharField(max_length=25)
    buyingPrice = models.FloatField()


class Signal(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    lastTime = models.CharField(max_length=25)
    buyingPrice = models.FloatField()
    lastPrice = models.FloatField()
    takeProfit = models.FloatField()
    stopLoss = models.FloatField()
    macd = models.FloatField()
    macdTriggered = models.FloatField()
    parabolicSAR = models.FloatField()
    ema = models.FloatField()
    sma = models.FloatField()
    atr = models.FloatField()
    rsi = models.FloatField()
    realizedPL = models.FloatField(null=True, blank=True, default=None)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


class Calendar(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    factor = models.FloatField()
    longShort = models.BooleanField()
    name = models.CharField(max_length=100)
    countryCode = models.CharField(max_length=10)
    realizedPL = models.FloatField(null=True, blank=True, default=None)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


class Upcoming(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    volatility = models.CharField(max_length=10)
    time = models.CharField(max_length=25)
    name = models.CharField(max_length=100)
    countryCode = models.CharField(max_length=10)
    realizedPL = models.FloatField(null=True, blank=True, default=None)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


class Random(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    buyingPrice = models.FloatField()
    time = models.CharField(max_length=25)
    takeProfit = models.FloatField()
    stopLoss = models.FloatField()
    sellingPrice = models.FloatField()
    realizedPL = models.FloatField(null=True, blank=True, default=None)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']
