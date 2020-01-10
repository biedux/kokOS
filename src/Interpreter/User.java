package Interpreter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class User {
    public String userName;
    public static String Password;
    public boolean isOwner;
    public boolean isOnList;
    public boolean Read;
    public boolean Write;
    ACL ACL;



    public User(String userName)
    {
        this.userName = userName;
    }

    public static List<User> UserList = new LinkedList<User>(); //lista przechowujaca uzytkownikow

    public String getUserName() {return userName;}


    public void isOwner()
    {
        if(isOwner == true && isOnList == true)
        {
            //ACL.setRead(true);
            //ACL.setWrite(true);
        }
        else if(isOwner == false && isOnList == true) //jezeli uzytkownik znajduje sie na liscie uzytkownikow
        {
            //ACL.setRead(true);
            //ACL.setWrite(false);
        }
        else //jezeli nie jest wlascicielem i nie znajduje sie na liscie uzytkownikow
        {
            //ACL.setRead(false);
            //ACL.setWrite(false);
        }
    }

    public boolean checkPermission()
    {

        return true;
    }

    public String getPermissions()
    {
        return userName;
    }

    public void log_in (String Name)
    {
        this.userName = Name;
        Scanner scan = new Scanner(System.in);
        System.out.println("Password: ");
        String Pass = scan.nextLine();
        if (Pass == Password)
        {
            System.out.println("Logged in!");
        }
        else
        {
            System.out.println("Wrong password!");
        }
    }

    public void log_out (String Name)
    {
        this.userName = Name;
        System.out.println("Logged out!");
    }



}