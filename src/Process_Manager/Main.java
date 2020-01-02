package Process_Manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import Pipes.*;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        //PM.showAllProcesses();
        //System.out.println(" ");
        //PM.kill(P3);
        //PM.showAllProcesses();
        //System.out.println(" ");

        //PM.showAllProcesses();
        //System.out.println(" ");
        //PM.kill(P2);
        //PCB P7=PM.fork(P3,"P7",120,"");
        //PM.showAllProcesses();

        Process_Management PM=new Process_Management();

        /*
        int[] pdesc = new int[2];
        IPC a = new IPC();
        a.Pipe(pdesc);

        Vector<Byte> be = new Vector<Byte>(4);
        Vector<Byte> en = new Vector<Byte>(4);

        //jaki proces tworzy dzieccko pid dziecka
        //PCB

        Byte b = '3';
        Byte c = 'i';
        be.add(b);
        en.add(c);

        a.writeToPipe(pdesc[1], be, 1);
        a.readFromPipe(pdesc[0], en, 1);
        a.readFromPipe(pdesc[0], en, 1);
        System.out.println(en);
        close(pdesc[0]);
        close(pdesc[1]);
         */

        PCB P1=PM.fork(PM.init, "P1",120,"");
        PCB P2=PM.fork(PM.init,"P2",120,"");
        PCB P3=PM.fork(PM.init,"P3",120,"");
        PCB P4=PM.fork(P1,"P4",120,"");
        PCB P5=PM.fork(P2,"P5",120,"");
        PCB P8=PM.fork(P4,"P8",120,"");
        PCB P9=PM.fork(P8,"P9",120,"");

        int[] pdesc = new int[2];

        P5.pipe.close(pdesc[0]);
        P5.pipe.Pipe(pdesc);

        Vector<Byte> be = new Vector<Byte>(4);
        Vector<Byte> en = new Vector<Byte>(4);

        //jaki proces tworzy dzieccko pid dziecka
        //PCB

        //Plik z programem z wiadomoscia
        Byte b = '3';
        Byte c = 'i';
        be.add(b);
        en.add(c);

        P5.pipe.writeToPipe(pdesc[1], be, 1);

        PCB P6=PM.fork(P5,"P6",120,"");
        P6.pipe.close(pdesc[1]);

        P5.pipe.readFromPipe(pdesc[0], en, 1);
        P5.pipe.readFromPipe(pdesc[0], en, 1);
        System.out.println(en);
        P6.pipe.close(pdesc[0]);
        P5.pipe.close(pdesc[1]);

        PCB P7=PM.fork(PM.init,"P7",120,"");
        System.out.println("Drzewo procesow:");
        PM.showTree(PM.init);
        //PM.kill(P5);
        //System.out.println("\nDrzewo procesow:");
        //PM.showTree(PM.init);
        //PM.showAllProcesses();
    }
}
