package Process_Manager;

import java.util.LinkedList;
import java.util.List;

public class PCB {

    //Nazwa procesu
    private String Name;

    //ID procesu
    private int ID;

    //ID rodzica procesu
    private int ParentID;

    //Lista ID dzieci procesu
    List<PCB> ChildrenList=new LinkedList<PCB>();

    //Stany procesu
    public enum StateList{
        New,Running,Waiting,Ready,Terminated,Zombie
    };

    // Pole odpowiedzialne za przechowywanie aktualnego stanu procesu.
    public StateList State;

    // Licznik procesów, który pomaga w nadawaniu ID.
    private static int CountProcess = 0;

    // Czas procesora
    private int BaseTime;
    private int CurrTime;

    // Priorytety
    private int BasePriority;
    private int CurrPriority;

    // Rejestry.
    private int AX, BX, CX, DX, Counter;

    // Flagi przeniesienia i zera
    private boolean ZF, CF;

    // Tablica stronic.
    //private PageTab processTab;

    // Nazwa pliku z danymi programu - niedopowiedziane przez moduły.
    private String fileName;

    // Rozmiar pliku.
    private int sizeOfFile;



    public PCB(String Name, int priority, int time, String fileName, int sizeOfFile){
        this.ID=CountProcess;
        CountProcess++;

        this.Name=Name;
        this.fileName=fileName;
        this.sizeOfFile=sizeOfFile;
        this.State=StateList.New;

        this.AX=0;
        this.BX=0;
        this.CX=0;
        this.DX=0;

        this.Counter=0;
        this.BasePriority=priority;
        this.BaseTime=time;

        ZF=true;
        CF=false;

        System.out.println("Utworzono proces: " + this.Name + " o ID: " + this.ID);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        System.out.println("Stare ID procesu: " + this.ID);
        this.ID = ID;
        System.out.println("Nowe ID procesu: " + this.ID);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        System.out.println("Stara nazwa procesu: " + this.Name);
        Name = name;
        System.out.println("Nowa nazwa procesu: " + this.Name);
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        System.out.println("Stare ID procesu rodzica: " + this.ParentID);
        ParentID = parentID;
        System.out.println("Nowe ID procesu rodzica: " + this.ParentID);
    }

    public StateList getState() {
        return State;
    }

    public void setState(StateList state) {
        System.out.println("Stary stan procesu: " + this.State);
        State = state;
        System.out.println("Nowy stan procesu: " + this.State);
    }

    public static int getCountProcess() {
        return CountProcess;
    }

    public int getAX() {
        return AX;
    }

    public int getBX() {
        return BX;
    }

    public int getCX() {
        return CX;
    }

    public int getDX() {
        return DX;
    }

    public void setAX(int AX) {
        System.out.println("Poprzedni stan rejestru AX: " + this.AX);
        this.AX = AX;
        System.out.println("Obecny stan rejestru AX: " + this.AX);
    }

    public void setBX(int BX) {
        System.out.println("Poprzedni stan rejestru BX: " + this.BX);
        this.BX = BX;
        System.out.println("Obecny stan rejestru BX: " + this.BX);
    }

    public void setCX(int CX) {
        System.out.println("Poprzedni stan rejestru CX: " + this.CX);
        this.CX = CX;
        System.out.println("Obecny stan rejestru CX: " + this.CX);
    }

    public void setDX(int DX) {
        System.out.println("Poprzedni stan rejestru DX: " + this.DX);
        this.DX = DX;
        System.out.println("Obecny stan rejestru DX: " + this.DX);
    }

    public int getBasePriority() {
        return BasePriority;
    }

    public int getBaseTime() {
        return BaseTime;
    }

    public int getCurrPriority() {
        return CurrPriority;
    }

    public int getCurrTime() {
        return CurrTime;
    }

    public boolean isCF() {
        return CF;
    }

    public void setCF(boolean CF) {
        this.CF = CF;
    }

    public boolean isZF() {
        return ZF;
    }

    public void setZF(boolean ZF) {
        this.ZF = ZF;
    }

    public List<PCB> getChildrenList() {
        return ChildrenList;
    }

    public void printProcessInfo() {
        System.out.println("Proces o nazwie: " + this.Name + ", ID: " + this.ID + ", stanie: " + this.State + " i ID rodzica: " + this.ParentID);
    }
}
