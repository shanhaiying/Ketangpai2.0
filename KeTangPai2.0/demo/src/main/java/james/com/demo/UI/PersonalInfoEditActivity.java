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
import james.com.demo.Util.MD5;
import james.com.demo.Util.Utils;

public class PersonalInfoEditActivity extends Activity {
    Button save;
    EditText nickname;//nickname
    EditText phone_number;//phone_number
    EditText mail;//mail
    EditText address;//address
    EditText sex;
    RequestQueue mQueue;
    SharedPreferences getAccount;
    public static PersonalInfoEditActivity personalInfoEditActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        save = (Button)findViewById(R.id.save_profile);
        nickname = (EditText)findViewById(R.id.nickname);
        phone_number = (EditText)findViewById(R.id.phone_number);
        mail = (EditText)findViewById(R.id.mail);
        address = (EditText)findViewById(R.id.address);
        sex = (EditText)findViewById(R.id.my_sex);
        personalInfoEditActivity = PersonalInfoEditActivity.this;
        getAccount = getSharedPreferences("login_data",MODE_PRIVATE);
        mQueue = Volley.newRequestQueue(PersonalInfoEditActivity.personalInfoEditActivity);
        showPersonal();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPersonal();
                Intent intent = new Intent(view.getContext(),PersonalInfoShowActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
    protected void setPersonal(){//设置个人信息
        SharedPreferences.Editor editor = getSharedPreferences("personal_data",MODE_PRIVATE).edit();
        editor.putString("teacherName", nickname.getText().toString());
        editor.putString("phone_number",phone_number.getText().toString());
        editor.putString("mail",mail.getText().toString());
        editor.putString("address",address.getText().toString());
        editor.putString("sex", sex.getText().toString());
        editor.apply();
        Profile profile = new Profile();
        profile.setUsername(getAccount.getString("username","error"));
        profile.setSex(sex.getText().toString());
        //profile.setStuId();
        profile.set



                //Todo~~~
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
                }
                if (answer == null){
                    Toast.makeText(personalInfoEditActivity, "未知错误", Toast.LENGTH_SHORT).show();
                }else if (answer.equals("success")){
                    Toast.makeText(personalInfoEditActivity,"注册成功!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(personalInfoEditActivity,PersonalInfoShowActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                mQueue = Volley.newRequestQueue(RegisterActivity.registerActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject("{username:" + username + ",password:" + encryptPassword + "}");
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
//发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, URL.URL_REGISTER_STUDENT, jsonObject,
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
                                        msg.what = SUCCESS_SYMBOL;
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
        nickname.setText(pref.getString("teacherName", "尚未填写"));
        phone_number.setText(pref.getString("phone_number", "尚未填写"));
        mail.setText(pref.getString("mail", "尚未填写"));
        address.setText(pref.getString("address", "尚未填写"));
        sex.setText(pref.getString("sex", "尚未填写"));
    }

}
