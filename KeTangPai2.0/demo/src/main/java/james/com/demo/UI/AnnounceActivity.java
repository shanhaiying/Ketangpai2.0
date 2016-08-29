package james.com.demo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import james.com.demo.R;

public class AnnounceActivity extends Activity implements View.OnClickListener{
    public static AnnounceActivity AnnounceActivity = null;
    private Button course;
    private Button message;
    private Button announce;
    private Button about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_announcement);
        initWidget();
    }
    public void initWidget(){
        AnnounceActivity = this;
        course = (Button)findViewById(R.id.course);
        message = (Button)findViewById(R.id.messages);
        announce = (Button)findViewById(R.id.announcement);
        about = (Button)findViewById(R.id.about);
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
                Intent intent = new Intent(this,MessageActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.announcement:
                Intent intent1 = new Intent(this,AnnounceActivity.class);
                finish();
                startActivity(intent1);
                break;
            case R.id.about:
                Intent intent2 = new Intent(this,AboutActivity.class);
                finish();
                startActivity(intent2);
                break;
            case R.id.join_class:
                //Intent intent3 = new Intent(this,)
                break;
            case R.id.logout:
                Intent intent3 = new Intent(this,LoginActivity.class);
                finish();
                startActivity(intent3);
                break;
        }
    }
}