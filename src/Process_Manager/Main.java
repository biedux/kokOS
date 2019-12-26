package Process_Manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Process_Management PM=new Process_Management();
        PCB P1=PM.fork(PM.init, "P1",120,"");
        PCB P2=PM.fork(PM.init,"P2",120,"");
        PCB P3=PM.fork(P1,"P3",120,"");
        //PM.showAllProcesses();
        //System.out.println(" ");
        //PM.kill(P3);
        //PM.showAllProcesses();
        //System.out.println(" ");
        PCB P4=PM.fork(P1,"P4",120,"");
        PCB P5=PM.fork(P2,"P5",120,"");
        //PM.showAllProcesses();
        //System.out.println(" ");
        //PM.kill(P2);
        //PCB P7=PM.fork(P3,"P7",120,"");
        //PM.showAllProcesses();
        PCB P6=PM.fork(P5,"P6",120,"");
        PCB P7=PM.fork(PM.init,"P7",120,"");
        System.out.println("Drzewo procesow:");
        PM.showTree(PM.init);
        PM.kill(P5);
        System.out.println("\nDrzewo procesow:");
        PM.showTree(PM.init);
    }
}
