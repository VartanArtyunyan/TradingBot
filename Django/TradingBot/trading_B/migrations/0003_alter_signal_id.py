# Generated by Django 4.0.4 on 2022-07-09 17:59

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('trading_B', '0002_sd'),
    ]

    operations = [
        migrations.AlterField(
            model_name='signal',
            name='id',
            field=models.AutoField(editable=False, primary_key=True, serialize=False),
        ),
    ]
