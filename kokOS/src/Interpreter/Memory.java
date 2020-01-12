package Interpreter;

import java.util.Vector;

public class Memory {
    private static char[] Memory = new char[512];

    //16 ramek po 32 bajty
    // ostatnia ramka zarezerwowana na pipe'y i to co ciechan chce


    //FUNKCJE DO DEBUGGINGU

    //tu bedzie WYSWIETLENIE ZAWARTOSCI RAMU (do debugowania)
    public static void printRawRam() {
        System.out.println("_________________________________________________________________");
        System.out.println("|                   Wyswietlam surowy ram:                      |\n");
        int tmp = 0;
        System.out.print("|");
        for (int i = 0; i < 512; i++) {
            System.out.print(Memory[i] + "|");
            if(tmp % 32 == 31){
                System.out.print("\n|");
            }
            tmp++;
        }
        System.out.println("----------------------------------------------------------------\n");
    }


    //ODCZYT POJEDYNCZEGO BAJTU
    public static char read(int i) {
        try {
            return Memory[i];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }


    //ZAPIS POJEDYNCZEGO BAJTU
    public static void write(char data, int i){
        try {
            Memory[i] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //FUNKCJE DO PIPE

    //odczyt z PIPE
    public static char readPipe(int adresLogicz){     //adresy od 0 do 15 dozwolone
        if (adresLogicz < 16){
            try {
                return Memory[adresLogicz + 480];
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return 0;
        } else {
            return 0;
        }
    }


    //zapis do PIPE
    public static void writePipe(char data, int adresLogicz){     //adresy od 0 do 15 dozwolone
        if (adresLogicz < 16){
            try {
                Memory[adresLogicz + 480] = data;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //odczyt ramki PIPE
    public static Vector<Character> readPipeFrame(){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 16; i++){
                odczyt.add(Memory[480 + i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odczyt;
    }

    public static int writeString(String napis, int adress, int frame){
        int licznik = 0;
        try {
            for (int i = 0; i < napis.length();i++){
                char wsadz = napis.charAt(i);
                Memory[frame * 32 + (adress % 32) + i] = wsadz;
                licznik++;
            }
            Memory[frame * 32 + (adress % 32) + licznik] = ' ';
            licznik++;
            //System.out.println("Zapisano " + licznik + " bajtów");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
        return licznik;
    }

    //czyszczenie PIPE
    public static void clearPIPE(){
        for (int i = 0; i < 16; i++){
            try {
                Memory[480 + i] = ' ';
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    //FUNKCJE DLA M.CIECHAN

    //odczyt chara z ramki MC
    public static char readMC(int adresLogicz){     //adresy od 0 do 15 dozwolone
        if (adresLogicz < 16){
            try {
                return Memory[adresLogicz + 496];
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return 0;
        } else {
            return 0;
        }
    }


    //zapis chara do MC
    public static void writeMC(char data, int adresLogicz){     //adresy od 0 do 15 dozwolone
        if (adresLogicz < 16){
            try {
                Memory[adresLogicz + 496] = data;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int writeNumMC(int liczba, int adresLogicz){  //adresy od 0 do 15 dozwolone
        int licznik = 0;
        if (adresLogicz < 16){
            try {
                String tmp = String.valueOf(liczba);
                for (int i = 0; i < tmp.length();i++){
                    char wsadz = tmp.charAt(i);
                    Memory[adresLogicz + 496 + i] = wsadz;
                    licznik++;
                }
                Memory[adresLogicz + 496 + licznik] = ' ';
                licznik++;
                System.out.println("Zapisano " + licznik + " bajtów");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                return 0;
            }
        }
        return licznik;
    }

    public static int readNumMC(int adresLogicz){   //adresy od 0 do 15
        try{
            int i = 0;
            char tmp;
            String czytany = "";
            do{
                tmp = Memory[adresLogicz + 496 + i];
                czytany += tmp;
                i++;
            } while (Memory[adresLogicz + 496 + i] != ' ');
            int num = Integer.parseInt(czytany);
            System.out.println("Odczytano " + num + ".");
            return num;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }

    }


    //odczyt ramki MC
    public static Vector<Character> readMCFrame(){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 16; i++){
                odczyt.add(Memory[496 + i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odczyt;
    }

    //czyszczenie MC
    public static void clearMC(){
        for (int i = 0; i < 16; i++){
            try {
                Memory[496 + i] = ' ';
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //FUNKCJE UŻYTKOWE

    //tu bedzie ZAPIS STRONICY W RAMCE
    public static boolean writeFrame(Vector<Character> data, int frame){
        if ((frame < 0) || (frame > 14)){
            return false;
        }
        for (int i = 0; i < data.size(); i++){
            Memory[frame * 32 + i] = data.get(i);
        }
        return true;
    }


    //tu bedzie ODCZYT STRONICY Z RAMKI
    public static Vector<Character> readFrame(int frame){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 32; i++){
                odczyt.add(Memory[frame * 32 + i]);
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
            Memory[frame * 32 + (adress % 32)] = data;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    //tu bedzie ODCZYT BAJTU ZGODNIE Z TABLICA STRONIC
    public static char readFromFrame(int adress, int frame){
        try {
            return Memory[frame * 32 + (adress % 32)];
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}