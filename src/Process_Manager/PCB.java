package Process_Manager;

import Pipes.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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
    private static int CountProcess = 0;

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
    //private PageTab processTab;

    // Nazwa pliku z danymi programu - niedopowiedziane przez moduły.
    private String fileName;

    // Rozmiar pliku.
    private int sizeOfFile;

    public IPC pipe;

    public PCB(String Name, String fileName){
        this.ID=CountProcess;
        CountProcess++;

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


        this.pipe=new IPC();
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
        //System.out.println("Poprzedni stan rejestru AX: " + this.AX);
        this.AX = AX;
        //System.out.println("Obecny stan rejestru AX: " + this.AX);
    }

    public void setBX(int BX) {
        //System.out.println("Poprzedni stan rejestru BX: " + this.BX);
        this.BX = BX;
        //System.out.println("Obecny stan rejestru BX: " + this.BX);
    }

    public void setCX(int CX) {
        //System.out.println("Poprzedni stan rejestru CX: " + this.CX);
        this.CX = CX;
        //System.out.println("Obecny stan rejestru CX: " + this.CX);
    }

    public void setDX(int DX) {
        //System.out.println("Poprzedni stan rejestru DX: " + this.DX);
        this.DX = DX;
        //System.out.println("Obecny stan rejestru DX: " + this.DX);
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

    l

    //Pipe
//    public final Vector<PipeQueue> Pipes = new Vector<>();
//    public final Vector<Integer> descriptor = new Vector<>();
//
//    public Vector<PipeQueue> getPipes() {
//        return Pipes;
//    }
//
//    public Vector<Integer> getDescriptor() {
//        return descriptor;
//    }
//
//    public int writeToPipe(int fd, Vector<Byte> buffer, Integer nbyte) { //funkcja zapisujaca do pipe
//        int written = 0;
//        for (PipeQueue e : Pipes) {
//            if (e.descW == fd) {
//
//                if (nbyte < 64 - e.eQueue.size())
//                    for (int i = 0; i < nbyte; i++) { //perhaps 16, do sprawdzenia
//                        Byte a = buffer.get(i); //buffer- segment danych w procesie, do którego majá zostac przekazane dane
//                        //tablica stronic, przydziel pamiec
//                        e.eQueue.add(a);
//                        written++;
//                    }
//                else if (nbyte > 64 - e.eQueue.size()) {
//                    //zwracana wartosc w RAM na 1 miejscu 0
//                }
//            }
//            //System.out.print("Writing to a pipe:  ");
//
//            //return nbyte
//        }
//        return written;
//    }
//
//    public int readFromPipe ( int fd, Vector<Byte > buffer, Integer nbyte)
//    { //funkcja - szok i niedowierzanie - odczytujaca z pipea
//        //(int fd, Vector<Bytes> buffer, int nbyte);
//        int read = 0;
//        for (PipeQueue e : Pipes) { //iterowanie again
//            if (e.descR == fd) {
//                for (int i = 0; i < nbyte; i++) {
//                    if (!e.eQueue.isEmpty()) {
//                        Byte a = e.eQueue.remove();
//                        buffer.add(a);
//                        read++;
//                    } else if (e.eQueue.isEmpty()) {
//                        //zapis 0 i elo
//                    }
//                    break;
//                }
//            }
//        }
//        //System.out.print("Reading from a Pipe:  ");
//        return read; //to int or not to int??
//    }
//
//    public static void closePipe () { //zamykanie pipea
//
//        System.out.print("The pipe has been closed... R.I.P.");
//
//    }
//
//    public static void close ( int i){
//        System.out.print("The descriptor is closed\n");
//    }
//
//    public int Pipe(int[] pdesc) { //funkcja tworzaca pipe
//
//        Random rand = new Random();
//        int read = rand.nextInt(32);
//        int write = rand.nextInt(32);//|| 32
//
//        while (descriptor.contains(write)) {
//            write = rand.nextInt(32);
//        }
//        write = pdesc[1];
//        descriptor.add(write);
//
//        while (descriptor.contains(read)) {
//            read = rand.nextInt(32);
//        }
//        read = pdesc[0];
//        descriptor.add(read);
//
//        PipeQueue queueToAdd = new PipeQueue(write, read);
//        Pipes.add(queueToAdd);
//        return 0;
//    }

    public void printProcessInfo() {
        //System.out.println("Proces o nazwie: " + this.Name + ", ID: " + this.ID + ", stanie: " + this.State + " i ID rodzica: " + this.ParentID);
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", "Name", "Id", "Priority", "State", "RegA", "RegB", "RegC", "RegD", "Counter", "Parent ID");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s %-10s\n", this.Name, this.ID, this.Priority, this.State, this.AX, this.BX, this.CX, this.DX, this.Counter, this.getParentID());
    }
}
