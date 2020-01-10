import java.util.Hashtable;
import java.util.ArrayList;

public class User_List {
    public ArrayList<User> UserList= new ArrayList<>();
//funkcja zmiana uzytkownika, nazwa i haslo,
    //zaloguj, czy username jest na liscie
    //zmieniam isLogged na false staremu
    //true nowemu
    //komentarz

    //jak stworzyc admina przy starcie
    //jak przypisac adminowi argumenty

    User_List()
    {
        UserList.add(new User("Root", ""));
        for (int i=0; i<UserList.size(); i++)
        {
            if(UserList.get(i).getUserName() == "Root")
            {
                UserList.get(i).setLogged(true);
            }
        }
    }

    public void printUsers()
    {
        for(int i=0; i<UserList.size(); i++)
        {
            System.out.println(UserList.get(i).getUserName() + ", " + UserList.get(i).getPassword() + ", " + UserList.get(i).getIsLogged());
        }
    }

    public void userAdd(String Name, String Password)
    {
        UserList.add(new User(Name, Password));
    }


    public void userRemove(String ID)
    {
        for(int i=0; i<UserList.size(); i++)
        {
            if(UserList.get(i).getUserName()==ID)
            {
                UserList.remove(i);
            }
        }
    }

    public void changeUser(String userName)
    {
        boolean flag = false;
        for(int i=0; i<UserList.size(); i++)
        {
            if(UserList.get(i).getUserName().equals(userName)) {
                flag = true;
            }
        }
        if (!flag)
        {
            System.out.println("Nie ma takiego uzytkownika na liscie!");
        }
        else
        {
            for(int i=0; i<UserList.size(); i++)
            {
                if(UserList.get(i).getIsLogged()==true) {
                    UserList.get(i).setLogged(false);
                }
                else if(UserList.get(i).getUserName().equals(userName)) {
                    UserList.get(i).setLogged(true);
                }
            }
        }
    }


/*    public void changeUser (String userID, String newUser)
    {
        {
            if(UserList.get(userID).getIsLogged() == true)
            {
                UserList.get(userID).setLogged(false);
                UserList.get(newUser).setLogged(true);
            }
        }
    }
*/





}