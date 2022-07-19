from django.shortcuts import render
from django.http import HttpResponse
from trading_B.api import plots as pl
from trading_B.models import Signal, Calendar, Upcoming, Random, Balance


def displayBalance():
    value = Balance.objects.values_list('value')
    time = Balance.objects.values_list('time')
    va = []
    ti = []
    for tim in time:
        ti.append(tim)
    for val in value:
        va.append(val)
    chart = pl.get_plot(ti, va)
    return chart


def displaySignals():
    value = Signal.objects.values_list('realizedPL', 'lastTime')
    va = []
    va = [t for t in value if None not in t]
    first = []
    second = []
    for i in va:
        first.append(i[0])
        second.append(i[1])
    chart = pl.get_plot("second", 2343)
    return chart


def displayCalendars():
    value = Calendar.objects.values_list('realizedPL', 'time')
    va = []
    va = [t for t in value if None not in t]
    first = []
    second = []
    for i in va:
        print(type(i))
        first.append(i[0])
        second.append(i[1])
    chart = pl.get_plot(second, first)
    return chart


def displayUpcoming():
    value = Upcoming.objects.values_list('realizedPL', 'time')
    va = []
    va = [t for t in value if None not in t]
    first = []
    second = []
    for i in va:
        first.append(i[0])
        second.append(i[1])
    chart = pl.get_plot(second, first)
    return chart


def displayRandoms():
    value = Random.objects.values_list('realizedPL', 'time')
    va = []
    va = [t for t in value if None not in t]
    first = []
    second = []
    for i in va:
        first.append(i[0])
        second.append(i[1])
    chart = pl.get_plot(second, first)
    return chart


def index(request):
    chart = displayBalance()
    chart2 = displaySignals()
    chart3 = displayCalendars()
    chart4 = displayUpcoming()
    chart5 = displayRandoms()
    """ , {'chart5': chart5} """
    return render(request, 'trading_B/hello.html', {'chart': chart})


def signal(request):
    return render(request, 'trading_B/signal.html')


def calendar(request):
    return render(request, 'trading_B/calendar.html')


def upcoming(request):
    return render(request, 'trading_B/upcoming.html')


def random(request):
    return render(request, 'trading_B/random.html')
