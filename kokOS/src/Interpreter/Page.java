package Interpreter;
// pojedynczy wpis w tablicy stronic

public class Page {
    boolean odniesienia;
    boolean valid;
    int nrramki;
    Page()
    {
        odniesienia = false;
        valid = false;
        nrramki = -1;
    }
}