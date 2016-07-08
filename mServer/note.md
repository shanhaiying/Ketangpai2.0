# 工作流程
setting.py中指定文件所在目录 BASE\_DIR+templates
urls.py通过URL的具体路径,调用具体函数
函数定义在view.py中
最后找到templates目录下的html文件
html文件中有许多未定义的值 用双大括号包裹 {{example}}
可以通过view.py中的函数传键值对过去
在函数中，往往有键值对构成的字典作为参数传给html文件

表名组成结构为：app名_类名（如：TestModel_test）。

HttpResponse一般只接受一个参数，比如：
return HttpResponse("HelloWorld")

Render一般返回两个或三个参数，比如：
return render(request,"hello.html")
或者
return render(request,"hello.html",context)
context是一个字典对象，其代码可以写为：
```
context = {}
context['hello'] = "HelloWorld"
```
此时，系统会根据这个字典对象将模板html中的{{hello}}自动替换为HelloWorld

如何将JSON解析为字符串：
若有以下json数据：data = {"name":"james","age":"12"}
可以通过导入from django.http import JsonResponse
然后response = JsonResponse(data)
response.content中的内容就是JSON转化而来的字符串

如何将字符串转换为字典：
ret_dict = eval(response.content)
然后ret_dict就变成了字典，可以通过键值对进行操作

也可以使用simplejson 
```
sudo pip install simplejson
json到字典：
ret_dict = simplejson.loads(data)
字典到json:
str = simplejson.dumps(ret_dict)
```

如何取得数据包中的json数据
注意request.raw_post_data在django1.4被废弃，现在应该使用request.body
android代码：
private void getJson() {


        String url = "http://10.3.116.146:8000/json/";

        JsonObjectRequest jsonObjectRequest;
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject("{name:james,id:11111,data:2014}");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
//打印前台向后台要提交的post数据
        Log.d("post", jsonObject != null ? jsonObject.toString() : null);

//发送post请求
        try
        {
            jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //打印请求后获取的json数据
                            Log.d("bbb", response.toString());

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError arg0) {
                    Log.d("aaa", arg0.toString());
                }
            });
            mQueue.add(jsonObjectRequest);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e + "");
        }
        mQueue.start();
    }
	
django端代码：
def json(request):
    if request.method == 'POST':
    	data = {"name":"james","age":"12"}
        received_json_data = simplejson.dumps(request.body)
        return HttpResponse(request.body)
此时便可以读出返回的json数据