package james.com.demo.Data;

import java.util.Random;

import james.com.demo.Util.MD5;

/*
课程信息类
 */
public class ClassInfo {
    private String className;
    private String teacherName;
    private String username;
    private String inviteCode;
    public ClassInfo(String mClassName,String mTeacherName){//注意 该构造方法是主页读取课程信息时使用了 无需用户名信息
        className = mClassName;
        teacherName = mTeacherName;
    }
    public ClassInfo(String mClassName, String mTeacherName,String mUsername){//注意 该构造方法是创建课程时发包使用的
        className = mClassName;
        teacherName = mTeacherName;
        username = mUsername;
    }
    public String inviteCodeGenerator(){
        /*
         产生一个随机的6位字符串作为课程邀请码
         */
        String tempCode = MD5.getMD5(new Random().nextInt(10000000) + "hello");
        Random random = new Random();
        int randomLength = random.nextInt(25);
        inviteCode = tempCode.substring(randomLength,randomLength + 6);
        return inviteCode;
    }
    public void setInviteCode(){
        inviteCode = inviteCodeGenerator();
    }
    public void setClassName(String temp){
        className = temp;
    }
    public void setTeacherName(String temp){
        teacherName = temp;
    }
    public void setUsername(String temp){
        username = temp;
    }
    public String getUsername(){
        return username;
    }
    public String getClassName(){
        return className;
    }
    public String getTeacherName(){
        return teacherName;
    }
    @Override
    public String toString(){
        return "{username:" + username + ",teacherName:" + teacherName + ",className:" + className + ",inviteCode:" + inviteCode + "}";
    }
}
