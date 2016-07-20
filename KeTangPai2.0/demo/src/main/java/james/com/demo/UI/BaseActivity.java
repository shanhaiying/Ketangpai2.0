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

public class BaseActivity extends Activity implements View.OnClickListener {
    public static BaseActivity BaseActivity = null;
    Button course;
    Button message;
    Button announce;
    Button about;
    TextView joinClass;
    ImageView logout;
    RequestQueue mQueue;
    AlertDialog.Builder builder;
    EditText getInviteCode;
    SharedPreferences getPersonInfo;
    BaseActivity baseActivity;
    String signal;
    private List<ClassInfo> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base);
        initWidget();
        initClassInfo();
        ClassAdapter adapter = new ClassAdapter(BaseActivity.this,R.layout.class_item,classList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ClassInfo classInfo = classList.get(position);
                Toast.makeText(BaseActivity.this,classInfo.getClassName(),Toast.LENGTH_SHORT).show();
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
    private void initClassInfo(){
        ClassInfo Android = new ClassInfo("Android","James","test123");
        classList.add(Android);
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
                    if (answer == null){
                        Toast.makeText(BaseActivity.baseActivity,"服务器错误,请稍后再试",Toast.LENGTH_SHORT).show();
                    } else if (answer.equals("success")){
                        Toast.makeText(BaseActivity.baseActivity,"课程添加成功",Toast.LENGTH_SHORT).show();
                    }else if (answer.equals("already_exist")){
                        Toast.makeText(BaseActivity.baseActivity,"您已经选了这门课程,请不要重复添加",Toast.LENGTH_SHORT).show();
                    }else if (answer.equals("not_exist")){
                        Toast.makeText(BaseActivity.baseActivity,"不存在这门课程,请检查您的邀请码",Toast.LENGTH_SHORT).show();
                    }
                }else if (msg.what == -1){//-1为特殊情况
                    Toast.makeText(BaseActivity.baseActivity,"请先输入您的学号",Toast.LENGTH_SHORT).show();
                }else if (msg.what == SymBol.RETURN_FIAL){//失败的回调发出的信息
                    Toast.makeText(BaseActivity.baseActivity,"服务器错误,请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            String stuID = getPersonInfo.getString("stuID", "error");

            @Override
            public void run() {
                CounterPick counterpick = new CounterPick(stuID,inviteCode);
                /*
                处理用户名未添加的情况
                 */
                if (stuID.equals("error")){
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
