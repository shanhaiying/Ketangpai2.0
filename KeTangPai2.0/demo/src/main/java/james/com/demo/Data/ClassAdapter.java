package james.com.demo.Data;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import james.com.demo.R;

public class ClassAdapter extends ArrayAdapter<ClassInfo> {
    private int resourceID;
    public ClassAdapter(Context context,int textViewResourceID,List<ClassInfo> objects){
        super(context,textViewResourceID,objects);
        resourceID = textViewResourceID;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        ClassInfo tempclass = getItem(position);
        View view;
        ViewHolder viewHolder;
        Resources r = this.getContext().getResources();
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,null);
            viewHolder = new ViewHolder();
            viewHolder.className = (TextView)view.findViewById(R.id.class_name);
            viewHolder.teacherName = (TextView)view.findViewById(R.id.teacher_name);
            /*
            班级的背景图片是随机的
             */
            String colorset[] = {"color_brown","color_green","color_points","color_purple","color_wood"};
            Random random = new Random();
            String certainColor = colorset[random.nextInt(4)];
            switch (random.nextInt(5)){
            //switch (3){
                case 0:
                    viewHolder.className.setBackground(r.getDrawable(R.drawable.color_cloth));
                    //viewHolder.teacherName.setBackground(r.getDrawable(R.drawable.color_brown));
                    break;
                case 1:
                    viewHolder.className.setBackground(r.getDrawable(R.drawable.color_fade));
                    //viewHolder.teacherName.setBackground(r.getDrawable(R.drawable.color_green));
                    break;
                case 2:
                    viewHolder.className.setBackground(r.getDrawable(R.drawable.color_points));
                    //viewHolder.teacherName.setBackground(r.getDrawable(R.drawable.color_points));
                    break;
                case 3:
                    viewHolder.className.setBackground(r.getDrawable(R.drawable.color_white));
                    //viewHolder.teacherName.setBackground(r.getDrawable(R.drawable.color_purple));
                    break;
                case 4:
                    viewHolder.className.setBackground(r.getDrawable(R.drawable.color_wood));
                    //viewHolder.teacherName.setBackground(r.getDrawable(R.drawable.color_wood));
                    break;
                case 5:
                    viewHolder.className.setBackground(r.getDrawable(R.drawable.color_blue));
                    //viewHolder.teacherName.setBackground(r.getDrawable(R.drawable.color_wood));
                    break;
            }
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.className.setText(tempclass.getClassName());
        viewHolder.teacherName.setText(tempclass.getTeacherName());
        return view;
    }
    class ViewHolder
    {
        TextView className;
        TextView teacherName;
    }
}
