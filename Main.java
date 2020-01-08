import java.util.Hashtable;
import java.util.Scanner;

public class Main {

    public static void spis_tresci()
    {
        System.out.println("********************\n");
        System.out.println("ZARZADZANIE DYSKIEM\n");
        System.out.println("********************\n\n");
        System.out.println("1. Stworz plik\n");
        System.out.println("2. Otworz plik\n");
        System.out.println("3. Zapisz do pliku\n");
        System.out.println("4. Dopisz na koniec pliku\n");
        System.out.println("5. Odczytaj z pliku\n");
        System.out.println("6. Zamknij plik\n");
        System.out.println("7. Usun plik\n");
        System.out.println("8. Utworz dowiazanie\n");
        System.out.println("9. Zmien nazwe pliku\n");
        System.out.println("10. Wyswietl zawartosc katalogu\n");
        System.out.println("11.Wyswietl zawartosc dysku\n\n");

        System.out.println("Podaj nr polecenia: ");
    }


    public static void main(String[] args) {
        try
        {
            Disc disc = new Disc();
            Hashtable <String, PCB> pcblist = new Hashtable<String, PCB>();
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
                        String name = scan.next();
                        disc.createFile(name, "root");
                        System.out.println("Utworzono plik: " + name + "\n\n");
                        break;
                    }
                    case 2:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę procesu: ");
                        String pro = scan.next();
                        PCB process = new PCB("process1", "file");
                        pcblist.put(pro, process);
                        disc.openFile(name, process);
                        System.out.println("Pomyslnie otworzono plik\n\n");
                        break;
                    }
                    case 3:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj dane do zapisania: ");
                        String data = scan.next();
                        disc.writeFile(name, data);
                        System.out.println("Pomyslnie zapisano do pliku\n\n");
                        break;
                    }
                    case 4:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj dane do zapisania: ");
                        String data = scan.next();
                        disc.appendFile(name, data);
                        System.out.println("Pomyslnie zapisano do pliku\n\n");
                        break;
                    }
                    case 5:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj ilosc danych do sczytania: ");
                        int amount = scan.nextInt();
                        System.out.println("Dane sczytane z pliku:");
                        System.out.println(disc.readFile(name, amount) + "\n\n");
                        break;
                    }
                    case 6:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nazwę procesu: ");
                        String pro = scan.next();
                        if (pcblist.get(pro) == null)
                        {
                            System.out.println("Proces nie istnieje");
                            break;
                        }
                        else
                        {
                            disc.closeFile(name, pcblist.get(pro));
                            pcblist.remove(pro);
                            System.out.println("Zamknieto plik\n\n");
                            break;
                        }
                    }
                    case 7:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        disc.deleteFile(name);
                        System.out.println("Usunieto plik: " + name + "\n\n");
                        break;
                    }
                    case 8:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nowa nazwe (nazwa dowiazania): ");
                        String newName = scan.next();
                        disc.createLink(name, newName);
                        break;
                    }
                    case 9:
                    {
                        System.out.println("Podaj nazwę pliku: ");
                        String name = scan.next();
                        System.out.println("Podaj nowa nazwe: ");
                        String newName = scan.next();
                        disc.renameFile(name, newName);
                        break;
                    }
                    case 10:
                    {
                        disc.ListDirectory();
                        break;
                    }
                    case 11:
                    {
                        disc.printDisc();
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