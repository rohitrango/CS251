# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-11-02 11:42
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('website', '0016_remove_member_token'),
    ]

    operations = [
        migrations.AddField(
            model_name='member',
            name='token',
            field=models.TextField(blank=True, default='-1', null=True),
        ),
    ]
