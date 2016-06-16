package james.com.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageActivity extends Activity implements View.OnClickListener{
    Button course;
    Button message;
    Button announce;
    Button about;
    TextView joinClass;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message);
        course = (Button)findViewById(R.id.course);
        message = (Button)findViewById(R.id.messages);
        announce = (Button)findViewById(R.id.announcement);
        about = (Button)findViewById(R.id.about);
        joinClass = (TextView)findViewById(R.id.join_class);
        logout = (ImageView)findViewById(R.id.logout);
        course.setOnClickListener(this);
        message.setOnClickListener(this);
        announce.setOnClickListener(this);
        about.setOnClickListener(this);
        joinClass.setOnClickListener(this);
        logout.setOnClickListener(this);
    }
    public void onBackPressed(){
        Intent intent = new Intent(this,ExitWindow.class);
        startActivity(intent);
    }
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.course:break;
            case R.id.messages:
                Intent intent = new Intent(this,MessageActivity.class);
                startActivity(intent);
                break;
            case R.id.announcement:
                Intent intent1 = new Intent(this,AnnounceActivity.class);
                startActivity(intent1);
                break;
            case R.id.about:
                Intent intent2 = new Intent(this,AboutActivity.class);
                startActivity(intent2);
                break;
            case R.id.join_class:
                //Intent intent3 = new Intent(this,)
                break;
            case R.id.logout:
                Intent intent3 = new Intent(this,LoginActivity.class);
                startActivity(intent3);
                break;
        }
    }
}