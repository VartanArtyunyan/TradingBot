# Generated by Django 4.0.4 on 2022-07-15 12:54

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('trading_B', '0005_calendar_realizedpl_random_realizedpl_and_more'),
    ]

    operations = [
        migrations.DeleteModel(
            name='Calendar',
        ),
        migrations.DeleteModel(
            name='Random',
        ),
        migrations.DeleteModel(
            name='sd',
        ),
        migrations.DeleteModel(
            name='Signal',
        ),
        migrations.DeleteModel(
            name='Upcoming',
        ),
    ]
