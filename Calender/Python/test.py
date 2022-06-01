from datetime import datetime

word = "2022-05-01T01:30:00Z"

word = datetime.strptime(word, "%Y-%m-%dT%H:%M:%SZ")
print(word)
print(type(word)) 

date = datetime.now()

timedelta = date - word

print(date - word)



""" word = word[0:len(word)-1]
word = word.replace('T', ' ')
print(datetime.fromisoformat(word))
print(type(word)) """
""" word = datetime.strptime(word, '%y-%m-%d %H:%M:%S')


print ("The type of the date is now",  type(word))
print ("The date is", word) """

""" date_time_str = '18/09/19T01:55:19'
date_time_str = date_time_str.replace('T', ' ')



date_time_obj = datetime.strptime(date_time_str, '%d/%m/%y %H:%M:%S')


print ("The type of the date is now",  type(date_time_obj))
print ("The date is", date_time_obj) """