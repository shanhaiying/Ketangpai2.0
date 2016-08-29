package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import james.com.demo.R;

public class PersonalInfoShowActivity extends Activity{
    private Button edit;
    private TextView nickname;
    private TextView stuID;
    private TextView classBelong;
    private TextView sex;
    private TextView birthday;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_show);
        initWidget();
        showPersonal();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PersonalInfoEditActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
    /*
    个人信息直接从本地读取
     */
    private void showPersonal(){
        SharedPreferences pref = getSharedPreferences("personal_data",MODE_PRIVATE);
        nickname.setText(pref.getString("nickname", "尚未填写"));
        classBelong.setText(pref.getString("class_belong", "尚未填写"));
        sex.setText(pref.getString("sex", "尚未填写"));
        stuID.setText(pref.getString("stuID", "尚未填写"));
        birthday.setText(pref.getString("birthday", "尚未填写"));
    }
    private void initWidget(){
        edit = (Button)findViewById(R.id.edit_profile);
        edit.getBackground().setAlpha(0);
        nickname = (TextView)findViewById(R.id.nickname);
        classBelong = (TextView)findViewById(R.id.class_belong);
        sex= (TextView)findViewById(R.id.my_sex);
        birthday = (TextView)findViewById(R.id.birthday);
        stuID = (TextView)findViewById(R.id.stu_ID);
    }
}
