# Generated by Django 4.0.4 on 2022-07-10 16:13

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('trading_B', '0003_alter_signal_id'),
    ]

    operations = [
        migrations.CreateModel(
            name='Calendar',
            fields=[
                ('id', models.AutoField(editable=False, primary_key=True, serialize=False)),
                ('instrument', models.CharField(max_length=10)),
                ('factor', models.FloatField()),
                ('longShort', models.BooleanField()),
                ('name', models.CharField(max_length=100)),
                ('countryCode', models.CharField(max_length=10)),
            ],
            options={
                'ordering': ['id'],
            },
        ),
        migrations.CreateModel(
            name='Random',
            fields=[
                ('id', models.AutoField(editable=False, primary_key=True, serialize=False)),
                ('instrument', models.CharField(max_length=10)),
                ('buyingPrice', models.FloatField()),
                ('time', models.CharField(max_length=25)),
                ('takeProfit', models.FloatField()),
                ('stopLoss', models.FloatField()),
                ('sellingPrice', models.FloatField()),
            ],
            options={
                'ordering': ['id'],
            },
        ),
        migrations.CreateModel(
            name='Upcoming',
            fields=[
                ('id', models.AutoField(editable=False, primary_key=True, serialize=False)),
                ('instrument', models.CharField(max_length=10)),
                ('volatility', models.CharField(max_length=10)),
                ('time', models.CharField(max_length=25)),
                ('name', models.CharField(max_length=100)),
                ('countryCode', models.CharField(max_length=10)),
            ],
            options={
                'ordering': ['id'],
            },
        ),
    ]
