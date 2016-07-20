#!/usr/bin/env python
# coding=utf-8

from __future__ import unicode_literals
from django.db import models


# Create your models here.

'''
学院派会告诉你在设计的时候把应该有的约束都加上

而实践派得出的结论是主键一定加，非空约束尽量加，外键最好依赖于程序逻辑，而不是数据库，从而更好的拥抱变化，快速响应，数据库也会有相对较好的性能
'''

#以下是学生登录信息表
#主要信息有 1.账号  2.密码
class Account(models.Model):
	username = models.CharField(max_length = 20,unique = True)
	password = models.CharField(max_length = 40)
	def __unicode__(self):
		return self.password
		
#下面是课程信息表
#主要信息有 1.课程名称 2.任课教师 3.课程邀请码 4.教师用户名
class ClassInfo(models.Model):
	username = models.CharField(max_length = 20)
	className = models.CharField(max_length = 20,unique = True)
	teacherName = models.CharField(max_length = 10)
	inviteCode = models.CharField(max_length = 10,unique = True)
	def getClassName(self):
		return self.className
	def getTeacherName(self):
		return self.teacherName
	def getInviteCode(self):
		return self.inviteCode
		
#下面是教师登录信息表
#主要信息有	1.账号	2.密码	3.姓名
class Account_Teacher(models.Model):
	username = models.CharField(max_length = 20,unique = True)
	password = models.CharField(max_length = 40)
	def __unicode__(self):
		return self.password

#下面是学生个人信息表
#主要信息有 1.账号信息 2.学生姓名 3.出生日期 4.所属班级 5.性别 6.学号
class Student_Info(models.Model):
	username = models.CharField(max_length = 20)
	StuName = models.CharField(max_length = 20)
	Birthday = models.DateField()	#date_joined=date(1962, 8, 16)  插入格式可以为'2015,4,3'
	ClassBelong = models.CharField(max_length = 20)
	Sex = models.CharField(max_length = 10)
	StuId = models.CharField(max_length = 20,unique = True)
	def getStuName(self):
		return self.StuName
	def getBirthday(self):
		return self.Birthday
	def getClassName(self):
		return self.ClassBelong
	def getGender(self):
		return self.Sex
	def getID(self):
		return self.StuId

#下面是学生选课信息表
#主要信息有 1.学生账号信息 2.学生归属课程
class Course_Pick(models.Model):
	StuId = models.CharField(max_length = 20)#此处命名不规范 懒得改了
	className = models.CharField(max_length = 30)
	def getStuId(self):
		return self.stuId
	def getClassName(self):
		return self.className
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
