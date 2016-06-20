package james.com.demo.Data;

import java.util.Date;

public class Student {
    private int id;
    private String name;
    private int date;
    public void setID(int id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDate(int date){
        this.date = date;
    }
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getDate(){
        return date;
    }
    @Override
    public String toString(){
        return "Student [id=" + id + ",name=" + name + ",date=" + date + "]";
    }
}
