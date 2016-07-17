#!/usr/bin/env python
# coding=utf-8

from django.http import HttpResponse
from django.shortcuts import render
from django.http import JsonResponse
from testCon.models import Account
from testCon.models import Account_Teacher
import simplejson
import json
	
from django.core import serializers


