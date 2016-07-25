#!/usr/bin/env python
# coding=utf-8

from __future__ import unicode_literals

from django.db import models

# Create your models here.

'''
学院派会告诉你在设计的时候把应该有的约束都加上

而实践派得出的结论是主键一定加，非空约束尽量加，外键最好依赖于程序逻辑，而不是数据库，从而更好的拥抱变化，快速响应，数据库也会有相对较好的性能
'''


class Account(models.Model):
	username = models.CharField(max_length = 20,primary_key = True)#长度限制逻辑写在app中
	password = models.CharField(max_length = 40)
	def __unicode__(self):
		return self.password

class Personal(models.Model):
	account = models.ForeignKey('Account',primary_key = True)#整个对象是一个属性
	nickname = models.CharField(max_length = 40)
	sex = models.CharField(max_length = 2)
	telphone = models.CharField(max_length = 20)
	email = models.CharField(max_length = 40)
	status = models.CharField(max_length = 200)
	birthday = models.CharField(max_length = 20)
	def getnickname(self):
		return self.nickname
	def getgender(self):
		return self.sex
	def gettelphone(self):
		return self.telphone
	def getemail(self):
		return self.email
	def getstatus(self):
		return self.status
	def getBirthday(self):
		return self.birthday
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		