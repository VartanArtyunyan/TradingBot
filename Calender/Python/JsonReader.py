import json


x = open("jsonCalender2.txt")
text = x.read()
x.close()
y = json.loads(text)
print(y[0]["name"])
