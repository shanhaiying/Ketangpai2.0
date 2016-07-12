from django.shortcuts import render

# Create your views here.


from django.http import HttpResponse
from django.db.models import *

#!/usr/bin/env python
# coding=utf-8

# db operation
def testdb(request):
	test1 = test(name='w3cschool.cc')
	test1.save()
	return HttpResponse("<p>Data added</p>")
	
	
def staff(request):
	staff_list = test.objects.all()
	staff_str = map(str,staff_list)
	return HttpResponse("<p>"+' '.join(staff_str)+"</p>")
	
	
	
	
	
	
	
	
	
	
	
	
