# Generated by Django 4.0.4 on 2022-07-17 15:55

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('trading_B', '0009_balance'),
    ]

    operations = [
        migrations.AlterField(
            model_name='balance',
            name='time',
            field=models.DateField(auto_now_add=True),
        ),
    ]