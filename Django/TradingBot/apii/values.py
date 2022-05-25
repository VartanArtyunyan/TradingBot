import json


def read():
    x = open('sample.json')
    text = x.read()
    x.close()
    y = json.loads(text)
    return y


print(read())
