#!/usr/bin/env python
# coding=utf-8

from django.http import HttpResponse
from django.shortcuts import render
from testCon.models import test
from django.http import JsonResponse
from testCon.models import Student
from testCon.models import Account
import simplejson
import json
def hello(request):
    return HttpResponse("HelloWorld")

def testdb(request):
	test1 = test(name='w3cschool.cc')
	test1.save()
	return HttpResponse("<p>数据添加成功！</p>")
def fff(request):
    data = {"name":"james","age":"12"}
    context = {}
    context['test'] = "test"
    context['hello'] = "HelloWorld"
    context['name'] = (eval(JsonResponse(data).content))['name']
    context['age'] = (eval(JsonResponse(data).content))['age']
    return render(request,"hello.html",context)
def json(request):
    if request.method == 'POST':
        temp = simplejson.loads(request.body)
	tempname = temp["name"]
	tempid = temp["id"]
	tempdate = temp["date"]
	stu = Student.objects.create(name = tempname,mid = tempid,Date = tempdate)
        stu.save()
        return HttpResponse(request.body)
    else:
    	data = {"name":"james","id":"11111","date":"2014"}
        return render(request,"json.html",data)
		
from django.core import serializers
def check(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		musername = recv["username"]
		mpassword = recv["password"]
		try:
			key = Account.objects.get(username = musername).__unicode__()
		except Account.DoesNotExist:
			dict = {}
			dict["result"] = "not_exist"
			return JsonResponse(dict)#账号不存在
		if str(key) == str(mpassword):
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)#登录成功
		else:
			dict = {}
			dict["result"] = "error"
			return JsonResponse(dict)#密码不正确
	else:
		return HttpResponse("Working")
		
		
def register(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		musername = recv["username"]
		mpassword = recv["password"]
		try:
			key = Account.objects.get(username = musername).__unicode__()
			dict = {}
			dict["result"] = "already_exist"
			return JsonResponse(dict)
		except Account.DoesNotExist:
			account = Account(username = musername,password = mpassword)
			account.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
	else:
		return HttpResponse("Working")


