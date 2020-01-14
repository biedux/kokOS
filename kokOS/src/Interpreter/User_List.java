package Interpreter;

import java.util.ArrayList;
import java.util.Scanner;

public class User_List {
    public ArrayList<User> UserList= new ArrayList<>();

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
        System.out.println("Dostepni uzytkownicy: ");
        for(int i=0; i<UserList.size(); i++)
        {
            System.out.println(UserList.get(i).getUserName() + ", " + UserList.get(i).getPassword() + ", " + UserList.get(i).getIsLogged());
        }
    }

    public void userAdd(String Name, String Password) throws Exception {
        boolean flag = false;
        for (int i = 0; i < UserList.size(); i++) {
            if (UserList.get(i).getUserName().equals(Name)) {
                flag = true;
            }
        }
        if (flag) {
            throw new Exception("Na liscie wystepuje juz podany uzytkownik!");
        } else {
            // for (int i = 0; i < UserList.size(); i++) {
            //   if (UserList.get(i).getUserName().equals((Name))) {
//
            //                  } else {
            for (int j=0; j<UserList.size(); j++) {
                if(UserList.get(j).getUserName().equals("Root") && UserList.get(j).getIsLogged() == true) {
                    UserList.add(new User(Name, Password));
                    System.out.println("Dodano uzytkownika: " + Name);
                }
                else if(UserList.get(j).getUserName()!="Root" && UserList.get(j).getIsLogged() == true)
                {
                    throw new Exception("Zalogowany uzytkownik nie moze dodac nowego uzytkownika!");
                }
            }
        }
    }
    //  }
    //    }


    public void userRemove(String ID) throws Exception {
        boolean flag = false;
        for (int i = 0; i < UserList.size(); i++) {
            if (UserList.get(i).getUserName().equals(ID)) {
                flag = true;
            }
        }
        if (!flag) {
            throw new Exception("Podany uzytkownik nie wystepuje na liscie!");
        } else {
            for (int i = 0; i < UserList.size(); i++) {
                if(UserList.get(i).getUserName().equals("Root") && UserList.get(i).getIsLogged() == true) {
                    for (int j=0; j<UserList.size(); j++) {
                        if (UserList.get(j).getUserName().equals(ID) && UserList.get(j).getUserName() != "Root") {
                            System.out.println("Usunieto uzytkownika: " + UserList.get(j).getUserName());
                            UserList.remove(j);
                        } else if (UserList.get(j).getUserName().equals(ID) && UserList.get(j).getUserName() == "Root") {
                            throw new Exception("Nie mozna usunac uzytkownika Root!");
                        }
                    }
                }
                else if(UserList.get(i).getUserName()!="Root" && UserList.get(i).getIsLogged() == true)
                {
                    throw new Exception("Zalogowany uzytkownik nie moze usunac istniejacego uzytkownika!");
                }
            }
        }
    }

    public void changeUser(String userName, String Password) throws Exception
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
            throw new Exception("Nie ma takiego uzytkownika na liscie!");
        }
        else
        {
            for(int i=0; i<UserList.size(); i++) {
                if(UserList.get(i).getUserName().equals(userName)) {
                    //Scanner scan = new Scanner(System.in);
                    //System.out.println("Podaj haslo: ");
                    //String Password = scan.nextLine();
                    if (Password.equals(UserList.get(i).getPassword())) {
                        for (int j = 0; j < UserList.size(); j++) {
                            if (UserList.get(j).getIsLogged() == true && !UserList.get(j).getUserName().equals(userName)) {
                                UserList.get(j).setLogged(false);
                            } else if (UserList.get(j).getUserName().equals(userName)) {
                                UserList.get(j).setLogged(true);
                                System.out.println("Zalogowano uzytkownika: " + UserList.get(j).getUserName());
                            }
                        }
                    }
                    else
                    {
                        throw new Exception("Bledne haslo!");
                    }
                }
            }
        }
    }

    public String printLoggedUser()
    {
        String Name = "";
        for(int i=0; i<UserList.size(); i++)
        {
            if(UserList.get(i).getIsLogged()==true) {
                Name = UserList.get(i).getUserName();
                //System.out.println("Zalogowany uzytkownik: " + UserList.get(i).getUserName());
            }
        }
        return Name;
    }



}