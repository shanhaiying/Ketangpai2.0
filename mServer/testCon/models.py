#!/usr/bin/env python
# coding=utf-8

from __future__ import unicode_literals
from django.db import models


# Create your models here.

#以下是学生登录信息表
#主要信息有 1.账号  2.密码
class Account(models.Model):
	username = models.CharField(max_length = 20,unique = True)
	password = models.CharField(max_length = 40)
	def __unicode__(self):
		return self.password
		
#下面是课程信息表
#主要信息有 1.课程名称 2.任课教师
class ClassInfo(models.Model):
	className = models.CharField(max_length = 20,unique = True)
	teacherName = models.CharField(max_length = 10)
	def getClassName(self):
		return self.className
	def getTeacherName(self):
		return self.teacherName
		
#下面是教师登录信息表
#主要信息有	1.账号	2.密码
class Account_Teacher(models.Model):
	username = models.CharField(max_length = 20)
	password = models.CharField(max_length = 40)
	def __unicode__(self):
		return self.password

#下面是学生个人信息表
#主要信息有 1.账号信息(外键) 2.学生姓名 3.出生日期 4.所属班级 5.性别 6.学号
class Student_Info(models.Model):
	username = models.ForeignKey('Account','username')
	StuName = models.CharField(max_length = 20)
	Birthday = models.DateField()	#date_joined=date(1962, 8, 16)
	ClassBelong = models.CharField(max_length = 20)
	Gender = models.CharField(max_length = 2)
	StuId = models.CharField(max_length = 20,unique = True)
	def getStuName(self):
		return self.StuName
	def getBirthday(self):
		return self.Birthday
	def getClassName(self):
		return self.ClassBelong
	def getGender(self):
		return self.Gender
	def getID(self):
		return self.StuId

#下面是学生选课信息表
#主要信息有 1.学生账号信息(外键) 2.学生归属课程(外键)
class Course_Pick(models.Model):
	StuId = models.ForeignKey('Student_Info','StuId')
	className = models.ForeignKey('ClassInfo','className')
	def getStuId(self):
		return self.StuId
	def getClassName(self):
		return self.className
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
