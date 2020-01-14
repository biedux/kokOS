package Interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;


import java.io.FileNotFoundException;

public class Main {
    private static void logo()
    {
        System.out.println("                                                            \n" +
                "                                           ```              \n" +
                "                           `..         `:osys-              \n" +
                "                            :sys+:`   -+sso:                \n" +
                "                             `/syys:`/oso:``...`            \n" +
                "                          ``````:sso/+ss+:ossyyyso/-        \n" +
                "                      -/+osoooo//:+soooooooo++/::-.`        \n" +
                "                    `-......--:/++ooooooooosoo++:.          \n" +
                "                          `..:/+++++o++++++:-:/+oso.        \n" +
                "                    `.` `-///:-:+/+++++yo+++/.    .-        \n" +
                "         `/ss+-   :oo-  ./+:-``:/+++osys+-+++/.             \n" +
                "         ` `:oo+-oo::/oo+++/` .+ooo/.-`   `/ooo.            \n" +
                "     .:/oo+++:osoo++/:. `o+-  .oso:        `+ss:            \n" +
                "        .-/++++ooo++///-.-+   `os+`         `os:            \n" +
                "      .+o++//+/+++so.-/+o+-   `+o`           -s`            \n" +
                "     -so/+/-`/++//o.   `-os- `/+-             -             \n" +
                "     +- :+:  :s+` `/:     -+`-+/                            \n" +
                "        .+`  `/-   `:/      `/o.                            \n" +
                "         `          `:/     -+/                             \n" +
                "                     `//   `/+-                             \n" +
                "                      `+-  .+o.                             \n" +
                "                       -+. :++`                             \n" +
                "                        //`/+s-                             \n" +
                "                        -+-/ss.                             \n" +
                "                        -o+oso/:                            \n" +
                "                      .:/sosssso/`                          \n" +
                "                    `:/ossssssss+-`                         \n" +
                "            ````....-----------------.....```               ");

        System.out.println("oooo                  oooo          .oooooo.    .oooooo..o \n" +
                "`888                  `888         d8P'  `Y8b  d8P'    `Y8 \n" +
                " 888  oooo   .ooooo.   888  oooo  888      888 Y88bo.      \n" +
                " 888 .8P'   d88' `88b  888 .8P'   888      888  `\"Y8888o.  \n" +
                " 888888.    888   888  888888.    888      888      `\"Y88b \n" +
                " 888 `88b.  888   888  888 `88b.  `88b    d88' oo     .d8P \n" +
                "o888o o888o `Y8bod8P' o888o o888o  `Y8bood8P'  8\"\"88888P'  \n" +
                "____________________________________________________________\n");
    }
    public static void main(String[] arg) throws FileNotFoundException {
        logo();
        Shell shell = new Shell();
        shell.start();
    }
}

/*
        System.out.println("                                                            \n" +
                "                                           ```              \n" +
                "                           `..         `:osys-              \n" +
                "                            :sys+:`   -+sso:                \n" +
                "                             `/syys:`/oso:``...`            \n" +
                "                          ``````:sso/+ss+:ossyyyso/-        \n" +
                "                      -/+osoooo//:+soooooooo++/::-.`        \n" +
                "                    `-......--:/++ooooooooosoo++:.          \n" +
                "                          `..:/+++++o++++++:-:/+oso.        \n" +
                "                    `.` `-///:-:+/+++++yo+++/.    .-        \n" +
                "         `/ss+-   :oo-  ./+:-``:/+++osys+-+++/.             \n" +
                "         ` `:oo+-oo::/oo+++/` .+ooo/.-`   `/ooo.            \n" +
                "     .:/oo+++:osoo++/:. `o+-  .oso:        `+ss:            \n" +
                "        .-/++++ooo++///-.-+   `os+`         `os:            \n" +
                "      .+o++//+/+++so.-/+o+-   `+o`           -s`            \n" +
                "     -so/+/-`/++//o.   `-os- `/+-             -             \n" +
                "     +- :+:  :s+` `/:     -+`-+/                            \n" +
                "        .+`  `/-   `:/      `/o.                            \n" +
                "         `          `:/     -+/                             \n" +
                "                     `//   `/+-                             \n" +
                "                      `+-  .+o.                             \n" +
                "                       -+. :++`                             \n" +
                "                        //`/+s-                             \n" +
                "                        -+-/ss.                             \n" +
                "                        -o+oso/:                            \n" +
                "                      .:/sosssso/`                          \n" +
                "                    `:/ossssssss+-`                         \n" +
                "            ````....-----------------.....```               ");

        System.out.println("oooo                  oooo          .oooooo.    .oooooo..o \n" +
                "`888                  `888         d8P'  `Y8b  d8P'    `Y8 \n" +
                " 888  oooo   .ooooo.   888  oooo  888      888 Y88bo.      \n" +
                " 888 .8P'   d88' `88b  888 .8P'   888      888  `\"Y8888o.  \n" +
                " 888888.    888   888  888888.    888      888      `\"Y88b \n" +
                " 888 `88b.  888   888  888 `88b.  `88b    d88' oo     .d8P \n" +
                "o888o o888o `Y8bod8P' o888o o888o  `Y8bood8P'  8\"\"88888P'  ");
*/






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


//            Interpreter interpreter = new Interpreter();
//            Process_Management PM = Interpreter.getPM();
//            Disc dysk = Interpreter.getDisc();
//            Memory ram = Interpreter.getRam();
//            VirtualMemory virt = Interpreter.getVirtual();
//PCB P2 = PM.fork(PM.init, "P2", 1, "jjj.txt");
//PCB P5 = PM.fork(PM.init, "P5", 3, "otwieramy.txt");
//PCB P2 = PM.fork(PM.init, "P2", 10, "jjj.txt");
//PCB P3 = PM.fork(PM.init, "P3", 7, "cos.txt");
//PCB P4 = PM.fork(PM.init, "P4", 5, "cos.txt");

//        PCB P6 = PM.fork(PM.init, "P6", 3, "pliki.txt");
//
//
//        PM.scheduler.check();
//            PM.showAllProcesses();
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

//            //SZYBCKIE DRUKOWANIE
//            for (int j = 1; j <= 100; j++) {
//
//                    System.out.println();
//                    System.out.println("----------------------------------------------------------");
//                    System.out.println("---------------- KROK " + j + " ----------------");
//                    //Memory.printRawRam();
//                    //VirtualMemory.printPageFile();
//                try
//                {
//                    interpreter.makeStep();
//                }
//                catch(Exception e)
//                {
//                    System.out.println(e.getMessage());
//                }
//                    PM.showAllProcesses();
//              PM.showTree(P6);
//                    //interpreter.disc.printDisc();
//                    //interpreter.disc.ListDirectory();
//                    Memory.printRawRam();
//                    //VirtualMemory.printQueue();
//
//
//            }
//
//        System.out.println("-----------------Koniec programu WSZYSTKO POSZLO ZGODNIE Z PLANEM--------------");
//
//    }
//}
//
