package Interpreter;

import java.sql.SQLOutput;
import java.util.*;

public class Shell {

    private static Process_Management PM;
    Disc disc = new Disc();
    public User_List user_list = new User_List();
    static IPC ipc;
    static OpenFileTab open_file_table = new OpenFileTab();
    static Memory ram;
    static VirtualMemory virtual;
    static Scheduler scheduler=new Scheduler();
    private Map<String, String> allCommands = new LinkedHashMap<>();
    private Map<String, String> argCommands = new LinkedHashMap<>();

    static {
        try {
            PM = new Process_Management();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Shell()
    {

    }

    public static Process_Management getPM() { return PM;}
    public static Scheduler getScheduler() {return scheduler;}
    // GETY DO INTERPRETERA

    public void start()
    {
        addAllCommands();
        while(true)
        {
            read();
        }
    }

    private void addAllCommands()
    {
        allCommands.put("MAN" , "Wyswietla liste komend");
        argCommands.put("MAN", " ");
        allCommands.put("EXIT", "Kończy prace systemu");
        argCommands.put("EXIT", " ");

        // PLIKI
        allCommands.put("CREATE_FILE", "Tworzy nowy plik");
        argCommands.put("CREATE_FILE", "[Nazwa pliku]");

        allCommands.put("OPEN_FILE" , "Otwiera istniejący plik");
        argCommands.put("OPEN_FILE", "[Nazwa pliku] [Nazwa procesu]");

        allCommands.put("WRITE_FILE" , "Zapisuje do pliku");
        argCommands.put("WRITE_FILE" , "[Nazwa pliku] [Dane do zapisania]");

        allCommands.put("APPEND_FILE" , "Dopisuje na końcu pliku");
        argCommands.put("APPEND_FILE" , "[Nazwa pliku] [Dane do dopisania");

        allCommands.put("READ_FILE" , "Odczytuje z pliku daną ilość znaków");
        argCommands.put("READ_FILE" , "[Nazwa pliku] [liczba znaków do odczytania]");

        allCommands.put("CLOSE_FILE" , "Zamyka plik");
        argCommands.put("CLOSE_FILE" , "[Nazwa pliku]");


        allCommands.put("DELETE_FILE" , "Usuwa plik");
        argCommands.put("DELETE_FILE" , "[Nazwa pliku]");

        allCommands.put("RENAME_FILE" , "Zmienia nazwe pliku");
        argCommands.put("RENAME_FILE" , "[Nazwa pliku] , [Nowa nazwa pliku]");


        allCommands.put("CREATE_LINK" , "Tworzy dowiązanie");                                       //
        argCommands.put("CREATE_LINK" , "[Nazwa pliku] [Nazwa dowiązania] ");                                      // dodac argumenty


        allCommands.put("DISC" , "Wyświetla zawartość dysku");
        argCommands.put("DISC" , " ");

        allCommands.put("DIRECTORY" , "Wyświetla zawartość katalogu");
        argCommands.put("DIRECTORY" , " ");

        allCommands.put("TAB_OPEN_FILES" , "Wyświetla tablice otwartych plików");    //               //print TAB_OPEN_FILE


        allCommands.put("INODE_FILE" , "Wyświetla zawartość i-wezła pliku");           //             //


        allCommands.put("INODE_TAB" , "Wyświetla tablice i-węzłów");                     //           //



        //Uprawnienia w plikach
        allCommands.put("CHANGE_FILE_PERM" , ""); // zmiana uprawnien w pliku           //



        // Uprawnienia
        allCommands.put("USERS" , "Wyswietla wszystkich użytkowników");
        argCommands.put("USERS" , " ");

        allCommands.put("NEW_USER" , "Tworzy nowego użytkownika");
        argCommands.put("NEW_USER" , "[Nazwa użytkownika] [hasło]");

        allCommands.put("LOGIN" , "Loguje się na podanego użytkownika");                //
        argCommands.put("LOGIN" , "[Nazwa użytkownika] [hasło]");

        allCommands.put("DELETE_USER", "Usuwa użytkownika");                            //
        argCommands.put("DELETE_USER", "[Nazwa użytkownika] [hasło]");


        //Procesy
        allCommands.put("CREATE_PROCESS" , " ");
        argCommands.put("CREATE_PROCESS" , "[Nazwa procesu] [Nazwa pliku] [Priorytet]");

        allCommands.put("CREATE_PROCESS_PARENT" , " ");
        argCommands.put("CREATE_PROCESS_PARENT" , "[Nazwa procesu] [Nazwa pliku] [Priorytet] [Nazwa rodzica]");

        allCommands.put("DELETE_PROCESS" , " ");
        argCommands.put("DELETE_PROCESS" , "[Nazwa procesu]");


        allCommands.put("DELETE_PROCESS_GROUP" , " ");
        argCommands.put("DELETE_PROCESS_GROUP" , "[Nazwa procesu]");


        allCommands.put("PAGE_TABLE_PROCESS","Wyświetla tablice stronnic dla danego procesu");
        argCommands.put("PAGE_TABLE_PROCESS","[ID_procesu]");
        //VIRTUALMEMORY.printPageTable(ID)
        allCommands.put("PAGE_TABLE","Wyświetla wszystkie tablice stronnic");
        argCommands.put("PAGE_TABLE"," ");
        //printPageTable()
        allCommands.put("VICTIM_QUEUE","Wyświetla kolejke ofiar");
        argCommands.put("VICTIM_QUEUE"," ");
        //printQueue()
        allCommands.put("","Program od procesu");
        argCommands.put("","");
        //printPageFile(ID procesu)
        allCommands.put("","-||- wszystkie ");
        argCommands.put("","");
        //printPageFile()
        allCommands.put("","Wyświetla ");
        argCommands.put("","");


        allCommands.put("PROCESS_LIST" , " ");
        argCommands.put("PROCESS_LIST" , " ");


        allCommands.put("PROCESS_TREE" , " ");
        argCommands.put("PROCESS_TREE" , " "); //init

//        allCommands.put("PCB" , " ");
//        allCommands.put("PCB" , " ");
//
//                        System.out.println("CREATE_PROCESS -> Stworzenie procesu");
//                        //PM.fork(PM.init,"nazwa",priorytet-pewnie domyslnu,"plik z programem assemblerowyn");
//
//                        System.out.println("DELETE_PROCESS -> Usunięcie procesu");
//                        //PM.kill(PM.findPCB("nazwa procesu"));          usuwa jeden
//                        //PM.killByGroup(PM.findPCB("nazwa procesu"));
//
//                        System.out.println("PROCESS_LIST -> Wyświetla liste procesów");
//                        //PM.showAllProcesses();
//
//                        System.out.println("PROCESS_TREE -> Wyswietla drzewo procesów");
//                        //
//
//                        //PCB-infom
//                        //PM.findPCB("nazwa").printProcessInfo();
    }

    private static void command_check(String[] command, Map<String, String> argCommands) {
        System.out.println("Podano błędne argumenty. \nPrawidłowa składnia: " + command[0]+" "+ argCommands.get(command[0]));
    }

    private void read()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj komende");
        String input = scan.nextLine();
        String[] command = input.split(" ");
        command[0] = command[0].toUpperCase();

        switch (command[0])
        {
            case "MAN": {
                if(command.length == 1)
                    man();
                else
                    command_check(command,argCommands);
                break;
            }
            case "EXIT": {
                if(command.length == 1)
                    exit();
                else
                    command_check(command,argCommands);
                break;
            }
            case "CREATE_FILE": {
                if(command.length == 2) {
                    try {
                        disc.createFile(command[1], user_list.printLoggedUser());
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                } else
                    command_check(command, argCommands);
                break;
            }
            case "OPEN_FILE":{
                if (command.length == 3)
                {
                    try
                    {
                        disc.openFile(command[1],PM.findPCB(command[2]));
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                {
                    command_check(command,argCommands);
                }
                break;
            }
            case "WRITE_FILE":{
                if(command.length > 2)
                {
                    try
                    {
                        String data = "";
                        for(int i = 2; i< command.length; i++)
                        {
                            data = data + command[i];
                            if(i != command.length-1)
                                data = data + " ";
                        }
                        disc.writeFile(command[1],user_list.printLoggedUser(), data);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command, argCommands);
                break;
            }
            case "APPEND_FILE":{
                if(command.length > 2)
                {
                    try
                    {
                        String data = "";
                        for(int i = 2; i< command.length; i++)
                        {
                            data = data + command[i];
                            if(i != command.length-1)
                                data = data + " ";
                        }
                        disc.appendFile(command[1],user_list.printLoggedUser(), data);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command, argCommands);
                break;
            }
            case "READ_FILE":{
                if(command.length == 2)
                {
                    try{
                        int amount;
                        amount = Integer.parseInt(command[2]);
                        disc.readFile(command[1], user_list.printLoggedUser(), amount);
                    }
                    catch(NumberFormatException e )
                    {
                        command_check(command,argCommands);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "CLOSE_FILE":{
                //GDZIE JEST ZAMKNIĘCIE??

                break;
            }
            case "DELETE_FILE":{
                if (command.length == 2)
                {
                 try{
                     disc.deleteFile(command[1],user_list.printLoggedUser());
                 }
                 catch (Exception e)
                 {
                     System.out.println(e.getLocalizedMessage());
                 }
                }
                else
                    command_check(command, argCommands);
                break;
            }
            case "RENAME_FILE":{
                if(command.length == 3)
                {
                    try
                    {
                       disc.renameFile(command[1],user_list.printLoggedUser(),command[2]);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command, argCommands);
                break;
            }
            case "CREATE_LINK":{
                if(command.length == 3)
                {
                    try
                    {
                        disc.createLink(command[1],command[2]);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "DISC":{
                if(command.length == 1)
                    disc.printDisc();
                else
                    command_check(command,argCommands);
                break;
            }
            case "DIRECTORY":{
                if(command.length == 1)
                    disc.ListDirectory();
                else
                    command_check(command,argCommands);
                break;
            }
            case "TAB_OPEN_FILES":{
                if(command.length == 1)
                {
                    try
                    {
                        open_file_table.printTab();
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "INODE_FILE":{

            }
            case "INODE_TAB":{

            }
            case "CHANGE_FILE_PERM":{

            }
            case "USERS": {
                user_list.printUsers();
                break;
            }
            case "NEW_USER": {
                if(command.length == 3)
                {
                    try
                    {
                        user_list.userAdd(command[1],command[2]);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }

                }
                else
                {
                    command_check(command,argCommands);
                }
                break;
            }
            case "LOGIN":{
                if(command.length == 3)
                {
                    try
                    {
                        user_list.changeUser(command[1]);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "DELETE_USER":{


                break;
            }
            case "CREATE_PROCESS":{
                if(command.length == 4)
                {
                    try
                    {
                        int amount;
                        amount = Integer.parseInt(command[3]);
                        PCB p=PM.fork(PM.init, command[1], amount, command[2]);
                        if (PM.ProcessList.size() == 2)
                            PM.scheduler.check();
                    }
                    catch(NumberFormatException e )
                    {
                        command_check(command,argCommands);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "CREATE_PROCESS_PARENT":
            {
                if(command.length == 5)
                {
                    try
                    {
                        int amount;
                        amount = Integer.parseInt(command[3]);
                        PM.fork(PM.findPCB(command[4]), command[1], amount, command[2]);
                        if (PM.ProcessList.size() == 2)
                            PM.scheduler.check();
                    }
                    catch(NumberFormatException e )
                    {
                        command_check(command,argCommands);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "PROCESS_LIST": {
                if(command.length == 1)
                    PM.showAllProcesses();
                else
                    command_check(command,argCommands);

                break;
            }
            case "PROCESS_TREE": {
                if(command.length == 1)
                {
                    PM.showTree(PM.init);
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "PCB":
            {
                if (command.length == 2) {
                    try {
                        PM.findPCB(command[1]).printProcessInfo();
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                break;
            }
            case "DELETE_PROCESS":
            {
                if(command.length == 2)
                {
                    try{
                        PCB pcb = PM.findPCB(command[1]);
                        PM.kill(pcb);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                        if(PM.ProcessList.size()==1){
                            PM.scheduler.check();
                        }
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "DELETE_PROCESS_GROUP":
            {
             if(command.length == 2)
             {
                 try
                 {
                     PCB pcb = PM.findPCB(command[1]);
                     PM.killByGroup(pcb);
                 }
                 catch(Exception e)
                 {
                     System.out.println(e.getLocalizedMessage());
                 }
             }
             else
                 command_check(command,argCommands);
                break;
            }
            default: {
                System.out.println("Błędna komenda");
                man();
                break;
            }
        }
    }

    private void man()
    {
        for (String command : allCommands.keySet())
        {
//            int j = 0;
            String space = "";
//            j = command.length() + argCommands.get(command).length();
//            for (int i = 0; i < (60 - j); i++)
//            {
//                space = space + " ";
//            }
            System.out.println(command + " " + argCommands.get(command) + space + ": " + allCommands.get(command));
        }
    }

    private void exit()
    {
        System.exit(0);
    }
}


//    public static void spis_tresci()
//    {
//        System.out.println("********************");
//        System.out.println("ZARZADZANIE DYSKIEM");
//        System.out.println("********************\n");
//
//        System.out.println("1. Stworz plik");
//        System.out.println("2. Otworz plik");
//        System.out.println("3. Zapisz do pliku");
//        System.out.println("4. Dopisz na koniec pliku");
//        System.out.println("5. Odczytaj z pliku");
//        System.out.println("6. Zamknij plik");
//        System.out.println("7. Usun plik");
//        System.out.println("8. Utworz dowiazanie");
//        System.out.println("9. Zmien nazwe pliku");
//        System.out.println("10. Wyswietl zawartosc katalogu");
//        System.out.println("11. Wyswietl zawartosc dysku");
//        System.out.println("12. Wyswietl zawartosc tablicy otwartych plikow");
//        System.out.println("13. Wyswietl zawartosc i-wezla pliku");
//        System.out.println("14. Wyswietl zawartosc tablicy i-wezlow");
//        System.out.println("15. Utworz uzytkownika");
//        System.out.println("16. Zmien uzytkownika");
//
//        System.out.println("Podaj nr polecenia: ");
//    }
//
//        try
//        {
//            Disc disc = new Disc();
//            User_List user_list = new User_List();
//            Hashtable <String, PCB> pcblist = new Hashtable<String, PCB>();
//            char znak;
//            do {
//                spis_tresci();
//                Scanner scan = new Scanner(System.in);
//                int x = scan.nextInt();
//                switch(x)
//                {
//                    case 1:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next(); System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        disc.createFile(name, user);
//                        System.out.println("Utworzono plik: " + name + "\n\n");
//                        break;
//                    }
//                    case 2:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę procesu: ");
//                        String pro = scan.next();
//                        PCB process = new PCB("process1", "file");
//                        pcblist.put(pro, process);
//                        disc.openFile(name, process);
//                        System.out.println("Pomyslnie otworzono plik\n\n");
//                        break;
//                    }
//                    case 3:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        System.out.println("Podaj dane do zapisania: ");
//                        String data = scan.next();
//                        disc.writeFile(name, user, data);
//                        System.out.println("Pomyslnie zapisano do pliku\n\n");
//                        break;
//                    }
//                    case 4:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        System.out.println("Podaj dane do zapisania: ");
//                        String data = scan.next();
//                        disc.appendFile(name,user, data);
//                        System.out.println("Pomyslnie zapisano do pliku\n\n");
//                        break;
//                    }
//                    case 5:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        System.out.println("Podaj ilosc danych do sczytania: ");
//                        int amount = scan.nextInt();
//                        System.out.println("Dane sczytane z pliku:");
//                        System.out.println(disc.readFile(name,user, amount) + "\n\n");
//                        break;
//                    }
//                    case 6:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę procesu: ");
//                        String pro = scan.next();
//                        if (pcblist.get(pro) == null)
//                        {
//                            System.out.println("Proces nie istnieje");
//                            break;
//                        }
//                        else
//                        {
//                            disc.closeFile(name, pcblist.get(pro));
//                            pcblist.remove(pro);
//                            System.out.println("Zamknieto plik\n\n");
//                            break;
//                        }
//                    }
//                    case 7:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        disc.deleteFile(name,user);
//                        System.out.println("Usunieto plik: " + name + "\n\n");
//                        break;
//                    }
//                    case 8:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nowa nazwe (nazwa dowiazania): ");
//                        String newName = scan.next();
//                        disc.createLink(name, newName);
//                        break;
//                    }
//                    case 9:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        System.out.println("Podaj nowa nazwe: ");
//                        String newName = scan.next();
//                        disc.renameFile(name,user, newName);
//                        break;
//                    }
//                    case 10:
//                    {
//                        disc.ListDirectory();
//                        break;
//                    }
//                    case 11:
//                    {
//                        disc.printDisc();
//                        break;
//                    }
//                    case 12:
//                    {
//                        disc.open_file_table.printTab();
//                        break;
//                    }
//                    case 13:
//                    {
//                        System.out.println("Podaj nazwę pliku: ");
//                        String name = scan.next();
//                        disc.printSingleInode(name);
//                        break;
//                    }
//                    case 14:
//                    {
//                        disc.printInodeTable();
//                        break;
//                    }
//                    case 15:
//                    {
//                        System.out.println("Podaj nazwę nowego uzytkownika: ");
//                        String user = scan.next();
//                        user_list.userAdd(user,"iksde");
//                        user_list.printUsers();
//                        break;
//                    }
//                    case 16:
//                    {
//                        System.out.println("Podaj nazwę uzytkownika: ");
//                        String user = scan.next();
//                        user_list.changeUser(user);
//                        break;
//                    }
//                    default:
//                    {
//                        System.out.println("Blad! Podaj poprawny nr z listy\n\n");
//                        break;
//                    }
//                }
//                System.out.println("Czy chcesz zamknac program? (t/n): ");
//                znak = scan.next().charAt(0);
//
//            } while (znak != 't');
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }