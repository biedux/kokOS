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
        System.out.println("Numer i-wezla: " + number);
        System.out.println("Pozycja czytania pliku: " + positionPtr);
        System.out.println("Typ pliku: " + type);
    }
}