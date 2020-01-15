package Interpreter;

//import Pipes.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
//import VirtualMemory.*;

public class PCB {

    //Nazwa procesu
    private String Name;

    //ID procesu
    public int ID;

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
    public static int CountProcess = 0;

    // Zapis kodu
    public String code;

    // Czas procesora
    private int Time;

    //Konrada życzenia
    private int BaseTime;
    private int VirtualTime;
    private int weight;
    private int CurrTime;
    private int TimeSlice;

    public int getBaseTime() {
        return BaseTime;
    }

    public void setBaseTime(int baseTime) {
        BaseTime = baseTime;
    }

    public int getVirtualTime() {
        return VirtualTime;
    }

    public void setVirtualTime(int virtualTime) {
        VirtualTime = virtualTime;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCurrTime() {
        return CurrTime;
    }

    public void setCurrTime(int currTime) {
        CurrTime = currTime;
    }

    public int getTimeSlice() {
        return TimeSlice;
    }

    public void setTimeSlice(int timeslice) {
        TimeSlice = timeslice;
    }

    //Po Konradzie

    // Priorytety
    private int Priority;

    // Rejestry.
    private int AX, BX, CX, DX;

    // Licznik rozkazow
    private int Counter;

    // Flagi przeniesienia i zera
    private boolean ZF, CF;

    // Tablica stronic.
    public Vector<Page> PageTable = new Vector<Page>();

    // Nazwa pliku z danymi programu - niedopowiedziane przez moduły.
    private String fileName;

    // Rozmiar pliku.
    private int sizeOfFile;

    // public IPC pipe;

    public PCB(String Name, String fileName){

        this.ID=CountProcess;

        this.Name=Name;
        this.fileName=fileName;
        this.State=StateList.New;

        this.AX=0;
        this.BX=0;
        this.CX=0;
        this.DX=0;

        this.Counter=0;

        ZF=true;
        CF=false;


        //this.pipe=new IPC();

        //System.out.println("Utworzono proces: " + this.Name + " o ID: " + this.ID);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        //System.out.println("Stare ID procesu: " + this.ID);
        this.ID = ID;
        //System.out.println("Nowe ID procesu: " + this.ID);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        //System.out.println("Stara nazwa procesu: " + this.Name);
        Name = name;
        //System.out.println("Nowa nazwa procesu: " + this.Name);
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        //System.out.println("Stare ID procesu rodzica: " + this.ParentID);
        ParentID = parentID;
        //System.out.println("Nowe ID procesu rodzica: " + this.ParentID);
    }

    public StateList getState() {
        return State;
    }

    public void setState(StateList state) {
        //System.out.println("Stary stan procesu: " + this.State);
        State = state;
        //System.out.println("Nowy stan procesu: " + this.State);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static int getCountProcess() {
        return CountProcess;
    }

    public static void setCountProcess(int countProcess) {
        CountProcess = countProcess;
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
        //System.out.println("Poprzedni stan rejestru AX: " + this.AX);
        if(AX<0){
            this.AX=0;
        }
        else this.AX = AX;
        //System.out.println("Obecny stan rejestru AX: " + this.AX);
    }

    public void setBX(int BX) {
        //System.out.println("Poprzedni stan rejestru BX: " + this.BX);
        if(BX<0){
            this.BX=0;
        }
        else this.BX = BX;
        //System.out.println("Obecny stan rejestru BX: " + this.BX);
    }

    public void setCX(int CX) {
        //System.out.println("Poprzedni stan rejestru CX: " + this.CX);
        if(CX<0){
            this.CX=0;
        }
        else this.CX = CX;
        //System.out.println("Obecny stan rejestru CX: " + this.CX);
    }

    public void setDX(int DX) {
        //System.out.println("Poprzedni stan rejestru DX: " + this.DX);
        if(DX<0){
            this.DX=0;
        }
        else this.DX = DX;
        //System.out.println("Obecny stan rejestru DX: " + this.DX);
    }

    public Vector<Page> getPageTable() {
        return PageTable;
    }

    public void setPageTable(Vector<Page> pageTable) {
        PageTable = pageTable;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public int Priority() {
        return Priority;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int priority) {
        Priority = priority;
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

    public int getCounter() {
        return Counter;
    }

    public void setCounter(int counter) {
        Counter = counter;
    }

    public void printProcessInfo() {
        //System.out.println("Proces o nazwie: " + this.Name + ", ID: " + this.ID + ", stanie: " + this.State + " i ID rodzica: " + this.ParentID);
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", "Name", "Id", "Priority", "State", "RegA", "RegB", "RegC", "RegD", "Counter", "Parent ID");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", this.Name, this.ID, this.Priority, this.State, this.AX, this.BX, this.CX, this.DX, this.Counter, this.getParentID());
    }
}
