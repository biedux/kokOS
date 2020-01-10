package Interpreter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

//todo
//sprawdzic skoki, pliki, CP, pipe
//zrobic zapis i odczytanie z adresu -> funckje od stempla
//programy dokonczyc
//matematyczne

enum Rozkazy {
    //nie bedzie tych enumów, ale chwilowo fajnie sie to tu rozpisuje
    AD, // AD A B           dodaje rejestr A + rejestr B
    AX, // AX A 3           dodaje liczbę do A
    SB, // SB A B           odejmuje od rejestru A rejestr B
    SX, // SX A 7           odejmuje od rejestru A LICZBE
    ML, // ML A B           mnozy A razy B
    MX, // MX A 5           mnozy A razy liczbe
    MV, // MV A B           kopiuje B do A
    MO, // MO A 4           umieszcza wartosc w rejestrze

    JP, // JP 100           skok do adresu
    JZ, // JZ 100           skok do adresu jesli po poprzedniej operacji jest gdzies zero

    CP, // CP               tworzy proces
    KP, // KP               zabija proces

    CF, // CF nazwa         tworzy plik o nazwie nazwa
    OF, // OF nazwa         otwiera plik o nazwie nazwa
    WF, // WF nazwa tresc   do pustego pliku nazwa wpisuje tresc
    AF, // AF nazwa tresc   dopisuje tresc na koniec pliku nazwa
    DF, // DF nazwa         usuwa plik nazwa
    RF, // RF nazwa         wyswietla zawartosc pliku nazwa
    FC, // FC nazwa         zamyka plik File Close

    PC, // PC               close pipe
    PW, // PW               write to pipe
    PR, // PW               read from pipe

    HL, // HL               koniec działania programu

}

 class Interpreter {
     Memory ram;
     VirtualMemory virtual;
     PCB pcb;
     static Process_Management processManagement;
     Pipe pipe;
     Disc disc;
     //IPC ipc;
     private int counter;
     private int id;

     Interpreter() {}

     Interpreter(Memory ram, VirtualMemory virt, Process_Management pm, Disc dysk) {
         this.ram = ram;
         this.virtual = virt;
         this.processManagement = pm;
         this.disc = dysk;
     }
public static String usunOstatni(String str) {
    String result = null;
    if ((str != null) && (str.length() > 0)) {
        result = str.substring(0, str.length() - 1);
    }
    return result;
}

public ArrayList<String> getArgs(int counter, int ilePobieram) {
    ArrayList<String> args = new ArrayList<>();
    String arg = "";
    Character czytany = '.';
    for(int i =0; i<ilePobieram; i++){
        do{
            czytany = virtual.readChar(pcb,counter);
            arg+=czytany;
            counter++;

        } while(czytany!=' ');
        System.out.println("Pobrano argument: " + arg);
        arg=usunOstatni(arg);
        args.add(arg);
        arg = "";
    }
    pcb.setCounter(counter);
    return args;
}

public void makeStep() {
        processManagement.scheduler.check();
         this.pcb=this.processManagement.scheduler.running;
         this.counter = this.pcb.getCounter();
         this.id = this.pcb.getID();

         boolean step = true;
         List<String> args = new LinkedList<>();
         int czytany = 0;
         String cmd = "";
         Character tmp = '.';

    //czytanie rozkazu
         while (step) {
                 do {
                     tmp = virtual.readChar(pcb,counter);
                     //System.out.println("pobrano znak w rozkazie"+tmp);
                     cmd += tmp;
                     counter++;
                 } while (tmp != ' ');

             cmd = usunOstatni(cmd);
             System.out.println("Pobrano rozkaz:   " + cmd);
             pcb.setCounter(counter);
             step = false;
             }
         //}
         //AD
         if(    (cmd.equals("AD"))  ){

             args = getArgs(counter, 2);
                         if(args.get(0).equals("A") && args.get(1).equals("A")) { pcb.setAX(pcb.getAX()+pcb.getAX()); }
                    else if(args.get(0).equals("A") && args.get(1).equals("B")) { pcb.setAX(pcb.getAX()+pcb.getBX()); }
                    else if(args.get(0).equals("A") && args.get(1).equals("C")) { pcb.setAX(pcb.getAX()+pcb.getCX()); }
                    else if(args.get(0).equals("A") && args.get(1).equals("D")) { pcb.setAX(pcb.getAX()+pcb.getDX()); }

                    else if(args.get(0).equals("B") && args.get(1).equals("A")) { pcb.setBX(pcb.getBX()+pcb.getAX()); }
                    else if(args.get(0).equals("B") && args.get(1).equals("B")) { pcb.setBX(pcb.getBX()+pcb.getBX()); }
                    else if(args.get(0).equals("B") && args.get(1).equals("C")) { pcb.setBX(pcb.getBX()+pcb.getCX()); }
                    else if(args.get(0).equals("B") && args.get(1).equals("D")) { pcb.setBX(pcb.getBX()+pcb.getDX()); }

                    else if(args.get(0).equals("C") && args.get(1).equals("A")) { pcb.setCX(pcb.getCX()+pcb.getAX()); }
                    else if(args.get(0).equals("C") && args.get(1).equals("B")) { pcb.setCX(pcb.getCX()+pcb.getBX()); }
                    else if(args.get(0).equals("C") && args.get(1).equals("C")) { pcb.setCX(pcb.getCX()+pcb.getCX()); }
                    else if(args.get(0).equals("C") && args.get(1).equals("D")) { pcb.setCX(pcb.getCX()+pcb.getDX()); }

                    else if(args.get(0).equals("D") && args.get(1).equals("A")) { pcb.setDX(pcb.getDX()+pcb.getAX()); }
                    else if(args.get(0).equals("D") && args.get(1).equals("B")) { pcb.setDX(pcb.getDX()+pcb.getBX()); }
                    else if(args.get(0).equals("D") && args.get(1).equals("C")) { pcb.setDX(pcb.getDX()+pcb.getCX()); }
                    else if(args.get(0).equals("D") && args.get(1).equals("D")) { pcb.setDX(pcb.getDX()+pcb.getDX()); }

         }
         //AX
         if(    (cmd.equals("AX"))  ) {
             args = getArgs(counter, 2);

             if (args.get(0).equals("A")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setAX(pcb.getAX() + result);

             } else if (args.get(0).equals("B")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setBX(pcb.getBX() + result);

             } else if (args.get(0).equals("C")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setCX(pcb.getCX() + result);

             } else if (args.get(0).equals("D")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setDX(pcb.getDX() + result);

             }
         }
         //SB
         if(    (cmd.equals("SB"))  ) {
             args = getArgs(counter, 2);
                  if(args.get(0).equals("A") && args.get(1).equals("A")) { pcb.setAX(pcb.getAX()-pcb.getAX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("B")) { pcb.setAX(pcb.getAX()-pcb.getBX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("C")) { pcb.setAX(pcb.getAX()-pcb.getCX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("D")) { pcb.setAX(pcb.getAX()-pcb.getDX()); }

             else if(args.get(0).equals("B") && args.get(1).equals("A")) { pcb.setBX(pcb.getBX()-pcb.getAX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("B")) { pcb.setBX(pcb.getBX()-pcb.getBX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("C")) { pcb.setBX(pcb.getBX()-pcb.getCX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("D")) { pcb.setBX(pcb.getBX()-pcb.getDX()); }

             else if(args.get(0).equals("C") && args.get(1).equals("A")) { pcb.setCX(pcb.getCX()-pcb.getAX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("B")) { pcb.setCX(pcb.getCX()-pcb.getBX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("C")) { pcb.setCX(pcb.getCX()-pcb.getCX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("D")) { pcb.setCX(pcb.getCX()-pcb.getDX()); }

             else if(args.get(0).equals("D") && args.get(1).equals("A")) { pcb.setDX(pcb.getDX()-pcb.getAX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("B")) { pcb.setDX(pcb.getDX()-pcb.getBX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("C")) { pcb.setDX(pcb.getDX()-pcb.getCX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("D")) { pcb.setDX(pcb.getDX()-pcb.getDX()); }

         }
         //SX
         if(    (cmd.equals("SX"))  ) {
             args = getArgs(counter, 2);

             if (args.get(0).equals("A")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setAX(pcb.getAX() - result);

             } else if (args.get(0).equals("B")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setBX(pcb.getBX() - result);

             } else if (args.get(0).equals("C")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setCX(pcb.getCX() - result);

             } else if (args.get(0).equals("D")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setDX(pcb.getDX() - result);

             }
         }
         //ML
         if(    (cmd.equals("ML"))  ) {
             args = getArgs(counter, 2);
             if(args.get(0).equals("A") && args.get(1).equals("A")) { pcb.setAX(pcb.getAX()*pcb.getAX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("B")) { pcb.setAX(pcb.getAX()*pcb.getBX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("C")) { pcb.setAX(pcb.getAX()*pcb.getCX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("D")) { pcb.setAX(pcb.getAX()*pcb.getDX()); }

             else if(args.get(0).equals("B") && args.get(1).equals("A")) { pcb.setBX(pcb.getBX()*pcb.getAX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("B")) { pcb.setBX(pcb.getBX()*pcb.getBX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("C")) { pcb.setBX(pcb.getBX()*pcb.getCX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("D")) { pcb.setBX(pcb.getBX()*pcb.getDX()); }

             else if(args.get(0).equals("C") && args.get(1).equals("A")) { pcb.setCX(pcb.getCX()*pcb.getAX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("B")) { pcb.setCX(pcb.getCX()*pcb.getBX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("C")) { pcb.setCX(pcb.getCX()*pcb.getCX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("D")) { pcb.setCX(pcb.getCX()*pcb.getDX()); }

             else if(args.get(0).equals("D") && args.get(1).equals("A")) { pcb.setDX(pcb.getDX()*pcb.getAX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("B")) { pcb.setDX(pcb.getDX()*pcb.getBX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("C")) { pcb.setDX(pcb.getDX()*pcb.getCX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("D")) { pcb.setDX(pcb.getDX()*pcb.getDX()); }

         }
         //MX
         if(    (cmd.equals("MX"))  ) {
             args = getArgs(counter, 2);

             if (args.get(0).equals("A")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setAX(pcb.getAX() * result);

             } else if (args.get(0).equals("B")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setBX(pcb.getBX() * result);

             } else if (args.get(0).equals("C")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setCX(pcb.getCX() * result);

             } else if (args.get(0).equals("D")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setDX(pcb.getDX() * result);

             }

         }
        //MV
         if(    (cmd.equals("MV"))  ) {
             args = getArgs(counter, 2);

                  if(args.get(0).equals("A") && args.get(1).equals("B")) { pcb.setAX(pcb.getBX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("C")) { pcb.setAX(pcb.getCX()); }
             else if(args.get(0).equals("A") && args.get(1).equals("D")) { pcb.setAX(pcb.getDX()); }

             else if(args.get(0).equals("B") && args.get(1).equals("A")) { pcb.setBX(pcb.getAX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("C")) { pcb.setBX(pcb.getCX()); }
             else if(args.get(0).equals("B") && args.get(1).equals("D")) { pcb.setBX(pcb.getDX()); }

             else if(args.get(0).equals("C") && args.get(1).equals("A")) { pcb.setCX(pcb.getAX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("B")) { pcb.setCX(pcb.getBX()); }
             else if(args.get(0).equals("C") && args.get(1).equals("D")) { pcb.setCX(pcb.getDX()); }

             else if(args.get(0).equals("D") && args.get(1).equals("A")) { pcb.setDX(pcb.getAX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("B")) { pcb.setDX(pcb.getBX()); }
             else if(args.get(0).equals("D") && args.get(1).equals("C")) { pcb.setDX(pcb.getCX()); }

         }
         //MO
         if(    (cmd.equals("MO"))  ) {

             args = getArgs(counter, 2);

             if (args.get(0).equals("A")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setAX(result);

             } else if (args.get(0).equals("B")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setBX(result);

             } else if (args.get(0).equals("C")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setCX(result);

             } else if (args.get(0).equals("D")) {
                 String temp = args.get(1);
                 int result = Integer.parseInt(temp);
                 pcb.setDX(result);

             }
         }
         //JP
         if(    (cmd.equals("JP"))  ){
             args = getArgs(counter, 1);
             String temp = args.get(0);
             int result = Integer.parseInt(temp);
             pcb.setCounter(result);
         }
         //JZ
         if(    (cmd.equals("JZ"))  ){
             if(    pcb.getAX()==0 || pcb.getBX()==0 || pcb.getCX()==0 || pcb.getDX()==0  ){
                 args = getArgs(counter, 1);
                 String temp = args.get(0);
                 int result = Integer.parseInt(temp);
                 pcb.setCounter(result);
             }

         }

         //CP
         if(    (cmd.equals("CP"))  ){
             try {
                 args = getArgs(counter, 3);
                 int prio = Integer.parseInt(args.get(1));
                 PCB.setCountProcess(PCB.getCountProcess()-1);
                 PCB New=processManagement.fork(processManagement.findPCB(pcb.getName()), args.get(0), prio, args.get(2));
             } catch(Exception e){}
         }

         //KP
         if(    (cmd.equals("KP"))  ){
             args = getArgs(counter, 1);
             PCB toKill=processManagement.findPCB(args.get(0));
             toKill=processManagement.findById(toKill.getID()-1);
             processManagement.kill(toKill);
             if(processManagement.ProcessList.size()==1){
                 VirtualMemory.nowyproces(processManagement.init);
             }
         }


         //pliki beda jeszcze zgrane z prawami dostepu

         //CF
         if (   (cmd.equals("CF"))  ) {
             try {
                 args = getArgs(counter, 2);
                 String name = args.get(0);
                 String userName = args.get(1);
                 disc.createFile(name, userName);
             }catch(Exception e){};
    }

         //OF
         if(    (cmd.equals("OF"))  ){
             try{
             args = getArgs(counter, 1);
             String name = args.get(0);
             disc.openFile(name, processManagement.findPCB(args.get(1)));
             }catch(Exception e){};
         }

         //WF
         if(    (cmd.equals("WF"))  ){
             try {
                 args = getArgs(counter, 2);
                 String name = args.get(0);
                 String data = args.get(1);
                 disc.writeFile(name, data);
             }catch(Exception e){};
         }

         //AF
         if(    (cmd.equals("AF"))  ) {
             try {
             args = getArgs(counter, 2);
             String name = args.get(0);
             String newData = args.get(1);
             disc.appendFile(name, newData);
             }catch(Exception e){};
         }

         //DF
         if(    (cmd.equals("DF"))  ){
             try {
                 args = getArgs(counter, 1);
                 String name = args.get(0);
                 disc.deleteFile(name);
             }catch(Exception e){};
         }

         //RF
         if(    (cmd.equals("RF"))  ){
             args = getArgs(counter, 1);
             String name = args.get(0);
             disc.readFile(name);
         }

         //FC
         if(    (cmd.equals("FC"))  ){
             try {
                 args = getArgs(counter, 2);
                 String name = args.get(0);
                 disc.closeFile(name, processManagement.findPCB(args.get(1)));
             }catch(Exception e){};
         }


         //PC
         if(    (cmd.equals("PC"))  ){


         }

         //PW
         if(    (cmd.equals("PW"))  ){

         }

         //PR
         if(    (cmd.equals("PR"))  ){


         }


         //HL
         if(    (cmd.equals("HL"))  ){
             System.out.println("Koniec procesu "+processManagement.scheduler.running.getName());
                processManagement.kill(processManagement.scheduler.running);
                //processManagement.scheduler.Delete(processManagement.scheduler.running);
             processManagement.scheduler.showHeap();
             if(processManagement.ProcessList.size()==1){
                 VirtualMemory.nowyproces(processManagement.init);
             }
             }
    processManagement.scheduler.check();
     }
}