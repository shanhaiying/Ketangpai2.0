package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import james.com.demo.R;

public class PersonalInfoEditActivity extends Activity {
    Button save;
    EditText nickname;//nickname
    EditText phone_number;//phone_number
    EditText mail;//mail
    EditText address;//address
    EditText sex;
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
        editor.putString("nickname",nickname.getText().toString());
        editor.putString("phone_number",phone_number.getText().toString());
        editor.putString("mail",mail.getText().toString());
        editor.putString("address",address.getText().toString());
        editor.putString("sex", sex.getText().toString());
        editor.apply();
    }
    protected void showPersonal(){//读取文件中的个人信息
        SharedPreferences pref = getSharedPreferences("personal_data", MODE_PRIVATE);
        nickname.setText(pref.getString("nickname", "尚未填写"));
        phone_number.setText(pref.getString("phone_number", "尚未填写"));
        mail.setText(pref.getString("mail", "尚未填写"));
        address.setText(pref.getString("address", "尚未填写"));
        sex.setText(pref.getString("sex", "尚未填写"));
    }

}
