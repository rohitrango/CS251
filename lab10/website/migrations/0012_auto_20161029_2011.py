# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-10-29 20:11
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('website', '0011_auto_20161028_2009'),
    ]

    operations = [
        migrations.AlterField(
            model_name='assignment',
            name='deadline',
            field=models.DateTimeField(),
        ),
    ]
