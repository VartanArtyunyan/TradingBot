from calendar import c
from django.db import models
import uuid
from django.utils.timezone import now

# Create your models here.


class Balance(models.Model):
    value = models.FloatField()
    time = models.DateTimeField(default=now, blank=True)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


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
    parabolicSAR14 = models.FloatField()
    ema200 = models.FloatField()
    sma20 = models.FloatField()
    sma50 = models.FloatField()
    atr14 = models.FloatField()
    realizedPL = models.FloatField(null=True, blank=True, default=None)
    typ = models.CharField(max_length=10)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


class Calendar(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    factor = models.FloatField()
    buyingPrice = models.FloatField()
    longShort = models.BooleanField()
    name = models.CharField(max_length=100)
    countryCode = models.CharField(max_length=10)
    realizedPL = models.FloatField(null=True, blank=True, default=None)
    time = models.DateTimeField(default=now, blank=True)
    typ = models.CharField(max_length=10)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


class Upcoming(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    volatility = models.CharField(max_length=10)
    time = models.DateTimeField(default=now, blank=True)
    name = models.CharField(max_length=100)
    countryCode = models.CharField(max_length=10)
    realizedPL = models.FloatField(null=True, blank=True, default=None)
    buyingPrice = models.FloatField()
    typ = models.CharField(max_length=10)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


class Random(models.Model):
    id = models.AutoField(primary_key=True, editable=False)
    instrument = models.CharField(max_length=10)
    buyingPrice = models.FloatField()
    time = models.DateTimeField(default=now, blank=True)
    takeProfit = models.FloatField()
    stopLoss = models.FloatField()
    realizedPL = models.FloatField(null=True, blank=True, default=None)
    typ = models.CharField(max_length=10)

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']
