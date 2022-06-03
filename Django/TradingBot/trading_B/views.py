from django.shortcuts import render
from django.http import HttpResponse
import apii.plotss as pltt
import apii.values as vals


def index(request):
    chart = pltt.get_plot(vals.df["time"], vals.df["close"])
    return render(request, 'trading_B/hello.html', {'chart': chart})
