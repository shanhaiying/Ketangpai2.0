package james.com.demo.Data;

public class Profile {
    //下面是学生个人信息表
    //主要信息有 1.账号信息 2.学生姓名 3.出生日期 4.所属班级 5.性别 6.学号
    private String username;
    private String stuName;
    private int year;
    private int month;
    private int day;
    private String classBelong;
    private String sex;
    private String stuId;
    private String birthday;
    public Profile(){}//懒得写构造函数了
    public void setUsername(String mUsername){
        this.username = mUsername;
    }
    public void setBirthday(int mYear,int mMouth,int mDay){
        this.year = mYear;
        this.month = mMouth;
        this.day = mDay;
    }
    public void setBirthday(String time){
        birthday = time;
    }
    public void setClassBelong(String mClassBelong){
        this.classBelong = mClassBelong;
    }
    public void setSex(String mSex){
        this.sex = mSex;
    }
    public void setStuId(String mStuId){
        this.stuId = mStuId;
    }
    public void setStuName(String mStuName){
        this.stuName = mStuName;
    }
    public String getBirthday(){
        return year + "," + month + "," + day;
    }
    public String getBirthday(int wholeStyle){
        return birthday;
    }
    //主要信息有 1.账号信息 2.学生姓名 3.出生日期 4.所属班级 5.性别 6.学号
    @Override
    public String toString(){
        return "{username:" + username + ",stuName:" + stuName + ",birthday:" + getBirthday(1) + ",class_belong:" + classBelong + ",sex:" + sex + ",stuID:" + stuId + "}";
    }

}
