package Interpreter;

import java.util.Random;
import java.util.Vector;

public class Pipe{
    //Pipe
    public final Vector<PipeQueue> Pipes = new Vector<>();
    public final Vector<Integer> descriptor = new Vector<>();

    public Vector<PipeQueue> getPipes() {
        return Pipes;
    }

    public Vector<Integer> getDescriptor() {
        return descriptor;
    }

    public int Pipe(int[] pdesc) { //funkcja tworzaca pipe

        Random rand = new Random();
        int read = rand.nextInt(32);
        int write = rand.nextInt(32);//|| 32

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

    public int writeToPipe(int fd, Vector<Byte> buffer, Integer nbyte) { //funkcja zapisujaca do pipe
        int written = 0;
        for (PipeQueue e : Pipes) {
            if (e.descW == fd) {

                if (nbyte < 64 - e.eQueue.size())
                    for (int i = 0; i < nbyte; i++) { //perhaps 16, do sprawdzenia
                        Byte a = buffer.get(i); //buffer- segment danych w procesie, do którego majá zostac przekazane dane
                        //tablica stronic, przydziel pamiec
                        //e.eQueue.add(a);
                        written++;
                    }
                else if (nbyte > 64 - e.eQueue.size()) {
                    //zwracana wartosc w RAM na 1 miejscu 0
                }
            }
            //System.out.print("Writing to a pipe:  ");

            //return nbyte
        }
        return written;
    }

    public int readFromPipe ( int fd, Vector<Byte > buffer, Integer nbyte)
    { //funkcja - szok i niedowierzanie - odczytujaca z pipea
        //(int fd, Vector<Bytes> buffer, int nbyte);
        int read = 0;
        for (PipeQueue e : Pipes) { //iterowanie again
            if (e.descR == fd) {
                for (int i = 0; i < nbyte; i++) {
                    if (!e.eQueue.isEmpty()) {
                        //Byte a = e.eQueue.remove();
                       // buffer.add(a);
                        read++;
                    } else if (e.eQueue.isEmpty()) {
                        //zapis 0 i elo
                    }
                    break;
                }
            }
        }
        //System.out.print("Reading from a Pipe:  ");
        return read; //to int or not to int??
    }

    public static void closePipe () { //zamykanie pipea

        System.out.print("The pipe has been closed... R.I.P.");

    }

    public static void close ( int i){
        System.out.print("The descriptor is closed\n");
    }
}
