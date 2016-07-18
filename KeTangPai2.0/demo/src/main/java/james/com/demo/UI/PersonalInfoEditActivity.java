package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import james.com.demo.Data.Profile;
import james.com.demo.Data.SymBol;
import james.com.demo.Data.URL;
import james.com.demo.R;
import james.com.demo.Util.Utils;

public class PersonalInfoEditActivity extends Activity {
    Button save;
    EditText nickname;//nickname
    EditText stuID;//phone_number
    EditText sex;//mail
    EditText birthday;//address
    EditText classBelong;
    RequestQueue mQueue;
    SharedPreferences getAccount;
    String signal;
    public static PersonalInfoEditActivity personalInfoEditActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        initWidget();
        showPersonal();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPersonal();
            }
        });
    }
    protected void setPersonal() {//设置个人信息
        SharedPreferences.Editor editor = getSharedPreferences("personal_data", MODE_PRIVATE).edit();
        editor.putString("nickname", nickname.getText().toString());
        editor.putString("stuID", stuID.getText().toString());
        editor.putString("birthday", birthday.getText().toString());
        editor.putString("class_belong", classBelong.getText().toString());
        editor.putString("sex", sex.getText().toString());
        editor.apply();
        final Profile profile = new Profile();
        profile.setUsername(getAccount.getString("username", "error"));
        profile.setSex(sex.getText().toString());
        profile.setStuId(stuID.getText().toString());
        profile.setClassBelong(classBelong.getText().toString());
        profile.setBirthday(birthday.getText().toString());
        profile.setStuName(nickname.getText().toString());
        if (!Utils.isNetworkAvailable(personalInfoEditActivity)){
            Toast.makeText(personalInfoEditActivity, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                String answer = null;
                if (msg.what == SymBol.RETURN_SUCCESS)
                {
                    Bundle bundle = msg.getData();
                    answer = bundle.getString("result");
                }else if (msg.what == SymBol.RETURN_FIAL){
                    Toast.makeText(personalInfoEditActivity,"服务器端错误,请稍后再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (answer == null){
                    Toast.makeText(personalInfoEditActivity, "未知错误", Toast.LENGTH_SHORT).show();
                }else if (answer.equals("success")){
                    Toast.makeText(personalInfoEditActivity,"资料修改成功!!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(personalInfoEditActivity,PersonalInfoShowActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                mQueue = Volley.newRequestQueue(PersonalInfoEditActivity.personalInfoEditActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(profile.toString());
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
//发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, URL.URL_SAVE_PROFILE, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //发送jsonObject 并在返回成功的回调里处理结果
                                    try
                                    {
                                        signal = response.getString("result");
                                        Log.d("Response_Message", response.toString());
                                        Log.d("Extract_result", signal);
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
                            Log.d("Failure_Message", arg0.toString());
                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("result","server_error");
                            message.what = SymBol.RETURN_FIAL;
                            handler.sendMessage(message);
                        }
                    });
                    mQueue.add(jsonRequest);
                    Log.d("The_Whole_JsonRequest", jsonRequest.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                mQueue.start();
            }
        }).start();
    }
    protected void showPersonal(){//读取文件中的个人信息
        SharedPreferences pref = getSharedPreferences("personal_data", MODE_PRIVATE);
        nickname.setText(pref.getString("nickname", "尚未填写"));
        classBelong.setText(pref.getString("class_belong", "尚未填写"));
        stuID.setText(pref.getString("stuID", "尚未填写"));
        birthday.setText(pref.getString("birthday", "尚未填写"));
        sex.setText(pref.getString("sex", "尚未填写"));
    }
    private void initWidget(){
        save = (Button)findViewById(R.id.save_profile);
        nickname = (EditText)findViewById(R.id.nickname);
        classBelong = (EditText)findViewById(R.id.class_belong_edit);
        stuID = (EditText)findViewById(R.id.stu_ID_edit);
        birthday = (EditText)findViewById(R.id.birthday_edit);
        sex = (EditText)findViewById(R.id.my_sex_edit);
        personalInfoEditActivity = PersonalInfoEditActivity.this;
        getAccount = getSharedPreferences("login_data",MODE_PRIVATE);
        mQueue = Volley.newRequestQueue(PersonalInfoEditActivity.personalInfoEditActivity);
    }

}
