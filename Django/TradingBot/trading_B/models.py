from calendar import c
from django.db import models
import uuid

# Create your models here.


class sd(models.Model):
    instrument = models.CharField(max_length=10)
    lastTime = models.CharField(max_length=25)
    buyingPrice = models.FloatField()


class Signal(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
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

    def __getitem__(self):
        return self.id

    class Meta:
        ordering = ['id']


""" class Calendar(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    instrument = models.CharField(max_length=10)
    rating = models.IntegerField()

    def __str__(self):
        return self.id

    class Meta:
        ordering = ['id']



class Upcoming(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    instrument = models.CharField(max_length=10)
    rating = models.IntegerField()

    def __str__(self):
        return self.id

    class Meta:
        ordering = ['id']



class Random(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    instrument = models.CharField(max_length=10)
    rating = models.IntegerField()

    def __str__(self):
        return self.id

    class Meta:
        ordering = ['id']
 """
