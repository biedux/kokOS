package com.company;
import java.io.FileNotFoundException;
import java.util.*;

//todo dodac fcje Create Pipe do assmbly i wwsolac Create Pipe w Interpreterze zapisywanie 0 kurwa nie dziala
public class IPC {
    public static final Vector<PipeQueue> Pipes = new Vector<>();
    public static final Vector<Integer> descriptor = new Vector<>();

    private static Process_Management PM;

    static {
        try {
            PM = new Process_Management();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public IPC() throws FileNotFoundException {}

    public Vector<PipeQueue> getPipes() {
        return Pipes;
    }

    public Vector<Integer> getDescriptor() {
        return descriptor;
    }

    public static void Scan(){
        Scanner myObj1 = new Scanner (System.in);
        System.out.println("Enter data");
        String c = myObj1.nextLine();
        VirtualMemory.saveString(PM.init, 5, c);
        Memory.printRawRam();

    }

    public int writeToPipe(int fd, Vector<Character> buffer, int nchar) {
        Scanner myObj1 = new Scanner (System.in);
        System.out.println("Enter data");
        String c = myObj1.nextLine();
        VirtualMemory.saveString(PM.init, 5, c);
        Memory.printRawRam();

        int written = 0;

        for (PipeQueue e : Pipes) {

            if (e.descW == fd) {

                if (1>0) {
                    if(c== "\n"){
                        c.replace('\n', (char) 0);
                        VirtualMemory.saveString(PM.init, 5, c);
                    }

                    for (int i = 0; i < 4; i++) { //buffer.size()
                        if (VirtualMemory.readChar(PM.init, 5 + i) == ' ') {
                            break;
                        }
                        if (VirtualMemory.readChar(PM.init, 5 + i) != ' ') {
                            char x = VirtualMemory.readChar(PM.init, 5 + i);
                            Memory.writePipe(x, i);
                            written++;
                        } else if (VirtualMemory.readChar(PM.init, 5) == ' ') {
                            Memory.writePipe((char) 0, 0);

                        }
                    }
                }
                else if (nchar > 4 - e.eQueue.size()) {
                    Memory.writePipe((char)written, 0);
                    Memory.printRawRam();
                    //System.out.println(a);
                }
                else if (nchar==1) {
                    System.out.println("I've nothing to read. Closing this pipe ;(");
                    Memory.writePipe((char)0, 0);
                    Memory.printRawRam();
                    //System.out.println(a);
                }
               /* else if (buffer.capacity()>4){
                    break;
                }*/
            }
            System.out.print("Writing to a pipe:  \n");

            Memory.printRawRam();
        }
        return written;
    }

    public static void readFromPipe()
    {
        int read = 0;
        for (PipeQueue e : Pipes) {

            if (1>0) {
                System.out.println("Enter how many chars you'd like to read: \n");
                Scanner c = new Scanner (System.in);
                int d = c.nextInt();
                if(d>4){
                    do{
                        System.out.println("The number must be between 0 and 4! ");
                        d=c.nextInt();
                    } while(d>4);
                }
                for (int i = 0; i < d; i++) { //buffer.size()
                    char x = VirtualMemory.readChar(PM.init, 5+i);
                    Memory.writePipe(x, 5+i);
                    read++;
                }
                if (VirtualMemory.readChar(PM.init, 5) == ' ') {
                    Memory.writePipe((char) 0, d + 5);
                    Memory.printRawRam();
                    System.out.println("This pipe's empty");
                    //break;
                }

            }

        }
        System.out.print("Reading from a pipe: \n ");
        Memory.printRawRam();
    }
    //System.out.print("Reading Pipe:  ");



    public static void closePipe () {
        Memory.clearPIPE();
        System.out.print("The pipe has been closed\n");
        Memory.printRawRam();

    }

    public static void close (int [] pdesc){
        //pdesc =null;
        System.out.print("The descriptor's closed\n");
    }

    public static int Pipe(int[] pdesc) {
        Random rand = new Random();
        int read = rand.nextInt(4);
        int write = rand.nextInt(4);

        while (descriptor.contains(write)) {
            write = rand.nextInt(4);
        }
        write = pdesc[1];
        descriptor.add(write);

        while (descriptor.contains(read)) {
            read = rand.nextInt(4);
        }
        read = pdesc[0];
        descriptor.add(read);

        PipeQueue queueToAdd = new PipeQueue(write, read);
        Pipes.add(queueToAdd);
        System.out.println("Pipe's created: [ ] \n");
//        System.out.println("[1]");
//        System.out.println(read);
       // System.out.println(queueToAdd);
        return 0;
    }

    public static void WritePipe() throws FileNotFoundException {
        int[] pdesc = new int[2];
        IPC a = new IPC();
        a.Pipe(pdesc);
        Pipe(pdesc);
        Vector<Character> be = new Vector<Character>(4);
        Vector<Character> en = new Vector<Character>(4);

        for(int i = 0; i < 5; i++){
            be.add('a');
        }


        if (be.size()>4){
            be.setSize(4);
        }
        if(be.size() ==1){
            System.out.println("No pipe");
        }
        for (int i= 1; i<6; i++) {
            // Memory.writePipe(be.get(), i);
        }
        a.writeToPipe(pdesc[1], be, 1);
        //System.out.println("Zapis do pipe" + be);
        // close(pdesc[1]);
        // en.add(c);
    }

    public static void ReadPipe() {
        //VirtualMemory.readChar(PCB proces, int adress);
        //Memory.writePipe(b, adress);
        Vector<Character> en = new Vector<Character>(4);
   /* P5.pipe.readFromPipe(pdesc[0], en, 1);
    P5.pipe.readFromPipe(pdesc[0], en, 1);
    P5.pipe.readFromPipe(pdesc[0], en, 1);
    P5.pipe.readFromPipe(pdesc[0], en, 1);*/

        System.out.println(en);
        Memory.readPipe(1);
   /* Memory.readPipeFrame();
    P6.pipe.close(pdesc[0]);
    P5.pipe.close(pdesc[1]);*/
        Memory.printRawRam();


    }


//
    public static void main(String[] arg) throws FileNotFoundException {
        //  Scan();
        int[] pdesc = new int[2];
        IPC a = new IPC();
        //  a.Pipe(pdesc);
        a.Pipe(pdesc);
        Vector<Character> be = new Vector<Character>(4);
        Vector<Character> en = new Vector<Character>(4);

        for(int i = 0; i < 5; i++){
            be.add('a');
        }


        if (be.size()>4){
            be.setSize(4);
        }
        for (int i= 1; i<6; i++) {
            // Memory.writePipe(be.get(), i);
        }
        a.writeToPipe(pdesc[1], be, 1);
        //System.out.println("Zapis do pipe" + be);
        // close(pdesc[1]);
        // en.add(c);
        // a.readFromPipe(pdesc[0], en, 0);
        //System.out.println(en);
        //  close(pdesc[0]);
        readFromPipe();

        a.closePipe();
//
    }
}
