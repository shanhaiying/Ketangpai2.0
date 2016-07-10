package james.com.demo.Data;

public class ClassInfo {
    private String className;
    private String teacherName;
    public ClassInfo(String temp1, String temp2){
        className = temp1;
        teacherName = temp2;
    }
    public String getClassName(){
        return "课程名称: " + className;
    }
    public String getTeacherName(){
        return "教师: " + teacherName;
    }
}
