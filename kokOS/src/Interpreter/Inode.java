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
        System.out.println("Rozmiar pliku: " + size);
        System.out.println("Licznik dowiazan: " + LinkCounter);
        System.out.println("Nr bloku bezposredniego: " + blocks[0]);
        System.out.println("Nr bloku indeksowego: " + blocks[1] + "\n");
    }
}
