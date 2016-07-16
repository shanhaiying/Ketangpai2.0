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
    public ClassInfo(String mClassName, String mTeacherName,String mUsername){
        className = mClassName;
        teacherName = mTeacherName;
        username = mUsername;
    }
    public String inviteCodeGenerate(){
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
        inviteCode = inviteCodeGenerate();
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
