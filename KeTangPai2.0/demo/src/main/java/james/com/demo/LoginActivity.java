package james.com.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity implements View.OnClickListener{
    Button login;
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        //MyOnClickListener myOnClickListener = new MyOnClickListener();
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login:
                Intent intent1 = new Intent(v.getContext(),BaseActivity.class);
                startActivity(intent1);
                //finish();
                break;
            case R.id.register:
                Intent intent2 = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(intent2);
                //finish();
                break;
        }
    }
    /*
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.login:
                    Intent intent = new Intent(v.getContext(),BaseActivity.class);
                    startActivity(intent);
                case R.id.register:
                    Intent intent = new Intent(v.getContext(),RegisterActivity.class);
                    startActivity(intent);

            }
        }
    }
    */
}
