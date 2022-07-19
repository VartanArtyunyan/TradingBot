from django.core.management.base import BaseCommand

from trading_B.models import Signal, Calendar, Upcoming, Random, Balance
import requests
from trading_B.api import defs
import json


def saveBalance():
    session = requests.Session()

    response = session.get(
        f"{defs.OANDA_URL}/accounts/{defs.ACCOUNT_ID}/summary", headers=defs.SECURE_HEADER)

    account_data = json.loads(response.content)
    a = account_data["account"]["balance"]
    req = Balance(value=a,)
    req.save()


class Command(BaseCommand):
    def handle(self, *args, **options):
        saveBalance()
