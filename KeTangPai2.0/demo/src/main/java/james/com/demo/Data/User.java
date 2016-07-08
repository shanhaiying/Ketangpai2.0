package james.com.demo.Data;

import com.android.volley.toolbox.StringRequest;

public class User {
    private String username;
    private String password;
    public void setUsername(String temp){
        this.username = temp;
    }
    public void setPassword(String temp){
        this.password = temp;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
