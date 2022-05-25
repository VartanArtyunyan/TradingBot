from django.shortcuts import render
from django.http import HttpResponse
import apii.data as dat
import apii.plotss as pltt


def index(request):
    chart = pltt.get_plot(77, 3)
    return render(request, 'trading_B/hello.html', {'chart': chart})
