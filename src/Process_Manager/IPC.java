package com.company;
import java.io.FileNotFoundException;
import java.util.*;


public class IPC {
    public final Vector<PipeQueue> Pipes = new Vector<>();
    public final Vector<Integer> descriptor = new Vector<>();

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

                    for (int i = 0; i < 4; i++) { //buffer.size()

                        if (VirtualMemory.readChar(PM.init, 5+i) == ' '){
                            break;
                        }
                        char x = VirtualMemory.readChar(PM.init, 5+i);
                        Memory.writePipe(x, i);
                        written++;
                    }
                }
                else if (nchar > 4 - e.eQueue.size()) {
                    Memory.writePipe((char)written, 0);
                    Memory.printRawRam();
                    //System.out.println(a);
                    }
               /* else if (buffer.capacity()>4){
                    break;
                }*/
            }
            System.out.print("Writing to a pipe:  ");

            Memory.printRawRam();
        }
        return written;
    }

    public int readFromPipe (int fd, Vector<Character> buffer, int nchar)
    {
        int read = 0;
        for (PipeQueue e : Pipes) {
            if (e.descR == fd) {
                    if (!e.eQueue.isEmpty()) {
                        for (int i = 0; i < buffer.size(); i++) {

                            Memory.readPipe(i);
                            Character a = e.eQueue.remove();
                           buffer.add(a);
                            while (!buffer.isEmpty())
                            Memory.writePipe(a, i);
                            //Memory.readPipe(i);
                            read++;
                        }
                    } else if (e.eQueue.isEmpty()) {
                        Memory.writePipe((char) 0, 0);
                    }
                    break;
                }
            }
        System.out.print("Reading Pipe:  ");
        return nchar;
    }


    public static void closePipe () {
        Memory.clearPIPE();
        System.out.print("The pipe has been closed\n");
        Memory.printRawRam();

    }

    public static void close (int pdesc){
        //pdesc =null;
        System.out.print("The descriptor is closed\n");
    }

    public int Pipe(int[] pdesc) {
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
        return 0;
    }

public static void WritePipe() throws FileNotFoundException {
        //VirtualMemory.writeChar(PCB process, int address, char data);
    int pdesc [] = new int[2];
    IPC a = new IPC();
    a.Pipe(pdesc);
   // P5.pipe.close(pdesc[0]);
    //P5.pipe.Pipe(pdesc);
    Vector<Character> be = new Vector<Character>(4);
    Scanner myObj = new Scanner (System.in);
    System.out.println("Enter data");
    char b = myObj.next().charAt(0);


    //System.out.println(b);
    be.add(b);

    //P5.pipe.writeToPipe(pdesc[1], be, 4);
    System.out.println(be);
    //PCB P6 = PM.fork(P5, "P6", 120, " ");
   // P6.pipe.close(pdesc[1]);
    Memory.printRawRam();
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



 public static void main(String[] arg) throws FileNotFoundException {
      //  Scan();
        int[] pdesc = new int[2];
        IPC a = new IPC();
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
        a.readFromPipe(pdesc[0], en, 0);
        System.out.println(en);
      //  close(pdesc[0]);
        a.closePipe();
//
     Memory.writePipe('x', 4);
     Memory.printRawRam();
 }
}