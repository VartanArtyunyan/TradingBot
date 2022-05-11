import Connection
import JsonReader

def DateTimeSplitter(word):
    word = word[0:len(word)-1]
    word = word.split('T')
    return word



Connection.start()

JsonArray = JsonReader.read()


for i in JsonArray:
    nextEvent = i["dateUtc"]
    print(i)
    nextEvent = DateTimeSplitter(nextEvent)
    print(nextEvent)


