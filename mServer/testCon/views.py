#!/usr/bin/env python
# coding=utf-8

from django.shortcuts import render
# Create your views here.


from django.http import HttpResponse
from django.shortcuts import render
from django.http import JsonResponse
from testCon.models import Account
from testCon.models import Account_Teacher
from testCon.models import ClassInfo
import simplejson
from django.db.models import *


	
def staff(request):
	return HttpResponse("hi")
	
#学生的登录验证
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
		
import hashlib
#学生的注册
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
			#hash_md5 = hashlib.md5(str(mpassword))
			#encrypt_password = hash_md5.hexdigest()
			account = Account(username = musername,password = mpassword)
			account.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
	else:
		data = "123456"
		hash_md5 = hashlib.md5(data)
		result = hash_md5.hexdigest()
		return HttpResponse(result)
		
#老师的注册
def register_teacher(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		musername = recv["username"]
		mpassword = recv["password"]
		try:
			key = Account_Teacher.objects.get(username = musername).__unicode__()
			dict = {}
			dict["result"] = "already_exist"
			return JsonResponse(dict)
		except Account_Teacher.DoesNotExist:
			#hash_md5 = hashlib.md5(str(mpassword))
			#encrypt_password = hash_md5.hexdigest()
			account = Account_Teacher(username = musername,password = mpassword)
			account.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
	else:
		data = "123456"
		hash_md5 = hashlib.md5(data)
		result = hash_md5.hexdigest()
		return HttpResponse(result)

#老师的登录验证
def check_teacher(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		musername = recv["username"]
		mpassword = recv["password"]
		try:
			key = Account_Teacher.objects.get(username = musername).__unicode__()
		except Account_Teacher.DoesNotExist:
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
	
#课程的添加
#接受到的json格式为
#{"username":username, 
#"teacherName":teacherName, 
#"className": className, 
#"inviteCode":inviteCode}
def create_course(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		musername = recv["username"]
		mteacherName = recv["teacherName"]
		mclassName = recv["className"]
		inviteCode = recv["inviteCode"]
		try: 
			key = ClassInfo.objects.get(className = mclassName)
		except ClassInfo.DoesNotExist:
			newClass = ClassInfo(username = musername,teacherName = mteacherName,className = mclassName,inviteCode = inviteCode)
			newClass.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
		dict = {}
		dict["result"] = "already_exist"
		return JsonResponse(dict)
	else:
		return HttpResponse("Working")
	
	
	
	
	
	
	
	
	
	
	
	