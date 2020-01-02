package memory;

import java.util.Vector;

public class memory {
    private static char[] memory = new char[512];

    //16 ramek po 32 bajty
    // ostatnia ramka zarezerwowana na pipe'y


    //FUNKCJE DO DEBUGGINGU

    //tu bedzie WYSWIETLENIE ZAWARTOSCI RAMU (do debugowania)
    public static void printRawRam() {
        System.out.println("////////////////////////////////////////////////////////////");
        System.out.println("                Wyswietlam surowy ram:");
        System.out.println("////////////////////////////////////////////////////////////\n");
        for (int i = 0; i < 511; i++) {
            System.out.print(memory[i]);
        }
        System.out.println("\n");
    }


    //ODCZYT POJEDYNCZEGO BAJTU
    public static char read(int i) {
        try {
            return memory[i];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }


    //ZAPIS POJEDYNCZEGO BAJTU
    public static void write(char data, int i){
        try {
            memory[i] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //FUNKCJE DO PIPE

    //odczyt z PIPE
    public static char readPipe(int adresLogicz){     //adresy od 0 do 31 dozwolone
        try {
            return memory[adresLogicz + 480];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }


    //zapis do PIPE
    public static void writePipe(char data, int adresLogicz){     //adresy od 0 do 31 dozwolone
        try {
            memory[adresLogicz + 480] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //odczyt ramki PIPE
    public static Vector<Character> readPipeFrame(){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 32; i++){
                odczyt.add(memory[480 + i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odczyt;
    }


    //FUNKCJE UŻYTKOWE

    //tu bedzie ZAPIS STRONICY W RAMCE
    public static boolean writeFrame(Vector<Character> data, int frame){
        if ((frame < 0) || (frame > 15)){
            return false;
        }
        for (int i = 0; i < data.size(); i++){
            memory[frame * 32 + i] = data.get(i);
        }
        return true;
    }


    //tu bedzie ODCZYT STRONICY Z RAMKI
    public static Vector<Character> readFrame(int frame){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 32; i++){
                odczyt.add(memory[frame * 32 + i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odczyt;
    }


    //tu bedzie ZAPIS BAJTU ZGODNIE Z TABLICA STRONIC
    public static void writeToFrame(char data, int adress, int frame){
        try {
            memory[frame * 32 + (adress % 32)] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    //tu bedzie ODCZYT BAJTU ZGODNIE Z TABLICA STRONIC
    public static char readFromFrame(int adress, int frame){
        try {
            return memory[frame * 32 + (adress % 32)];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}

