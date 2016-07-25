#!/usr/bin/env python
# coding=utf-8

from django.shortcuts import render
from django.http import JsonResponse
from django.http import HttpResponse
from band.models import Account
from band.models import Personal
import simplejson
# Create your views here.


#登录验证
def band_login(request):
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

#注册
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

		
#保存资料到服务器
# 主要信息有 1.账号信息 2.用户昵称 3.性别 4.手机号 5.email 6.状态 7.出生日期
# return "{username:" + username + ",nickname:" + nickname + ",sex:" + sex + ",telephone:" + telephone + ",email:" + email + ",status:" + status + ",birthday:" + birthday + "}";
def band_save_profile(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		mUsername = recv["username"]
		mNickname = recv["nickname"]
		mSex = recv["sex"]
		mTelephone = recv["telephone"]
		mEmail = recv["email"]
		mStatus = recv["status"]
		mBirthday = recv["birthday"]
		# 根据django文档 下述两种情况应当可以合并
		try:  # 若本身在数据库中已存在资料信息 则修改之
			key = Personal.objects.get(username=mUsername)
			key.nickname = mNickname
			key.birthday = mBirthday
			key.sex = mSex
			key.telephone = mTelephone
			key.email = mEmail
			key.status = mStatus
			key.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
		except Personal.DoesNotExist:  # 第一次存储时 不存在账号信息
			person_profile = Personal(username=mUsername, nickname=mNickname, birthday=mBirthday, telephone=mTelephone,
									  email=mEmail, sex=mSex,status=mStatus)
			person_profile.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
	else:
		return HttpResponse("Working")
		
#从服务器获取个人资料信息
def band_get_profile(request):
	if request.method == 'POST':
		return HttpResponse("Working")
	else:
		return HttpResponse("Working")