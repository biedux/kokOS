package Interpreter;
import java.util.Vector;

public class Memory {
    private static char[] memory = new char[512];

    //16 ramek po 32 bajty
    // ostatnia ramka zarezerwowana na pipe'y i to co ciechan chce


    //FUNKCJE DO DEBUGGINGU

    //tu bedzie WYSWIETLENIE ZAWARTOSCI RAMU (do debugowania)
    public static void printRawRam() {
        System.out.println("________________________________________________________________");
        System.out.println("|                  Wyswietlam surowy ram:                      |\n");
        int tmp = 0;
        for (int i = 0; i < 512; i++) {
            System.out.print(memory[i] + "|");
            if(tmp % 32 == 31){
                System.out.print("\n");
            }
            tmp++;
        }
        System.out.println("----------------------------------------------------------------\n");
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
    public static char readPipe(char a, int adresLogicz){     //adresy od 0 do 15 dozwolone
        if (adresLogicz < 16){
            try {
                return memory[adresLogicz + 480];
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
                memory[adresLogicz + 480] = data;
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
                odczyt.add(memory[480 + i]);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return odczyt;
    }

    //czyszczenie PIPE
    public static void clearPIPE(){
        for (int i = 0; i < 16; i++){
            try {
                memory[480 + i] = ' ';
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
                return memory[adresLogicz + 496];
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
                memory[adresLogicz + 496] = data;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //odczyt ramki MC
    public static Vector<Character> readMCFrame(){
        Vector<Character> odczyt = new Vector<Character>();
        try {
            for (int i = 0; i < 16; i++){
                odczyt.add(memory[496 + i]);
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
                memory[496 + i] = ' ';
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
