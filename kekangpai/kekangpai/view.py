#!/usr/bin/env python
# coding=utf-8

from django.http import HttpResponse
from django.shortcuts import render
from django.http import JsonResponse
from testCon.models import Account
from testCon.models import Account_Teacher
import simplejson
import json

def hello(request):
	return HttpResponse("hi")
def welcome(request):
	context = {}
	return render(request,'hello.html',context)
