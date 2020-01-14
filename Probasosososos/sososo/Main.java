package sososo;

public class Main {
    public static void main(String[] args) {
        User_List userList = new User_List();
        //  Perm_List permlist = new Perm_List();

        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.userAdd("U1", "1");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
      //  try {
        //    userList.userAdd("U1", "1");
        //} catch (Exception e) {
         //   e.printStackTrace();
       // }
        try {
            userList.userAdd("U2", "2");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        try {
            userList.userAdd("U5", "5");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        try {
            userList.userRemove("U1");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.changeUser("U2","2");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.userAdd("U4", "4");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        try {
            userList.userAdd("U3", "3");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.userRemove("U3");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.userRemove("U5");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.changeUser("Root","");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.userAdd("U3", "3");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();
        try {
            userList.userRemove("Root");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        userList.printUsers();
        userList.printLoggedUser();



    }
}
