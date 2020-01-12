package Interpreter;
//
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

//
//public class Main {
//    public static void main(String[] args) throws Exception {
//

import java.io.FileNotFoundException;
/*

 */
public class Main{
public static void main(String[] arg) throws FileNotFoundException {
        //  Scan();
//        int[] pdesc = new int[2];
//        IPC a = new IPC();
//        a.Pipe(pdesc);
//        Vector<Character> be = new Vector<Character>(4);
//        Vector<Character> en = new Vector<Character>(4);
//
//        for(int i = 0; i < 5; i++){
//        be.add('a');
//        }
//
//
//        if (be.size()>4){
//        be.setSize(4);
//        }
//        for (int i= 1; i<6; i++) {
//        // Memory.writePipe(be.get(), i);
//        }
//        a.writeToPipe(pdesc[1], be, 1);
//        //System.out.println("Zapis do pipe" + be);
//        // close(pdesc[1]);
//        // en.add(c);
//        a.readFromPipe(pdesc[0], en, 0);
//        System.out.println(en);
//        //  close(pdesc[0]);
//        a.closePipe();
////
//                }
//        }


            Interpreter interpreter = new Interpreter();
            Process_Management PM = Interpreter.getPM();
            Disc dysk = Interpreter.getDisc();
            Memory ram = Interpreter.getRam();
            VirtualMemory virt = Interpreter.getVirtual();
            //PCB P2 = PM.fork(PM.init, "P2", 1, "jjj.txt");
            //PCB P5 = PM.fork(PM.init, "P5", 3, "otwieramy.txt");
            //PCB P2 = PM.fork(PM.init, "P2", 10, "jjj.txt");
            //PCB P3 = PM.fork(PM.init, "P3", 7, "cos.txt");
            //PCB P4 = PM.fork(PM.init, "P4", 5, "cos.txt");

        PCB P6 = PM.fork(PM.init, "P6", 3, "pliki.txt");


        PM.scheduler.check();
            PM.showAllProcesses();
///*
//        int i = 0;
//        while (true) {
//            i++;
//            Scanner scan = new Scanner(System.in);
//            int cos = scan.nextInt();
//
//            if (cos == 0) {
//                System.out.println();
//                System.out.println("----------------------------------------------------------");
//                System.out.println("---------------- KROK " + i + " ----------------");
//                System.out.println("----------------------------------------------------------");
//                System.out.println();
//
//                //Memory.printRawRam();
//                //VirtualMemory.printPageFile();
//                interpreter.makeStep();
//                interpreter.processManagement.showAllProcesses();
//            }
//            if(cos == 9){
//                break;
//            }
//
//        }
//        System.out.println("Koniec programu");
//
//*/

            //SZYBCKIE DRUKOWANIE
            for (int j = 1; j <= 100; j++) {

                    System.out.println();
                    System.out.println("----------------------------------------------------------");
                    System.out.println("---------------- KROK " + j + " ----------------");
                    //Memory.printRawRam();
                    //VirtualMemory.printPageFile();
                try
                {
                    interpreter.makeStep();
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
                    PM.showAllProcesses();
              PM.showTree(P6);
                    //interpreter.disc.printDisc();
                    //interpreter.disc.ListDirectory();
                    Memory.printRawRam();
                    //VirtualMemory.printQueue();


            }

        System.out.println("-----------------Koniec programu MISIE--------------");

    }
}

