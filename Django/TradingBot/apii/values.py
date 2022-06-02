import json
import numpy as np
from numpy import double
import pandas as pd
import matplotlib.pyplot as plt


def read():
    x = open('transaction.json')
    text = x.read()
    x.close()
    y = json.loads(text)
    return y


transaction = read()

accountBalance = []
time = []

for i in transaction:
    accountBalance.append(i['accountBalance'])
    val = i['time'].split('T')
    time.append(val[0])


desired_array = [double(numeric_string) for numeric_string in accountBalance]
data_dict = pd.DataFrame(dict(zip(desired_array, zip(time))))
df = pd.read_json('transaction.json')
df.drop('userID', axis=1, inplace=True)
df.drop('batchID', axis=1, inplace=True)
df.drop('accountID', axis=1, inplace=True)
df.drop('positionFinancings', axis=1, inplace=True)
df.drop('baseHomeConversionCost', axis=1, inplace=True)
df.drop('homeConversionCost', axis=1, inplace=True)
df.drop('type', axis=1, inplace=True)
df.drop('id', axis=1, inplace=True)
df[['time', 'B']] = df['time'].str.split('T', 1, expand=True)
df.drop('B', axis=1, inplace=True)

df.index = pd.DatetimeIndex(df['time'])
df['open'] = df['financing'] * -1 + df['accountBalance']
df['close'] = df['accountBalance']
df.drop('accountBalance', axis=1, inplace=True)
