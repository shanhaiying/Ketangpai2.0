package james.com.demo.Data;
import james.com.demo.Util.MD5;

/*
账号密码类
 */
public class Account {
    private String username;
    private String password;
    public Account(String mUsername,String mPassword){
        username = mUsername;
        password = encryptPassword(mPassword);
    }
    public void setUsername(String mUsername){
        username = mUsername;
    }
    public void setPassword(String mPassword){
        password = encryptPassword(mPassword);
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    private String encryptPassword(String mPassword){
        /*
        加盐 两次MD5 增加破解难度
        */
        return MD5.getMD5(MD5.getMD5("hello" + mPassword + "world"));
    }
    @Override
    public String toString(){
        return "{username:" + username + ",password:" + password + "}";
    }
}
