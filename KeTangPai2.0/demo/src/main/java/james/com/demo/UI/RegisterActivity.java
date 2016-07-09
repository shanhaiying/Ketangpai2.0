package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import james.com.demo.R;
import james.com.demo.Util.Utils;

public class RegisterActivity extends Activity{
    Button register;
    EditText mUsername;
    EditText mPassword;
    EditText mConfirm;
    RequestQueue mQueue;
    String username;
    String password;
    String confirm;
    final int SUCCESS_SYMBOL = 1;
    public static RegisterActivity registerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        initWidget();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),BaseActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initWidget(){
        register = (Button)findViewById(R.id.register);
        mUsername = (EditText)findViewById(R.id.username_edit);
        mPassword = (EditText)findViewById(R.id.password_edit);
        mConfirm = (EditText)findViewById(R.id.confirmPassword);
        registerActivity = this;
        mQueue = Volley.newRequestQueue(RegisterActivity.registerActivity);
    }
    private void tryRegister(){
        if (!Utils.isNetworkAvailable(registerActivity)){
            Toast.makeText(registerActivity, "网络不可用", Toast.LENGTH_SHORT).show();
        }
        String REGISTER_URL = "";
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        confirm = mConfirm.getText().toString();
        if (!password.equals(confirm)){
            Toast.makeText(registerActivity,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
        }
        final Handler handler = new Handler(){
            @Override
        public void handleMessage(Message msg){
                super.handleMessage(msg);
                String answer = null;
                if (msg.what == SUCCESS_SYMBOL)
                {
                    Bundle bundle = msg.getData();
                    answer = bundle.getString("result");
                }
            }
        };
    }
}
