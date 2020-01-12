package Interpreter;
public class Inode
{
    public boolean state; //otwarty/uzywany przez proces
    public int size; //rozmiar
    public int LinkCounter; //licznik stałych dowiązań
    public Lock lock; //Zamek
    public int [] blocks = new int [2]; //2 pierwsze pola to nr bloków bezpośrednich, ostatnie pole to nr bloku indeksowego
    public User owner; //Właściciel pliku
    Perm_List permissions;

    public void printInode()
    {
        System.out.println("\nStan: " + state);
        System.out.println("\nRozmiar pliku: " + size);
        System.out.println("\nLicznik dowiazan: " + LinkCounter);
        //System.out.println("\nStan zamka: " + lock.tryLock());
        System.out.println("\nNr bloku bezposredniego: " + blocks[0]);
        System.out.println("\nNr bloku indeksowego: " + blocks[1] + "\n");
    }
}

