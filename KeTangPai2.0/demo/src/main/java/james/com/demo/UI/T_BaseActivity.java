package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

import james.com.demo.Data.ClassAdapter;
import james.com.demo.Data.ClassInfo;
import james.com.demo.R;

public class T_BaseActivity extends Activity implements View.OnClickListener {
    public static T_BaseActivity BaseActivity = null;
    Button course;
    Button message;
    Button announce;
    Button about;
    TextView joinClass;
    ImageView logout;
    RequestQueue mQueue;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private List<ClassInfo> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_layout_base);
        initWidget();
        initClassInfo();
        ClassAdapter adapter = new ClassAdapter(T_BaseActivity.this,R.layout.class_item,classList);
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ClassInfo classInfo = classList.get(position);
                Toast.makeText(T_BaseActivity.this,classInfo.getClassName(),Toast.LENGTH_SHORT).show();
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
        sharedPreferences = getSharedPreferences("login_data",MODE_PRIVATE);
        editor = getSharedPreferences("login_data",MODE_PRIVATE).edit();
        course.setOnClickListener(this);
        message.setOnClickListener(this);
        announce.setOnClickListener(this);
        about.setOnClickListener(this);
        joinClass.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, ExitWindow.class);
        startActivity(intent);
    }
    /*
    管理课程信息
     */
    private void initClassInfo(){
        ClassInfo Android = new ClassInfo("Android","James");
        classList.add(Android);
        ClassInfo Python = new ClassInfo("Python","James");
        classList.add(Python);
        ClassInfo Linux = new ClassInfo("Linux","James");
        classList.add(Linux);
        ClassInfo Assembly = new ClassInfo("Assemble","James");
        classList.add(Assembly);
        ClassInfo HeadFirst = new ClassInfo("HeadFirst","James");
        classList.add(HeadFirst);
    }

    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.course:
                Intent intent0 = new Intent(this, T_BaseActivity.class);
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
                //Intent intent3 = new Intent(this,)
                break;
            case R.id.logout:
                Intent intent3 = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent3);
                break;
            case R.id.create_class:
                createClass();
        }
    }
    /*
    创建班级的基本思路
    发送的数据4项
    1.教师账号 (从本地xml文件中获取)
    2.教师姓名 (同上)
    3.课程名称
    4.课程邀请码 (自动生成 本地保存)
    其中 要手动的填写的只有 课程名称 一项
    本地一份 服务器一份
    */
    private void createClass(){

    }
}
