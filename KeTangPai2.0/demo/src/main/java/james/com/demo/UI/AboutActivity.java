package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

import james.com.demo.R;

public class AboutActivity extends Activity implements View.OnClickListener{
    public static AboutActivity AboutActivity = null;
    Button course;
    Button message;
    Button announce;
    Button about;
    TableRow personalInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        initWidget();
    }
    public void initWidget(){
        AboutActivity = this;
        course = (Button)findViewById(R.id.course);
        message = (Button)findViewById(R.id.messages);
        announce = (Button)findViewById(R.id.announcement);
        about = (Button)findViewById(R.id.about);
        personalInfo = (TableRow)findViewById(R.id.personInfo);
        personalInfo.setOnClickListener(this);
        course.setOnClickListener(this);
        message.setOnClickListener(this);
        announce.setOnClickListener(this);
        about.setOnClickListener(this);
    }
    public void onBackPressed(){
        Intent intent = new Intent(this,ExitWindow.class);
        startActivity(intent);
    }
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.course:
                Intent intent0 = new Intent(this,BaseActivity.class);
                finish();
                startActivity(intent0);
                break;
            case R.id.messages:
                Intent intent = new Intent(v.getContext(),MessageActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.announcement:
                Intent intent1 = new Intent(v.getContext(),AnnounceActivity.class);
                finish();
                startActivity(intent1);
                break;
            case R.id.about:
                Intent intent2 = new Intent(v.getContext(),AboutActivity.class);
                finish();
                startActivity(intent2);
                break;
            case R.id.personInfo:
                Intent intent3 = new Intent(v.getContext(),PersonalInfoShowActivity.class);
                startActivity(intent3);
        }
    }
}