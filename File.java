package com.poznan.put;

public class File
{
    String name;
    int number; //numer i-węzła
    int positionPtr; //pozycja czytania pliku

    public void printFile()
    {
        System.out.println("Nazwa: " + name);
        System.out.println("\nNumer i-wezla: " + number);
        System.out.println("\nPozycja czytania pliku: " + positionPtr);
    }
}
