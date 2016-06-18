package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import james.com.demo.R;

public class LoginActivity extends Activity implements View.OnClickListener{
    Button login;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_new);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login:
                Intent intent1 = new Intent(v.getContext(),BaseActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.register:
                Intent intent2 = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
