import base64
import numpy as np
from matplotlib import style
import matplotlib.pyplot as plt
from matplotlib import style
from io import BytesIO
import pandas as pd
import mplfinance as mpf


def get_graph():
    buffer = BytesIO()
    plt.savefig(buffer, format='png')
    buffer.seek(0)
    image_png = buffer.getvalue()
    graph = base64.b64encode(image_png)
    graph = graph.decode('utf-8')
    buffer.close()
    return graph


def get_plot(x, y):
    plt.switch_backend('AGG')
    fig, ax = plt.subplots(figsize=(10, 7))
    ax.plot(x, y)
    start, end = ax.get_xlim()
    ax.set_xticks(np.arange(start, end, 4))
    plt.gca().ticklabel_format(axis="y", useOffset=False)
    plt.gcf().autofmt_xdate()
    plt.plot()
    graph = get_graph()
    return graph
