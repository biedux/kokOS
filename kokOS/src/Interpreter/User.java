package Interpreter;

public class User {
    private String UserName;
    private String Password;
    private boolean isLogged;

    User(String UserName)
    {
        this.UserName = UserName;
        this.Password = null;
    }

    public void setUserName(String userName)
    {
        this.UserName = UserName;
    }
    public void setPassword(String Password)
    {
        this.Password = Password;
    }
    public String getUserName()
    {
        return UserName;
    }
    public String getPassword()
    {
        return Password;
    }

    User(String UserName, String Password)
    {
        this.UserName = UserName;
        this.Password = Password;
    }



    public boolean getIsLogged()
    {
        return isLogged;
    }

    public void setLogged(boolean isLogged)
    {
        this.isLogged = isLogged;
    }

}
