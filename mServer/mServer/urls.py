"""mServer URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.9/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Import the include() function: from django.conf.urls import url, include
    3. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""
from django.conf.urls import *
from django.contrib import admin
from mServer.view import hello
from mServer.view import test
from mServer.view import testdb
from testCon.views import staff
from . import view

urlpatterns = [
    url(r'^hello/$',view.hello,name = 'hello'),
	url(r'^testdb/$',view.testdb,name = 'testdb'),
	url(r'^admin/', include(admin.site.urls)),
	url(r'^fff/$',view.fff,name = 'fff'),
	url(r'^testCon/',include('testCon.urls')),
        url(r'^json/',view.json,name = 'json'),
	url(r'^check/',view.check,name = 'check'),
]









