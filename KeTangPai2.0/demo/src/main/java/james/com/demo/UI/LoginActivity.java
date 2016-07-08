package james.com.demo.UI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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

import james.com.demo.R;
import james.com.demo.Util.Utils;

public class LoginActivity extends Activity implements View.OnClickListener{
    Button login;
    Button register;
    String password;
    String username;
    EditText mPassword;
    EditText mUsername;
    RequestQueue mQueue;
    private final int RETURN_SYMBOL = 1;
    private String signal = "result";//存储服务器端返回的结果
    public static LoginActivity loginActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_new);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        mUsername = (EditText)findViewById(R.id.username_edit);
        mPassword = (EditText)findViewById(R.id.password_edit);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login:
                Loginresult();
                break;
            case R.id.register:
                Intent intent2 = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void Loginresult(){
        loginActivity = this;
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        /*
        1代表成功
        -1代表账号不存在
        -2代表密码错误
        -3代表连接问题
        -4代表未知错误
         */
        Log.d("TAG", password);
        if (!Utils.isNetworkAvailable(loginActivity)){
            Toast.makeText(loginActivity,"网络不可用",Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        checkPassword();
    }
    public void checkPassword() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String answer = null;
                if (msg.what == RETURN_SYMBOL)
                {
                    Bundle bundle = msg.getData();
                    answer = bundle.getString("result");
                }
                if (answer == null){
                    Toast.makeText(loginActivity,"网络问题",Toast.LENGTH_SHORT).show();
                }else if (answer.equals("success")){
                    Toast.makeText(loginActivity,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(loginActivity,BaseActivity.class);
                    startActivity(intent);
                    finish();
                }else if (answer.equals("not_exist")){
                    Toast.makeText(loginActivity,"账号不存在",Toast.LENGTH_SHORT).show();
                    mUsername.setText("");
                    mPassword.setText("");
                }else if (answer.equals("error")){
                    Toast.makeText(loginActivity,"密码错误",Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                }else if (answer.equals("result")){
                    Toast.makeText(loginActivity,"服务器问题,请稍后再试",Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://10.3.116.146:8000/check/";
                mQueue = Volley.newRequestQueue(LoginActivity.loginActivity);
                JsonObjectRequest jsonRequest;
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject("{username:" + username + ",password:" + password + "}");
                    Log.d("Sending_Message", jsonObject.toString());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
//发送post请求
                try
                {
                    jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, url, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //打印请求后获取的json数据
                                    try
                                    {
                                        signal = response.getString("result");
                                        Log.d("Response_Message", response.toString());
                                        Log.d("Extract_result", signal);
                                        Message msg = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("result", signal);
                                        msg.setData(bundle);
                                        msg.what = RETURN_SYMBOL;
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
}