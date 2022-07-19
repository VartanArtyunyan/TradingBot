from django.shortcuts import render
from django.http import HttpResponse
from trading_B.api import plots as pl
from trading_B.models import Signal, Calendar, Upcoming, Random, Balance
import matplotlib.pyplot as plt
from io import StringIO

""" Hier werden die Daten, welche fuer die Darstellung benoetigt werden hinzugefuegt """


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


""" Die folgenden Vier methoden berechnen den Gewinn der einzelnen Positionen """


def getSignalPL():
    value = Signal.objects.values_list('realizedPL')
    va = [t for t in value if None not in t]
    PL = 0
    for i in va:
        PL += i[0]
    return PL


def getCalendarPL():
    value = Calendar.objects.values_list('realizedPL')
    va = [t for t in value if None not in t]
    PL = 0
    for i in va:
        PL += i[0]
    return PL


def getUpcomingPL():
    value = Upcoming.objects.values_list('realizedPL')
    va = [t for t in value if None not in t]
    PL = 0
    for i in va:
        PL += i[0]
    return PL


def getRandomPL():
    value = Random.objects.values_list('realizedPL')
    va = [t for t in value if None not in t]
    PL = 0
    for i in va:
        PL += i[0]
    return PL


""" def displaySignals():
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
    fig = plt.figure()
    plt.plot(second, first)
    imgdata = StringIO()
    fig.savefig(imgdata, format='svg')
    imgdata.seek(0)
    data = imgdata.getvalue()
    return data """


""" Homepage """


def index(request):
    chart = displayBalance()
    PL1 = getSignalPL()
    PL2 = getCalendarPL()
    PL3 = getUpcomingPL()
    PL4 = getRandomPL()
    pr = []
    str1 = "Signale: " + str(PL1)
    str2 = "Calendar: " + str(PL2)
    str3 = "Upcoming: " + str(PL3)
    str4 = "Random: " + str(PL4)
    pr.append(chart)
    pr.append(str1)
    pr.append(str2)
    pr.append(str3)
    pr.append(str4)
    return render(request, 'trading_B/home.html', {"prs": pr})


""" Signal Page """


def signal(request):
    value = Signal.objects.all()
    a = []
    for val in value:
        a.append(val)
    return render(request, 'trading_B/signal.html', {"sigs": a})


""" Calendar Page """


def calendar(request):
    value = Calendar.objects.all()
    a = []
    for val in value:
        a.append(val)
    return render(request, 'trading_B/calendar.html', {"sigs": a})


""" Upcoming Page """


def upcoming(request):
    value = Upcoming.objects.all()
    a = []
    for val in value:
        a.append(val)
    return render(request, 'trading_B/upcoming.html', {"sigs": a})


""" Random Page """


def random(request):
    value = Random.objects.all()
    a = []
    for val in value:
        a.append(val)
    return render(request, 'trading_B/random.html', {"sigs": a})
