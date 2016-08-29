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

import james.com.demo.Data.*;
import james.com.demo.R;
import james.com.demo.Util.Utils;

public class BaseActivity extends Activity implements View.OnClickListener {
    public static BaseActivity BaseActivity = null;
    private Button course;
    private Button message;
    private Button announce;
    private Button about;
    private TextView joinClass;
    private TextView ifNoCourse;//这个控件是如果没有选课 主页上就显示这个
    private ImageView logout;
    private RequestQueue mQueue;
    private AlertDialog.Builder builder;
    private EditText getInviteCode;
    private SharedPreferences getPersonInfo;
    private SharedPreferences.Editor saveCourseInfo;
    private SharedPreferences getCourseInfo;
    private BaseActivity baseActivity;
    private String signal;
    private List<ClassInfo> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base);
        initWidget();
        getCourse();
        ClassAdapter adapter = new ClassAdapter(BaseActivity.this, R.layout.class_item, classList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ClassInfo classInfo = classList.get(position);
                Toast.makeText(BaseActivity.this, classInfo.getClassName(), Toast.LENGTH_SHORT).show();
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
        ifNoCourse = (TextView)findViewById(R.id.if_no_course);
        course.setOnClickListener(this);
        message.setOnClickListener(this);
        announce.setOnClickListener(this);
        about.setOnClickListener(this);
        joinClass.setOnClickListener(this);
        logout.setOnClickListener(this);
        saveCourseInfo = getSharedPreferences("course_data",MODE_PRIVATE).edit();
        getCourseInfo = getSharedPreferences("course_data",MODE_PRIVATE);
        getPersonInfo = getSharedPreferences("personal_data", MODE_PRIVATE);
        baseActivity = BaseActivity.this;
        builder = new AlertDialog.Builder(this);
        getInviteCode = new EditText(this);
        builder.setTitle("请输入课程邀请码");
        builder.setIcon(android.R.drawable.ic_dialog_info);
        /*
        @Todo 不知道怎么把Dialog给dismiss掉
         */
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                joinClass(getInviteCode.getText().toString());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setView(getInviteCode);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, ExitWindow.class);
        startActivity(intent);
    }

    /*
    管理课程信息
     */
    private void initClassInfo(JSONObject response) {
        if (Utils.isNetworkAvailable(baseActivity)){//网络可用时则从服务器读取数据
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
                        ifNoCourse.setText("您尚未选择任何课程");
                    }
                } else if (msg.what == -1)
                {//-1为特殊情况 没填学号
                    Toast.makeText(BaseActivity.baseActivity, "请先输入您的学号", Toast.LENGTH_SHORT).show();
                } else if (msg.what == SymBol.RETURN_FIAL)
                {//失败的回调发出的信息
                    Toast.makeText(BaseActivity.baseActivity, "课程列表读取错误,请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            String stuID = getPersonInfo.getString("stuID", "error");

            @Override
            public void run() {
                /*
                处理学号未添加的情况
                此时肯定查不到数据 并不会有bug产生
                但是为了提醒用户 必须加上这段代码
                 */
                if (stuID.equals("error"))
                {
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                    return;
                }
                mQueue = Volley.newRequestQueue(baseActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject("{stuID:" + stuID + "}");//请求只发送一个学号就行
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, URL.URL_GET_COURSE, jsonObject,
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
                builder.show();
                break;
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent3);
                break;
        }
    }

    /*
    加入班级的基本思路
    发送的数据2项
    1.学生学号(自动读取)
    2.课程邀请码 (通过老师告诉学生)
    其中 要手动的填写的只有 课程邀请码 一项
    */
    private void joinClass(final String inviteCode) {
        final Handler handler = new Handler() {
            String answer = null;

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == SymBol.RETURN_SUCCESS)
                {
                    Bundle bundle = msg.getData();
                    answer = bundle.getString("result");
                    if (answer == null)
                    {
                        Toast.makeText(BaseActivity.baseActivity, "服务器错误,请稍后再试", Toast.LENGTH_SHORT).show();
                    } else if (answer.equals("success"))
                    {
                        Toast.makeText(BaseActivity.baseActivity, "课程添加成功", Toast.LENGTH_SHORT).show();
                    } else if (answer.equals("already_exist"))
                    {
                        Toast.makeText(BaseActivity.baseActivity, "您已经选了这门课程,请不要重复添加", Toast.LENGTH_SHORT).show();
                    } else if (answer.equals("not_exist"))
                    {
                        Toast.makeText(BaseActivity.baseActivity, "不存在这门课程,请检查您的邀请码", Toast.LENGTH_SHORT).show();
                    }
                } else if (msg.what == -1)
                {//-1为特殊情况 没填学号
                    Toast.makeText(BaseActivity.baseActivity, "请先输入您的学号", Toast.LENGTH_SHORT).show();
                } else if (msg.what == SymBol.RETURN_FIAL)
                {//失败的回调发出的信息
                    Toast.makeText(BaseActivity.baseActivity, "服务器错误,请稍后再试", Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            String stuID = getPersonInfo.getString("stuID", "error");

            @Override
            public void run() {
                CounterPick counterpick = new CounterPick(stuID, inviteCode);
                /*
                处理用户名未添加的情况
                 */
                if (stuID.equals("error"))
                {
                    Message message = new Message();
                    message.what = -1;//特殊情况
                    handler.sendMessage(message);
                    return;
                }
                mQueue = Volley.newRequestQueue(baseActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(counterpick.toString());
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, URL.URL_PICK_COURSE, jsonObject,
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
}
