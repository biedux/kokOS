package Interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Process_Management PM = new Process_Management();
        Disc dysk = new Disc();
        Memory ram = PM.RAM;
        VirtualMemory virt = PM.vm;
        PCB P1 = PM.fork(PM.init, "P1", 1, "proces.txt");
        PCB P2 = PM.fork(PM.init, "P2", 10, "jjj.txt");
        PCB P3 = PM.fork(PM.init, "P3", 7, "cos.txt");
        PCB P4 = PM.fork(PM.init, "P4", 5, "cos.txt");
        Interpreter interpreter = new Interpreter(ram, virt, PM, dysk);
        interpreter.processManagement.scheduler.check();
        interpreter.processManagement.showAllProcesses();

        int i = 0;
        while (true) {
            i++;
            Scanner scan = new Scanner(System.in);
            int cos = scan.nextInt();

            if (cos == 0) {
                System.out.println();
                System.out.println("----------------------------------------------------------");
                System.out.println("---------------- KROK " + i + " ----------------");
                System.out.println("----------------------------------------------------------");
                System.out.println();

                //Memory.printRawRam();
                //VirtualMemory.printPageFile();
                interpreter.makeStep();
                interpreter.processManagement.showAllProcesses();
            }
            if(cos == 9){
                break;
            }

        }
        System.out.println("Koniec programu");


         /*
            for(int j = 1; j<100; j++){
                System.out.println();
                System.out.println("----------------------------------------------------------");
                System.out.println("---------------- KROK "+j + " ----------------");
                Memory.printRawRam();
                VirtualMemory.printPageFile();
                interpreter.makeStep();
                interpreter.processManagement.showAllProcesses();

            }*/


    }
}

            //po 32 kroku sie wysypuje, damian nie obsluguje ladowania takich bydlakow


            //TimeUnit.SECONDS.sleep(1);
            //interpreter.processManagement.scheduler.showProTimes(interpreter.processManagement.scheduler.running);

//        PCB P4=PM.fork(PM.init,"P4",120,"test.txt");
//        interpreter.processManagement.showAllProcesses();
//        System.out.println("");
//        PCB P5=PM.fork(PM.init,"P5",120,"test.txt");
//        interpreter.processManagement.showAllProcesses();



//        int[] pdesc = new int[2];
//
//        P5.pipe.close(pdesc[0]);
//        P5.pipe.Pipe(pdesc);
//
//        Vector<Byte> be = new Vector<Byte>(4);
//        Vector<Byte> en = new Vector<Byte>(4);
//
//        //jaki proces tworzy dzieccko pid dziecka
//        //PCB
//
//        //Plik z programem z wiadomoscia
//        Byte b = '3';
//        Byte c = 'i';
//        be.add(b);
//        en.add(c);
//
//        P5.pipe.writeToPipe(pdesc[1], be, 1);
//
//        PCB P6=PM.fork(P5,"P6",120,"");
//        P6.pipe.close(pdesc[1]);
//
//        P5.pipe.readFromPipe(pdesc[0], en, 1);
//        P5.pipe.readFromPipe(pdesc[0], en, 1);
//        System.out.println(en);
//        P6.pipe.close(pdesc[0]);
//        P5.pipe.close(pdesc[1]);
//
//        PCB P7=PM.fork(PM.init,"P7",120,"");
//        System.out.println("Drzewo procesow:");
//        PM.showTree(PM.init);



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