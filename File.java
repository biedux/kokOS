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
}
