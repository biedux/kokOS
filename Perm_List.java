package com.poznan.put;

import java.util.ArrayList;
import java.util.List;

public class Perm_List {
    public ArrayList<Perm> PermList = new ArrayList<>();
    //druga lista upraw
    //trzecia


    Perm_List(String UserID)
    {
        Perm p = new Perm();
        p.setUserId(UserID);
        p.setRw(3);
        p.setOwner(true);
        PermList.add(p);
    }

//lista obiektow o trzech argumentach czy tablica trzech list o jednym argumencie obiektu

    public void addPerm(String userID, int rw, boolean Owner)
    {
        Perm p = new Perm();
        p.setUserId(userID);
        p.setRw(rw);
        p.setOwner(Owner);
        PermList.add(p);
    }

    public void removePerm(String userID) {

        for (int i = 0; i < PermList.size(); i++) {
            if (PermList.get(i).getUserID() == userID) {
                PermList.remove(i);
            }
        }
    }
    /*   //2
       public void addPerm(String userID, int RW, boolean Owner)
       {
           Perm p = new Perm();
           p.setUserId(userID);
           p.setRw((RW));
           p.setOwner(Owner);
           Perm_List.add(p);
       }
   */
    public void changeParam(String UserID, int rw)
    {
        for(int i = 0; i < PermList.size(); i++)
            if(PermList.get(i).getUserID() == UserID)
            {
                PermList.get(i).setRw(rw);
            }
    }

    public void makeUser(String userName)
    {
        PermList.add(new Perm(userName, 3, true));
        for (int i=0; i<PermList.size(); i++)
        {
            if (PermList.get(i).getUserID() != userName)
            {

                PermList.add(new Perm( PermList.get(i).getUserID(), 2, false));
            }
        }
    }

    public void getPermission(String UserID) {
        for(int i = 0; i < PermList.size(); i++)
            if(PermList.get(i).getUserID() == UserID)
            {
                {
                    PermList.get(i).getRW();
                }
            }
    }

    public void printAllPermission()
    {
        for(int i=0; i<PermList.size(); i++)
        {
            System.out.println(PermList.get(i).getUserID() + ", " + PermList.get(i).getRW() + ", " + PermList.get(i).getOwner());
        }
    }



    //tutaj zmiana uprawnien fun(userid, rw)
}


