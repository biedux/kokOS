package Process_Management;

import java.util.LinkedList;
import java.util.List;

public class Process {

    //Nazwa procesu
    private String pName;

    //ID procesu
    private int pID;

    //ID rodzica procesu
    private int pParentID;

    //Lista ID dzieci procesu
    List<Process> pChildrenList=new LinkedList<Process>();

    //Stany procesu
    private enum pStateL{
        New,Running,Waiting,Ready,Terminated,Zombie
    };

    // Pole odpowiedzialne za przechowywanie aktualnego stanu procesu.
    public pStateL pState;

    // Licznik procesów, który pomaga w nadawaniu ID.
    private static int pCount = 0;

    // Rejestry.
    private int AX, BX, CX, DX, pCounter;

    // Flagi przeniesienia i zera
    private boolean ZF, CF;

    // Czas do wykonania
    private int pTimeExec;

    // Priorytet
    private int pPrior;

    // Tablica stronic.
    //private PageTab processTab;

    // Nazwa pliku z danymi programu - niedopowiedziane przez moduły.
    private String fileName;

    // Rozmiar pliku.
    private int sizeOfFile;

    public Process(String pName, String fileName, int sizeOfFile){
        this.pID=pCount;
        pCount++;

        this.pName=pName;
        this.fileName=fileName;
        this.sizeOfFile=sizeOfFile;
        this.pState=pStateL.New;

        this.AX=0;
        this.BX=0;
        this.CX=0;
        this.DX=0;
        this.pCounter=0;
        ZF=true;
        CF=false;

        this.pTimeExec=0;
        this.pPrior=0;7b

        System.out.println("Utworzono proces: " + pName + " o ID: " + pID);
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String Name) {
        System.out.println("Stara nazwa procesu: " + this.pName);
        this.pName = Name;
        System.out.println("Nowa nazwa procesu: " + this.pName);
    }

    public int getpID() {
        return pID;
    }

    public void setpID(int ID) {
        System.out.println("Stare ID procesu: " + this.pID);
        this.pID = ID;
        System.out.println("Nowe ID procesu: " + this.pID);
    }

    public int getpParentID() {
        return pParentID;
    }

    public void setpParentID(int ParentID) {
        System.out.println("Stare ID procesu rodzica: " + this.pParentID);
        this.pParentID = ParentID;
        System.out.println("Nowe ID procesu rodzica: " + this.pParentID);
    }

    public pStateL getState() {
        return pState;
    }

    public void setState(pStateL state) {
        this.pState = state;
    }

    public static int getpCount() {
        return pCount;
    }

    public int getpCounter() {
        return pCounter;
    }

    public int getAX() {
        return AX;
    }

    public int getBX() {
        return BX;
    }

    public int getCX(){
        return CX;
    }

    public int getDX(){
        return DX;
    }

    public void setAX(int wAX){
        System.out.println("Poprzedni stan rejestru AX: " + this.AX);
        this.AX=wAX;
        System.out.println("Obecny stan rejestru AX: " + this.AX);
    }

    public void setBX(int wBX){
        System.out.println("Poprzedni stan rejestru BX: " + this.BX);
        this.BX=wBX;
        System.out.println("Obecny stan rejestru BX: " + this.BX);
    }

    public void setCX(int wCX){
        System.out.println("Poprzedni stan rejestru CX: " + this.CX);
        this.CX=wCX;
        System.out.println("Obecny stan rejestru CX: " + this.CX);
    }

    public void setDX(int wDX){
        System.out.println("Poprzedni stan rejestru DX: " + this.DX);
        this.DX=wDX;
        System.out.println("Obecny stan rejestru DX: " + this.DX);
    }

    public boolean getZF(){
        return ZF;
    }

    public void setZF(boolean R){
        if(R==false){
            this.ZF=true;
        }
        else this.ZF=false;
    }

    public boolean getCF(){
        return CF;
    }

    public void setCF(){
        this.CF=true;
    }

    public void printProcessInfo(){
        System.out.println("Proces o nazwie: " + pName + ", ID: " + pID + ", stanie: " + pState + " i ID rodzica: " + pParentID);
    }
}
