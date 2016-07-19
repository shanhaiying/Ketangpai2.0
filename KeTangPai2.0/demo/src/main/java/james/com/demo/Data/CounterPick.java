package james.com.demo.Data;

public class CounterPick {
    private String stuID;
    private String inviteCode;
    public CounterPick(){}
    public CounterPick(String mStuID,String mInviteCode){
        this.stuID = mStuID;
        this.inviteCode = mInviteCode;
    }
    public void setStuID(String mStuID){
        this.stuID = mStuID;
    }
    public void setInviteCode(String mInviteCode){
        this.inviteCode = mInviteCode;
    }
    @Override
    public String toString(){
        return "{stuID:" + stuID + ",inviteCode:" + inviteCode + "}";
    }
}
