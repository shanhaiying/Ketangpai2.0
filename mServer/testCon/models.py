from __future__ import unicode_literals

from django.db import models

# Create your models here.

class test(models.Model):
	name = models.CharField(max_length = 20)
	def __unicode__(self):
            return self.name

class Student(models.Model):
	name = models.CharField(max_length = 10)
	mid = models.IntegerField()
	Date = models.IntegerField()
	def __unicode__(self):
            return self.name
class Account(models.Model):
	password = models.CharField(max_length = 20)
	username = models.CharField(max_length = 20)
	def __unicode__(self):
		return self.password
		
class ClassInfo(models.Model):
	className = models.CharField(max_length = 20)
	teacherName = models.CharField(max_length = 10)
	def __unicode__(self):
		return self.className
