package james.com.demo.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import james.com.demo.Data.ClassAdapter;
import james.com.demo.Data.ClassInfo;
import james.com.demo.Data.SymBol;
import james.com.demo.Data.URL;
import james.com.demo.R;
import james.com.demo.Util.Utils;

public class T_BaseActivity extends Activity implements View.OnClickListener {
    public static T_BaseActivity BaseActivity = null;
    Button course;
    Button message;
    Button announce;
    Button about;
    TextView joinClass;
    ImageView logout;
    RequestQueue mQueue;
    SharedPreferences getLoginData;
    SharedPreferences.Editor editClassInfo;
    SharedPreferences getPersonalInfo;
    SharedPreferences.Editor saveCourseInfo;
    SharedPreferences getCourseInfo;
    AlertDialog.Builder builder;
    EditText getClassName;
    TextView ifNoCourse;
    String signal;
    public static T_BaseActivity t_baseActivity = null;
    private List<ClassInfo> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_layout_base);
        initWidget();
        getCourse();
        ClassAdapter adapter = new ClassAdapter(T_BaseActivity.this, R.layout.class_item, classList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ClassInfo classInfo = classList.get(position);
                Toast.makeText(T_BaseActivity.this, classInfo.getClassName(), Toast.LENGTH_SHORT).show();
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
        joinClass = (TextView) findViewById(R.id.create_class);
        logout = (ImageView) findViewById(R.id.logout);
        ifNoCourse = (TextView)findViewById(R.id.if_no_course);
        saveCourseInfo = getSharedPreferences("course_data",MODE_PRIVATE).edit();
        getCourseInfo = getSharedPreferences("course_data",MODE_PRIVATE);
        getLoginData = getSharedPreferences("login_data", MODE_PRIVATE);
        editClassInfo = getSharedPreferences("login_data", MODE_PRIVATE).edit();
        getPersonalInfo = getSharedPreferences("personal_data",MODE_PRIVATE);
        course.setOnClickListener(this);
        message.setOnClickListener(this);
        announce.setOnClickListener(this);
        about.setOnClickListener(this);
        joinClass.setOnClickListener(this);
        logout.setOnClickListener(this);
        t_baseActivity = T_BaseActivity.this;
        builder = new AlertDialog.Builder(this);
        getClassName = new EditText(this);
        builder.setTitle("请输入课程名称");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        /*
        @Todo 不知道怎么把Dialog给dismiss掉
         */
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createClass(getClassName.getText().toString());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setView(getClassName);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, ExitWindow.class);
        startActivity(intent);
    }

    /*
    管理课程信息
     */
    private void initClassInfo(JSONObject response) {
        if (Utils.isNetworkAvailable(t_baseActivity)){//网络可用时则从服务器读取数据
            try
            {
                int sum = Integer.parseInt(response.getString("sum"));
                Log.d("Course_Sum","The sum is " + sum);
                saveCourseInfo.putInt("sum",sum);
                for (int i = 0; i < sum; i++)
                {
                    saveCourseInfo.putString("course" + i,response.getString("course" + i));
                    saveCourseInfo.putString("teacher" + i,response.getString("teacher" + i));//将选课数据存储到本地
                    ClassInfo classInfo = new ClassInfo(response.getString("course" + i),response.getString("teacher" + i));
                    Log.d("ClassInfo",classInfo.toString());
                    classList.add(classInfo);
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }else {//若没有网络则直接从本地读
            int sum = Integer.parseInt(getCourseInfo.getString("sum","0"));
            if (sum == 0){
                return;
            }
            for (int i = 0; i < sum; i++){
                ClassInfo classInfo = new ClassInfo(getCourseInfo.getString("course" + i,"error"),getCourseInfo.getString("teacher" + i,"error"));
                classList.add(classInfo);
            }
        }
    }
    /*
    启动时读取已选课程列表 优先从服务器同步到本地再读取 否则直接读取本地
    基本方法 先读Json中的result 若为not_exist 则直接返回
    若为success 则根据Json中的sum选项读取出课程总数
    然后遍历之
     */
    private void getCourse() {
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String answer;
                Bundle bundle = msg.getData();
                answer = bundle.getString("result");
                if (msg.what == SymBol.RETURN_SUCCESS){//成功的回调
                    if (answer == null){
                        Log.v("helloWorld","helloWorld");
                    }
                    else if (answer.equals("not_exist")){//如果没选课
                        ifNoCourse.setText("您创建选择任何课程");
                    }
                }else if (msg.what == SymBol.RETURN_FIAL)
                {//失败的回调发出的信息
                    Toast.makeText(T_BaseActivity.t_baseActivity, "课程列表读取错误,请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            String username = getLoginData.getString("username", "error");

            @Override
            public void run() {
                mQueue = Volley.newRequestQueue(t_baseActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject("{username:" + username + "}");//请求只发送一个账号就行
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, URL.URL_GET_COURSE_TEACHER, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //发送jsonObject 并在返回成功的回调里处理结果
                                    try
                                    {
                                        signal = response.getString("result");
                                        Log.d("Response_Message", response.toString());
                                        if (signal.equals("not_exist")){
                                            Message msg = new Message();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("result", signal);
                                            msg.setData(bundle);
                                            msg.what = SymBol.RETURN_SUCCESS;
                                            handler.sendMessage(msg);
                                        }else if (signal.equals("success")){//确实选了课了
                                            initClassInfo(response);
                                        }

                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError arg0) {
                            Message msg = new Message();
                            msg.what = SymBol.RETURN_FIAL;
                            handler.sendMessage(msg);
                            Log.d("Failure_Message", arg0.toString());
                        }
                    });
                    mQueue.add(jsonRequest);
                    Log.d("The_Whole_JsonRequest", jsonRequest.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*
    创建班级的基本思路
    发送的数据4项
    1.教师账号 (从本地xml文件中获取)
    2.教师姓名 (同上)
    3.课程名称
    4.课程邀请码 (自动生成 本地保存)
    其中 要手动的填写的只有 课程名称 一项
    本地一份 服务器一份
    */
    private void createClass(final String className) {
        final Handler handler = new Handler() {
            String answer = null;
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SymBol.RETURN_SUCCESS)
                {
                    Bundle bundle = msg.getData();
                    answer = bundle.getString("result");
                    if (answer == null){
                        Toast.makeText(T_BaseActivity.t_baseActivity,"服务器错误,请稍后再试",Toast.LENGTH_SHORT).show();
                    } else if (answer.equals("success")){
                        Toast.makeText(T_BaseActivity.t_baseActivity,"课程添加成功",Toast.LENGTH_SHORT).show();
                    }else if (answer.equals("already_exist")){
                        Toast.makeText(T_BaseActivity.t_baseActivity,"该课程已存在!",Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == SymBol.RETURN_FIAL){
                    Toast.makeText(T_BaseActivity.t_baseActivity,"请先输入您的姓名",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(T_BaseActivity.t_baseActivity,"服务器错误,请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            String username = getLoginData.getString("username", "error");
            String teacherName = getPersonalInfo.getString("teacherName", "error");

            @Override
            public void run() {
                ClassInfo classInfo = new ClassInfo(className,teacherName,username);
                classInfo.setInviteCode();
                /*
                处理用户名未添加的情况
                 */
                if (teacherName.equals("error")){
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                    return;
                }
                mQueue = Volley.newRequestQueue(t_baseActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(classInfo.toString());
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, URL.URL_CREATE_CLASS, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //发送jsonObject 并在返回成功的回调里处理结果
                                    try
                                    {
                                        signal = response.getString("result");
                                        Log.d("Response_Message", response.toString());
                                        Message msg = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("result", signal);
                                        msg.setData(bundle);
                                        msg.what = SymBol.RETURN_SUCCESS;
                                        handler.sendMessage(msg);
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError arg0) {
                            Message msg = new Message();
                            msg.what = SymBol.RETURN_FIAL;
                            handler.sendMessage(msg);
                            Log.d("Failure_Message", arg0.toString());
                        }
                    });
                    mQueue.add(jsonRequest);
                    Log.d("The_Whole_JsonRequest", jsonRequest.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.course:
                Intent intent0 = new Intent(this, T_BaseActivity.class);
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
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent3);
                break;
            case R.id.create_class://创建新班级
                builder.show();
        }
    }
}
