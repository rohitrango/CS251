# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-11-01 08:25
from __future__ import unicode_literals

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('website', '0015_auto_20161031_1922'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='member',
            name='token',
        ),
    ]
