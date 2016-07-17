#!/usr/bin/env python
# coding=utf-8

from django.shortcuts import render
from django.http import JsonResponse
from django.http import HttpResponse
from band.models import Account
import simplejson
# Create your views here.


#登录验证
def login(request):
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
		return HttpResponse("It is band Working")

	
def band_register(request):
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
		return HttpResponse("band is Working")
