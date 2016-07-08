package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import james.com.demo.Data.Student;
import james.com.demo.R;

public class BaseActivity extends Activity implements View.OnClickListener {
    public static BaseActivity BaseActivity = null;
    Button course;
    Button message;
    Button announce;
    Button about;
    Button testJson;
    TextView joinClass;
    ImageView logout;
    Button send;
    RequestQueue mQueue;
    Gson gson;
    String result;
    Student studentJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base);
        initWidget();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getJson();
            }
        });

        testJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gson = new Gson();
                Student student = new Student();
                student.setDate(100);
                student.setID(201501);
                student.setName("helloworld");
                result = gson.toJson(student);
                Log.d("TAG", result);

            }
        });
    }

    public void initWidget() {
        mQueue = Volley.newRequestQueue(getApplicationContext());
        BaseActivity = this;
        course = (Button) findViewById(R.id.course_base);
        message = (Button) findViewById(R.id.messages_base);
        announce = (Button) findViewById(R.id.announcement_base);
        about = (Button) findViewById(R.id.about_base);
        joinClass = (TextView) findViewById(R.id.join_class);
        logout = (ImageView) findViewById(R.id.logout);
        course.setOnClickListener(this);
        message.setOnClickListener(this);
        announce.setOnClickListener(this);
        about.setOnClickListener(this);
        joinClass.setOnClickListener(this);
        logout.setOnClickListener(this);
        send = (Button) findViewById(R.id.send);
        testJson = (Button) findViewById(R.id.json);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, ExitWindow.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.course:
                Intent intent0 = new Intent(this, BaseActivity.class);
                finish();
                startActivity(intent0);
                break;
            case R.id.messages_base:
                Intent intent = new Intent(this, MessageActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.announcement_base:
                Intent intent1 = new Intent(this, AnnounceActivity.class);
                finish();
                startActivity(intent1);
                break;
            case R.id.about_base:
                Intent intent2 = new Intent(this, AboutActivity.class);
                finish();
                startActivity(intent2);
                break;
            case R.id.join_class:
                //Intent intent3 = new Intent(this,)
                break;
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent3);
                break;
        }
    }

    private void getJson() {


        String url = "http://10.3.116.146:8000/json/";

        JsonObjectRequest jsonObjectRequest;
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject("{name:james,id:11111,date:2014}");
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
}