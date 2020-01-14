package Interpreter;

import java.sql.SQLOutput;
import java.util.*;

public class Shell {

    private static Process_Management PM;
    static Disc disc = new Disc();
    public User_List user_list = new User_List();
    static IPC ipc;
    static OpenFileTab open_file_table = new OpenFileTab();
    static Scheduler scheduler=new Scheduler();
    static VirtualMemory virtual;
    static Memory ram;
    static  Interpreter interpreter;
    private Map<String, String> allCommands = new LinkedHashMap<>();
    private Map<String, String> argCommands = new LinkedHashMap<>();

    static {
        try {
            PM = new Process_Management();
            interpreter = new Interpreter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Memory getRam() {return ram;}
    public static Disc getDisc() {return disc;}
    public static VirtualMemory getVirtual() {return virtual;}
    public static OpenFileTab getOpenFileTab()
    {
        return open_file_table;
    }
    public static IPC getIpc() {return ipc;}

    public Shell()
    {

    }

    public static Process_Management getPM() { return PM;}
    public static Scheduler getScheduler() {return scheduler;}

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

        allCommands.put("TAB_OPEN_FILES" , "Wyświetla tablice otwartych plików");
        argCommands.put("TAB_OPEN_FILES" , " ");

        allCommands.put("INODE_FILE" , "Wyświetla zawartość i-wezła pliku");
        argCommands.put("INODE_FILE" , "[Nazwa]");

        allCommands.put("INODE_TAB" , "Wyświetla tablice i-węzłów");                               // ???                     //           //
        argCommands.put("INODE_TAB" , " ");

        //Uprawnienia w plikach

        allCommands.put("FILE_PERM", "Wyświetla liste uprawnień dla danego pliku");
        argCommands.put("FILE_PERM", "[Nazwa pliku}");

        allCommands.put("CHANGE_FILE_PERM" , ""); // zmiana uprawnien w pliku           //
        argCommands.put("CHANGE_FILE_PERM" , "[Nazwa pliku] [Nazwa użytkownika] [uprawnienie]");


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
        allCommands.put("CREATE_PROCESS" , "Tworzy proces ");
        argCommands.put("CREATE_PROCESS" , "[Nazwa procesu] [Nazwa pliku] [Priorytet]");

        allCommands.put("CREATE_PROCESS_PARENT" , " Tworzy proces ze wskazaniem na rodzica");
        argCommands.put("CREATE_PROCESS_PARENT" , "[Nazwa procesu] [Nazwa pliku] [Priorytet] [Nazwa rodzica]");

        allCommands.put("PROCESS_LIST" , "Wyświetla liste procesów ");
        argCommands.put("PROCESS_LIST" , " ");

        allCommands.put("PROCESS_TREE" , "Wyświetla drzewo procesów ");
        argCommands.put("PROCESS_TREE" , " ");

        allCommands.put("PCB" , "Wyświetla PCB");
        argCommands.put("PCB" , " ");

        allCommands.put("DELETE_PROCESS" , "Usuwa proces ");
        argCommands.put("DELETE_PROCESS" , "[Nazwa procesu]");

        allCommands.put("DELETE_PROCESS_GROUP" , "Usuwa grupe procesów ");
        argCommands.put("DELETE_PROCESS_GROUP" , "[Nazwa procesu]");


        //Wirtualna
        allCommands.put("PAGE_TABLE","Wyświetla tablice stronnic dla procesów");
        argCommands.put("PAGE_TABLE","[ID_procesu] lub pozostaw puste aby wyświetlić wszystkie");

        allCommands.put("VICTIM_QUEUE","Wyświetla kolejke ofiar");
        argCommands.put("VICTIM_QUEUE"," ");
        //printQueue()
        allCommands.put("PAGE_FILE","Wyświetla ");
        argCommands.put("PAGE_FILE","[ID procesu] lub pozostaw puste aby wyświetlić wszystkie");

        allCommands.put("STEP"," Wykonuje krok");
        argCommands.put("STEP","[Liczba kroków] lub pozostaw puste aby zrobić jeden krok");

        allCommands.put("RAM", "Wyświetla pamięć RAM");
        argCommands.put("RAM", " ");
    }

    private static void command_check(String[] command, Map<String, String> argCommands) {
        System.out.println("Podano błędne argumenty. \nPrawidłowa składnia: " + command[0]+" "+ argCommands.get(command[0]));
    }

    private void read()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Wpisz komende");
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
                if(command.length == 3)
                {
                    try{
                        int amount;
                        amount = Integer.parseInt(command[2]);
                        String text = disc.readFile(command[1], user_list.printLoggedUser(), amount);
                        System.out.println(text);
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
                if(command.length == 2)
                {
                    try
                    {
                        int P_id = open_file_table.findPID(command[1]);
                        disc.closeFile(command[1], PM.findById(P_id));
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
                if(command.length == 2)
                {
                    try
                    {
                        disc.printSingleInode(command[1]);
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
            case "INODE_TAB":{
                if(command.length == 1)
                {
                    try
                    {
                        disc.printInodeTable();
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


            case "FILE_PERM": {
                //file perm [plik]
                if(command.length == 2)
                {
                    Perm_List perm = disc.getPermissions(command[1]);
                    perm.printAllPermission();
                }
                else
                    command_check(command,argCommands);

                break;
            }
            case "CHANGE_FILE_PERM":{
                //CHANGE [PLIK] [UŻYTKOWNIK] [PERM]
                if(command.length == 4)
                {
                    Perm_List perm = disc.getPermissions(command[1]);
                    if(perm != null)
                    {
                        try{
                            int permission;
                            permission = Integer.parseInt(command[3]);
                            perm.changeParam(command[2],permission);
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
                    {
                        System.out.println("Plik o podanej nazwie nie istnieje");
                    }
                }
                else
                {
                 command_check(command,argCommands);
                }
                break;
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
                        user_list.changeUser(command[1], command[2]);
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else if(command.length == 2)
                {
                    if(command[1].equals("Root"))
                    {
                        try
                        {
                            user_list.changeUser("Root","");
                        }
                        catch(Exception e)
                        {
                            System.out.println(e.getLocalizedMessage());
                        }
                    }
                    else
                    {
                        System.out.println("To nie root");
                        command_check(command,argCommands);
                    }
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "DELETE_USER":{
                if(command.length == 2)
                {
                    try
                    {
                        user_list.userRemove(command[1]);
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
            case "CREATE_PROCESS_PARENT": {
                if(command.length == 5)
                {
                    try
                    {
                        int priority;
                        priority = Integer.parseInt(command[3]);
                        PM.fork(PM.findPCB(command[4]), command[1], priority, command[2]);
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
            case "PCB": {
                if (command.length == 2) {
                    try {
                        PM.findPCB(command[1]).printProcessInfo();
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                break;
            }
            case "DELETE_PROCESS": {
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
            case "DELETE_PROCESS_GROUP": {
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

            case "PAGE_TABLE": {
                if(command.length == 1)
                {
                    virtual.printPageTable();
                }
                else if(command.length == 2)
                {
                    try
                    {
                        virtual.printPageTable(PM.findPCB(command[1]).getID());
                    }
                    catch(Exception e )
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else
                    command_check(command,argCommands);

                break;
            }
            case "VICTIM_QUEUE": {
                if(command.length == 1)
                {
                    virtual.printQueue();
                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "PAGE_FILE": {
                if(command.length == 2)
                {
                    try
                    {
                        virtual.printPageFile(PM.findPCB(command[1]).getID());
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else if(command.length == 1)
                {
                    virtual.printPageFile();
                }
                else
                    command_check(command,argCommands);
                break;
            }

            case "STEP":{
                if(command.length == 1)
                {
                    try
                    {
                        interpreter.makeStep();
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                else if (command.length == 2)
                {
                    try
                    {
                        int number;
                        number = Integer.parseInt(command[1]);
                        for ( int i = number; i!=0;i--)
                        {
                            try
                            {
                                interpreter.makeStep();
                            }
                            catch (Exception e)
                            {
                                System.out.println(e.getLocalizedMessage());
                            }
                        }
                    }
                    catch(NumberFormatException e )
                    {
                        command_check(command,argCommands);
                    }

                }
                else
                    command_check(command,argCommands);
                break;
            }
            case "RAM": {
                if(command.length == 1)
                {
                    ram.printRawRam();
                }
                else
                {
                    command_check(command,argCommands);
                }

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
            int j = 0;
            String space = "";
            j = command.length() + argCommands.get(command).length();
            for (int i = 0; i < (80 - j); i++)
            {
                space = space + " ";
            }
            System.out.println(command + " " + argCommands.get(command) + space + ": " + allCommands.get(command));
        }
    }

    private void exit()
    {
        System.exit(0);
    }

}