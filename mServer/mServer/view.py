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
		temp = simplejson.loads(request.body)
		musername = temp["username"]
		mpassword = temp["password"]
		try:
			key = Account.objects.get(username = musername).__unicode__()
		except Account.DoesNotExist:
			dict = {}
			dict["result"] = "not_exist"
			ret = simplejson.dumps(dict)
			return JsonResponse(dict)#账号不存在
		if str(key) == str(mpassword):
			dict = {}
			dict["result"] = "success"
			ret = simplejson.dumps(dict)
			return JsonResponse(dict)#登录成功
		else:
			dict = {}
			dict["result"] = "error"
			ret = simplejson.dumps(dict)
			return JsonResponse(dict)#密码不正确
	else:
		return HttpResponse("Working")
		
	


