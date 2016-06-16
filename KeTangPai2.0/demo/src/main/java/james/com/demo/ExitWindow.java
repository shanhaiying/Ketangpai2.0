package james.com.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ExitWindow extends Activity {
        private LinearLayout layout;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.window_exit);
            layout = (LinearLayout) findViewById(R.id.exit_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {//点击外部则关闭跳出的选择框
            finish();
            return true;
        }

        public void exitbutton1(View v) {//选择否则只关闭当前选择框
            this.finish();
        }

        public void exitbutton0(View v) {
            this.finish();//关闭弹框
            BaseActivity.BaseActivity.finish();//关闭MainActivity
        }
}
