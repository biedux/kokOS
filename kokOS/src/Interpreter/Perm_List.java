package Interpreter;

import java.util.ArrayList;

public class Perm_List extends User_List{
    public ArrayList<Perm> PermList = new ArrayList<>();

    Perm_List(String userName)
    {
        int sizeU = UserList.size();
        for(int i=0; i<sizeU; i++) {
            if(UserList.get(i).getUserName().equals("Root"))
            {
                Perm p = new Perm();
                p.setUserId(UserList.get(i).getUserName());
                p.setRw(3);
                p.setOwner(true);
                PermList.add(p);
            }
            else if(UserList.get(i).equals(userName)) {
                Perm p = new Perm();
                p.setUserId(UserList.get(i).getUserName());
                p.setRw(3);
                p.setOwner(true);
                PermList.add(p);
            }
            else
            {
                Perm p = new Perm();
                p.setUserId(UserList.get(i).getUserName());
                p.setRw(2);
                p.setOwner(false);
                PermList.add(p);
            }
        }
    }

    /*   public void addPerm(String userID, int rw, boolean Owner) throws Exception {
           if (userID.equals("Root")) {
               throw new Exception("Nie mozna dodawac uprawnien dla uzytkownika Root!");
           }
           else {
               for (int i = 0; i < UserList.size(); i++) {
                   for (int j = 0; j < PermList.size(); i++) {
                       if (UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals("Root") ||
                               UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner() == (true)) {
                           Perm p = new Perm();
                           p.setUserId(userID);
                           p.setRw(rw);
                           p.setOwner(Owner);
                           PermList.add(p);
                       } else {
                           throw new Exception("Zalogowany uzytkownik nie moze utworzyc nowego uprawnienia!");
                       }
                   }
               }
           }
       }
       public void removePerm(String userID) throws Exception {
           if(userID.equals("Root"))
           {
               throw new Exception("Nie mozna usunac uprawnien dla uzytkownika Root!");
           }
           else {
               for (int i = 0; i < UserList.size(); i++) {
                   for (int j = 0; j < PermList.size(); j++) {
                       if (UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals("Root") ||
                               UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner() == (true)) {
                           for (int k = 0; k < PermList.size(); k++) {
                               if (PermList.get(k).getUserID().equals(userID)) {
                                   PermList.remove(k);
                               } else {
                                   throw new Exception("Nie ma danego uzytkownika na liscie!");
                               }
                           }
                       } else {
                           throw new Exception("Zalogowany uzytkownik nie moze usunac uprawnienia!");
                       }
                   }
               }
           }
       }
*/
    public void changeParam(String UserID, int rw) throws Exception {
        //if () zmiana uprawnien/ sprawdzanie czy uzytkownik jest "Root" lub owner
        //czy jednak sprawdzanie przy zmianach uprawnien dla pliku
        //funkcja printLoggedUser -> porownanie nazwy do "Root" lub getOwner == true
        boolean flag = false;
        for(int j = 0; j < PermList.size(); j++)
        {
            if(PermList.get(j).getUserID().equals(UserID))
            {
                flag = true;
            }
        }
        if(flag==true) {
            if (UserID.equals("Root")) {
                throw new Exception("Nie mozna zmienic uprawnien dla uzytkownika Root!");
            } else {
                for (int i = 0; i < UserList.size(); i++) {
                    for (int j = 0; j < PermList.size(); j++) {
                        if (UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals("Root") ||
                                UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner() == (true)) {
                            for (int k = 0; k < PermList.size(); k++)
                                if (PermList.get(k).getUserID() == UserID) {
                                    PermList.get(k).setRw(rw);
                                }
                        } else if(UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner() == (false)){
                            throw new Exception("Zalogowany uzytkownik nie moze zmienic uprawnien!");
                        }
                    }
                }
            }
        }
        else
        {
            throw new Exception("Nie ma podanego uzytkownika na liscie!");
        }
    }

    /*     public void makeUser(String userName)
         {
             PermList.add(new Perm(userName, 3, true));
 //        for (int i=0; i<PermList.size(); i++)
 //        {
 //            if (PermList.get(i).getUserID() != userName)
 //            {
 //
 //                PermList.add(new Perm( PermList.get(i).getUserID(), 2, false));
 //            }
 //        }
         }
 */
    public void getPermission(String UserID) throws Exception {
        boolean flag = false;
        for(int j = 0; j < PermList.size(); j++)
        {
            if(PermList.get(j).getUserID().equals(UserID))
            {
                flag = true;
            }
        }
        if(flag==true) {
            for (int i = 0; i < PermList.size(); i++)
                if (PermList.get(i).getUserID() == UserID) {
                    {
                        System.out.println("ID: " + PermList.get(i).getUserID());
                        System.out.println("Prawa: " + PermList.get(i).getRW());
                        System.out.println("Właściciel(T/F): " + PermList.get(i).getOwner());
                    }
                }
        }
        else
        {
            throw new Exception("Nie ma podanego uzytkownika na liscie!");
        }
    }

    public void printAllPermission()
    {
        System.out.println("Prawa uzytkownikow: ");
        System.out.println("3: Read + Write");
        System.out.println("2: Read");
        System.out.println("1: Write");
        System.out.println("0: Brak uprawnień");
        System.out.println("Uprawnienia dostepnych uzytkownikow: ");
        System.out.println();
        for(Perm p:PermList)
        {
            System.out.println("ID: " + p.getUserID() + ", Prawa: " + p.getRW() + ", Właściciel(T/F): " + p.getOwner());
        }
    }

}
