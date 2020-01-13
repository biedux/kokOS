package sososo;

import java.util.ArrayList;

    public class Perm_List extends User_List{
        public ArrayList<Perm> PermList = new ArrayList<>();

        Perm_List(String UserID)
        {
            Perm p = new Perm();
            p.setUserId(UserID);
            p.setRw(3);
            p.setOwner(true);
            PermList.add(p);
        }

        public void addPerm(String userID, int rw, boolean Owner) throws Exception {
            for (int i = 0; i < UserList.size(); i++) {
                for(int j=0; j<PermList.size(); i++) {
                    if (UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals("Root") ||
                            UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner()==(true)) {
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

        public void removePerm(String userID) throws Exception {
            for (int i = 0; i < UserList.size(); i++) {
                for (int j=0; j<PermList.size(); j++) {
                    if (UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals("Root") ||
                            UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner()==(true)) {
                        for (int k = 0; k < PermList.size(); k++) {
                            if (PermList.get(k).getUserID() == userID) {
                                PermList.remove(k);
                            }
                        }
                    }
                    else
                    {
                        throw new Exception("Zalogowany uzytkownik nie moze usunac uprawnienia!");
                    }
                }
            }
        }

        public void changeParam(String UserID, int rw) throws Exception {
            //if () zmiana uprawnien/ sprawdzanie czy uzytkownik jest "Root" lub owner
            //czy jednak sprawdzanie przy zmianach uprawnien dla pliku
            //funkcja printLoggedUser -> porownanie nazwy do "Root" lub getOwner == true
            for (int i = 0; i < UserList.size(); i++) {
                for (int j = 0; j < PermList.size(); j++) {
                    if (UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals("Root") ||
                            UserList.get(i).getIsLogged() == true && UserList.get(i).getUserName().equals(PermList.get(j).getUserID()) && PermList.get(j).getOwner() == (true)) {
                        for (int k = 0; k < PermList.size(); k++)
                            if (PermList.get(k).getUserID() == UserID) {
                                PermList.get(k).setRw(rw);
                            }
                            else
                            {
                                throw new Exception("Nie ma podanego uzytkownika na liscie!");
                            }
                    }
                    else
                    {
                        throw new Exception("Zalogowany uzytkownik nie moze zmienic uprawnien!");
                    }
                }
            }
        }

        public void makeUser(String userName)
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

    }




