package Interpreter;

public class Perm {
    private String UserId;
    private int rw;
    private boolean Owner;

    Perm(){}

    //
    Perm(String UserID, int RW, boolean Owner)
    {
        this.UserId = UserID;
        this.rw = RW;
        this.Owner = Owner;
    }

    public String getUserID()
    {
        return UserId;
    }
    public int getRW()
    {
        return rw;
    }
    public boolean getOwner()
    {
        return Owner;
    }
    public void setOwner(boolean Owner)
    {
        this.Owner = Owner;
    }


    public void setUserId(String userId)
    {
        this.UserId = userId;
    }

    public void setRw(int rw)
    {
        this.rw = rw;
    }

}
