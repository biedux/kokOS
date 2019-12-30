package memory;

import java.util.Vector;

public class memory {
    private static char[] memory = new char[256];

    //16 ramek po 16 bajtów
    // ostatnia ramka zarezerwowana na pipe'y


    //tu bedzie WYSWIETLENIE ZAWARTOSCI RAMU (do debugowania)
    public static void printRawRam() {
        System.out.println("Wyswietlam surowy ram: \n");
        for (int i = 0; i < 256; i++) {
            System.out.println(memory[i]);
        }
        System.out.println("\n");
    }


    //ODCZYT POJEDYNCZEGO BAJTU
    public char read(int i) {
        try {
            return memory[i];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }


    //tu funkcje do PIPE

    //ZAPIS POJEDYNCZEGO BAJTU
    public void write(char data, int i){
        try {
            memory[i] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //zapis do PIPE
    public void writePipe(char data, int adresLogicz){     //adresy od 0 do 15 dozwolone
        try {
            memory[adresLogicz + 240] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //tu bedzie ZAPIS STRONICY W RAMCE
    public static boolean writeFrame(Vector<Character> data, int frame){
        if ((frame < 0) || (frame > 15)){
            return false;
        }
        for (int i = 0; i < data.size(); i++){
            memory[frame * 16 + i] = data.get(i);
        }
        return true;
    }




    //tu bedzie ODCZYT STRONICY Z RAMKI
    public static Vector<Character> readFrame(int frame){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 16; i++){
                odczyt.add(memory[frame * 16 + i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odczyt;
    }

    //odczyt z PIPE
    public char readPipe(int adresLogicz){     //adresy od 0 do 15 dozwolone
        try {
            return memory[adresLogicz + 240];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }



    //tu bedzie ZAPIS BAJTU ZGODNIE Z TABLICA STRONIC
    public void writeToFrame(char data, int adress, int frame){
        try {
            memory[frame * 16 + (adress % 16)] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    //tu bedzie ODCZYT BAJTU ZGODNIE Z TABLICA STRONIC
    public char readFromFrame(int adress, int frame){
        try {
            return memory[frame * 16 + (adress % 16)];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}

