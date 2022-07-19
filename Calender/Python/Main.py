import datetime
import json
import time
import Connection
import ReaderWriter
from Client import Client
from StoreList import StoreList
from threading import Event


def main():

    # Berechnet die Zeit wie lange das Programm bis zum naechsten Tag 0:00 Uhr schlafen soll
    def pause_timer():
        next_day = (datetime.datetime.utcnow() + datetime.timedelta(days=1)
                    ).replace(hour=0, minute=0, second=0, microsecond=0)
        seconds = (next_day - datetime.datetime.utcnow()).total_seconds()
        return seconds

    cl = Client()
    list_pairs = json.loads(cl.read())

    while(True):
        print("I'm awake, today is " + str(datetime.date.today()))

        # Änderung des Filenamen hier möglich
        file_name = "Events" + str(datetime.date.today()) + ".json"
        income_json = Connection.start()

        # Daten können bei Bedarf aus Dokument wieder ausgelesen werden
        ReaderWriter.writeInDocument(income_json, file_name)
        list_news = ReaderWriter.openJsonFile(file_name)

        List_Storage = StoreList(list_news, list_pairs, cl)

        # beseitigt den Bug, dass nicht alle ausgefilter werden beim ersten Durchlauf
        for i in range(3):
            List_Storage.filterOldEvents()

        while (bool(List_Storage.list_news)):
            List_Storage.filterOldEvents()

            List_Storage.check_actual_events
            List_Storage.check_upcoming_events

        print("Finished all events for today")
        print("It's time to sleep")
        time.sleep(pause_timer())


if __name__ == "__main__":
    main()
