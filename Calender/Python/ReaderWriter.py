import json

def writeInDocument(data, file_name):
    with open(file_name, "w") as f:
        f.write(data)
  
    
def openJsonFile(file_name):
    x = open(file_name)
    text = x.read()
    x.close()
    return json.loads(text)

