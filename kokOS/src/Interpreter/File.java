package Interpreter;
public class File
{
    public enum Types
    {
        FILE,
        LINK
    }
    String name; //nazwa pliku
    Types type; //typ, plik lub dowiązanie
    int number; //numer i-węzła
    int positionPtr; //pozycja czytania pliku

    public void printFile()
    {
        System.out.println("Nazwa: " + name);
        System.out.println("\nNumer i-wezla: " + number);
        System.out.println("\nPozycja czytania pliku: " + positionPtr);
        if(type == Types.FILE)
        {
            System.out.println("Typ pliku: plik tekstowy");
        }
        else
        {
            System.out.println("Typ pliku: dowiazanie");
        }
    }
}
