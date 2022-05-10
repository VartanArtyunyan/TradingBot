import json


x = open("jsonCalender1.json")
text = x.read()
x.close()
y = json.loads(text)
print(y[2]["name"])
