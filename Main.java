import java.util.Hashtable;
import java.util.Scanner;

public class Main {

    public static void spis_tresci()
    {
        System.out.println("********************");
        System.out.println("ZARZADZANIE DYSKIEM");
        System.out.println("********************\n");

        System.out.println("1. Stworz plik");
        System.out.println("2. Otworz plik");
        System.out.println("3. Zapisz do pliku");
        System.out.println("4. Dopisz na koniec pliku");
        System.out.println("5. Odczytaj z pliku");
        System.out.println("6. Zamknij plik");
        System.out.println("7. Usun plik");
        System.out.println("8. Utworz dowiazanie");
        System.out.println("9. Zmien nazwe pliku");
        System.out.println("10. Wyswietl zawartosc katalogu");
        System.out.println("11. Wyswietl zawartosc dysku");
        System.out.println("12. Wyswietl zawartosc tablicy otwartych plikow");
        System.out.println("13. Wyswietl zawartosc i-wezla pliku");
        System.out.println("14. Wyswietl zawartosc tablicy i-wezlow");
        System.out.println("15. Utworz uzytkownika");
        System.out.println("16. Zmien uzytkownika");

        System.out.println("Podaj nr polecenia: ");
    }


    public static void main(String[] args) {
        try
        {
            Shell shell = new Shell();
            User_List user_list = new User_List();
            //Hashtable <String, PCB> pcblist = new Hashtable<String, PCB>();
            char znak;
            do {
                spis_tresci();
                Scanner scan = new Scanner(System.in);
                int x = scan.nextInt();
                switch(x)
                {
                    case 1:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next(); System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        Shell.disc.createFile(name, user);
                        System.out.println("Utworzono plik: " + name + "\n\n");
                        break;
                    }
                    case 2:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę procesu: ");
                        String pro = scan.next();
                        PCB process = Shell.PM.fork(Shell.PM.init,pro,120,"");
                        System.out.println("przed openFile id: " + process.getID());
                        System.out.println("przed openFile  findem: " + Shell.PM.findPCB(pro).getID());
                        //PCB..put(pro, process);
                        Shell.getPM().ProcessList.add(process);
                        Shell.disc.openFile(name, process);
                        System.out.println("Pomyslnie otworzono plik\n\n");
                        break;
                    }
                    case 3:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        System.out.println("Podaj dane do zapisania: ");
                        String data = scan.next();
                        Shell.disc.writeFile(name, user, data);
                        System.out.println("Pomyslnie zapisano do pliku\n\n");
                        break;
                    }
                    case 4:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        System.out.println("Podaj dane do zapisania: ");
                        String data = scan.next();
                        Shell.disc.appendFile(name,user, data);
                        System.out.println("Pomyslnie zapisano do pliku\n\n");
                        break;
                    }
                    case 5:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        System.out.println("Podaj ilosc danych do sczytania: ");
                        int amount = scan.nextInt();
                        System.out.println("Dane sczytane z pliku:");
                        System.out.println(Shell.disc.readFile(name,user, amount) + "\n\n");
                        break;
                    }
                    case 6:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę procesu: ");
                        String pro = scan.next();
                        PCB pcb = Shell.PM.findPCB(pro);
                        if (pcb == null)
                        {
                            System.out.println("Proces nie istnieje");
                            break;
                        }
                        else
                        {
                            System.out.println(pcb.getID());
                            Shell.disc.closeFile(name, pcb);
                            System.out.println("Zamknieto plik\n\n");
                            break;
                        }
                    }
                    case 7:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        Shell.disc.deleteFile(name,user);
                        System.out.println("Usunieto plik: " + name + "\n\n");
                        break;
                    }
                    case 8:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nowa nazwe (nazwa dowiazania): ");
                        String newName = scan.next();
                        Shell.disc.createLink(name, newName);
                        break;
                    }
                    case 9:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        System.out.println("Podaj nowa nazwe: ");
                        String newName = scan.next();
                        Shell.disc.renameFile(name,user, newName);
                        break;
                    }
                    case 10:
                    {
                        Shell.disc.ListDirectory();
                        break;
                    }
                    case 11:
                    {
                        Shell.disc.printDisc();
                        break;
                    }
                    case 12:
                    {
                        Shell.open_file_table.printTab();
                        break;
                    }
                    case 13:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        Shell.disc.printSingleInode(name);
                        break;
                    }
                    case 14:
                    {
                        Shell.disc.printInodeTable();
                        break;
                    }
                    case 15:
                    {
                        System.out.println("Podaj nazwę nowego uzytkownika: ");
                        String user = scan.next();
                        user_list.userAdd(user,"iksde");
                        user_list.printUsers();
                        break;
                    }
                    case 16:
                    {
                        System.out.println("Podaj nazwę uzytkownika: ");
                        String user = scan.next();
                        user_list.changeUser(user);
                        break;
                    }
                    default:
                    {
                        System.out.println("Blad! Podaj poprawny nr z listy\n\n");
                        break;
                    }
                }
                System.out.println("Czy chcesz zamknac program? (t/n): ");
                znak = scan.next().charAt(0);

            } while (znak != 't');
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
