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
from testCon.models import Student_Info
import simplejson
from django.db.models import *


	
def staff(request):
	return HttpResponse("hi")
#如果你知道只有一个对象满足你的查询，你可以使用管理器的get() 方法，它直接返回该对象：
#>>> one_entry = Entry.objects.get(pk=1)

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
	
	
#保存资料
#主要信息有 1.账号信息 2.学生姓名 3.出生日期 4.所属班级 5.性别 6.学号
#return "{username:" + username + ",stuName:" + stuName + ",birthday:" + getBirthday(1) + ",class_belong:" + classBelong + ",sex:" + sex + ",stuID:" + stuId + "}";
#database字段名：username StuName Birthday ClassBelong Sex StuId
def save_profile(request):
	if request.method == 'POST':
		recv = simplejson.loads(request.body)
		mUsername = recv["username"]
		mStuID = recv["stuID"]
		mClass_belong = recv["class_belong"]
		mBirthday = recv["birthday"]
		mSex = recv["sex"]
		mStuName = recv["stuName"]
		#根据django文档 下述两种情况应当可以合并
		try:#若本身在数据库中已存在资料信息 则修改之
			key = Student_Info.objects.get(username = mUsername)
			#下面为失败的更新数据方法-。-
			#newStuInfo = Student_Info(username = mUsername,StuId = mStuID,Birthday = mBirthday,ClassBelong = mClass_belong,StuName = mStuName,Sex = mSex)
			#newStuInfo.update()
			#key.update(StuId = mStuID,Birthday = mBirthday,ClassBelong = mClass_belong,StuName = mStuName,Sex = mSex)
			key.StuId = mStuID
			key.ClassBelong = mClass_belong
			key.Birthday = mBirthday
			key.Sex = mSex
			key.StuName = mStuName
			key.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
		except Student_Info.DoesNotExist:#第一次存储时 不存在账号信息
			newStuInfo = Student_Info(username = mUsername,StuId = mStuID,Birthday = mBirthday,ClassBelong = mClass_belong,StuName = mStuName,Sex = mSex)
			newStuInfo.save()
			dict = {}
			dict["result"] = "success"
			return JsonResponse(dict)
	else:
		return HttpResponse("Working")
	
	
'''
Django 如何知道是UPDATE 还是INSERT

你可能已经注意到Django 数据库对象使用同一个save() 方法来创建和改变对象。Django 对INSERT 和UPDATE SQL 语句的使用进行抽象。当你调用save() 时，Django 使用下面的算法：

如果对象的主键属性为一个求值为True 的值（例如，非None 值或非空字符串），Django 将执行UPDATE。
如果对象的主键属性没有设置或者UPDATE 没有更新任何记录，Django 将执行INSERT。
现在应该明白了，当保存一个新的对象时，如果不能保证主键的值没有使用，你应该注意不要显式指定主键值。关于这个细微差别的更多信息，参见上文的显示指定主键的值 和下文的强制使用INSERT 或UPDATE。

在Django 1.5 和更早的版本中，在设置主键的值时，Django 会作一个 SELECT。如果SELECT 找到一行，那么Django 执行UPDATE，否则执行INSERT。旧的算法导致UPDATE 情况下多一次查询。有极少数的情况，数据库不会报告有一行被更新，即使数据库包含该对象的主键值。有个例子是PostgreSQL 的ON UPDATE 触发器，它返回NULL。在这些情况下，可能要通过将select_on_save 选项设置为True 以启用旧的算法。
'''
	
	
	
	
	