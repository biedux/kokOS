package Pipes;
import java.util.*;
import java.io.IOException;


public class IPC {
    public final Vector<PipeQueue> Pipes = new Vector<>();
    public final Vector<Integer> descriptor = new Vector<>();

    public IPC() {
    }

    public Vector<PipeQueue> getPipes() {
        return Pipes;
    }

    public Vector<Integer> getDescriptor() {
        return descriptor;
    }

    public int writeToPipe(int fd, Vector<Byte> buffer, Integer nbyte) { //funkcja zapisujaca do pipe
        int written = 0;
        for (PipeQueue e : Pipes) {
            if (e.descW == fd) {

                if (nbyte < 64 - e.eQueue.size())
                    for (int i = 0; i < nbyte; i++) {
                        Byte a = buffer.get(i);
                                             //buffer- segment danych w procesie, do którego majá zostac przekazane dane
                        e.eQueue.add(a);
                        writePipe(char data = nbyte, int adresLogicz =);
                        written++;
                    }
                else if (nbyte > 64 - e.eQueue.size()) {
                    char data=(char)written;
                    writePipe(char data = written, int adresLogicz =0);
                    System.out.println(a);
                }
            }
            System.out.print("Writing to a pipe:  ");

        }
        return written;
    }

    public int readFromPipe(int fd, Vector<Byte> buffer, Integer nbyte) {
        int read = 0;
        for (PipeQueue e : Pipes) {
            if (e.descR == fd) {
                for (int i = 0; i < nbyte; i++) {
                    if (!e.eQueue.isEmpty()) {
                        Byte a = e.eQueue.remove();
                        buffer.add(a);
                        read++;
                    } else if (e.eQueue.isEmpty()) {
                        writePipe(char data =0, int adresLogicz =0);

                    }
                    break;
                }
            }
        }
        System.out.print("Reading from a Pipe:  ");
        return read;
    }

    public static void closePipe() {
        clearPIPE();
        //System.out.print("The pipe has been closed... R.I.P.");

    }

    public static void close(int pdesc[]) {
        System.out.print("The descriptor is closed");
    }

    public int Pipe(int[] pdesc) { //funkcja tworzaca pipe

        Random rand = new Random();
        int read = rand.nextInt(32);
        int write = rand.nextInt(32);
        while (descriptor.contains(write)) {
            write = rand.nextInt(32);
        }
        write = pdesc[1];
        descriptor.add(write);
        while (descriptor.contains(read)) {
            read = rand.nextInt(32);
        }
        read = pdesc[0];
        descriptor.add(read);

        PipeQueue queueToAdd = new PipeQueue(write, read);
        Pipes.add(queueToAdd);
        return 0;
    }

    public static void WritePipe() {
        int[] pdesc = new int[2];
        P5.pipe.close(pdesc[0]);
        P5.pipe.Pipe(pdesc);
        Vector<Byte> be = new Vector<Byte>(4);
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter data");
        Byte b = myObj.nextByte();
        Byte c = myObj.nextByte();
        Byte d = myObj.nextByte();
        Byte e = myObj.nextByte(); //nie wiem czy by tego nie wrzucic w jakas petle i pytac usera ile znakow chce zapisac
        //System.out.println(b);
        be.add(b);
        be.add(c);
        be.add(d);
        be.add(e);

        P5.pipe.writeToPipe(pdesc[1], be, 4);
        System.out.println(be);

        PCB P6 = PM.fork(P5, "P6", 120, "");
        P6.pipe.close(pdesc[1]); //tez nie wiem czy to jest na pewno dobrze rozdzielone, imo tak


    }

    public static void ReadPipe() {
        WritePipe();
        P5.pipe.readFromPipe(pdesc[0], en, 1);
        P5.pipe.readFromPipe(pdesc[0], en, 1);
        P5.pipe.readFromPipe(pdesc[0], en, 1);
        P5.pipe.readFromPipe(pdesc[0], en, 1); //to tez chyba w petle powedruje
        System.out.println(en);
        readPipe(int adresLogicz); //or
        readPipeFrame();
        //fcja wyswietlanie ram

        P6.pipe.close(pdesc[0]);
        P5.pipe.close(pdesc[1]);
    }


//    public static void main(String[] arg) {
//        int[] pdesc = new int[2];
//        IPC a = new IPC();
//        a.Pipe(pdesc);
//
//        Vector<Byte> be = new Vector<Byte>(4);
//        Vector<Byte> en = new Vector<Byte>(4);
//
//        //jaki proces tworzy dzieccko pid dziecka
//        //PCB
//
//        Byte b = '3';
//        Byte c = 'i';
//        be.add(b);
//        en.add(c);
//
//        a.writeToPipe(pdesc[1], be, 1);
//        a.readFromPipe(pdesc[0], en, 1);
//        a.readFromPipe(pdesc[0], en, 1);
//        System.out.println(en);
//        close(pdesc[0]);
//        close(pdesc[1]);
//
//    }
}
