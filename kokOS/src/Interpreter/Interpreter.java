package Interpreter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

enum Rozkazy {
    AD, // AD A B           dodaje rejestr A + rejestr B
    AX, // AX A 3           dodaje liczbę do A
    SB, // SB A B           odejmuje od rejestru A rejestr B
    SX, // SX A 7           odejmuje od rejestru A LICZBE
    ML, // ML A B           mnozy A razy B
    MX, // MX A 5           mnozy A razy liczbe
    MV, // MV A B           kopiuje B do A, ADRESY TEZ
    MO, // MO A 4           umieszcza wartosc w rejestrze

    JP, // JP 100           skok do adresu
    JZ, // JZ 100           skok do adresu jesli po poprzedniej operacji jest gdzies zero

    CP, // CP idrodzica, priorytet, plik.txt  tworzy proces
    KP, // KP idprocesu             zabija proces

    CF, // CF nazwa username         tworzy plik o nazwie nazwa
    OF, // OF nazwa username        otwiera plik o nazwie nazwa
    WF, // WF nazwa username tresc   do pustego pliku nazwa wpisuje tresc
    AF, // AF nazwa username tresc   dopisuje tresc na koniec pliku nazwa
    DF, // DF nazwa username         usuwa plik nazwa
    RF, // RF nazwa username         wyswietla zawartosc pliku nazwa
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
    Process_Management processManagement;
    IPC ipc;
    Disc disc;
    OpenFileTab open_file_table;
    private int counter;
    private int id;
    private boolean czyDoRamu;
    int ktoryArgDoRamu;
    private int memoryCounter = 0;


    Interpreter() throws FileNotFoundException {
        ram = Shell.getRam();
        virtual = Shell.getVirtual();
        processManagement = Shell.getPM();
        disc = Shell.getDisc();
        ipc = Shell.getIpc();
        open_file_table = Shell.getOpenFileTab();
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
                if(czytany.equals('[')){                //tylko do pracy na adresach
                    czyDoRamu = true;
                    ktoryArgDoRamu = i;
                    counter++;
                    do{
                        czytany = virtual.readChar(pcb,counter);
                        arg += czytany;
                        counter++;

                    }while(czytany!=']');

                    arg=usunOstatni(arg);

                }else {
                    arg += czytany;
                    counter++;
                }

            } while(czytany!=' ');
            arg=usunOstatni(arg);
            System.out.println("Pobrano argument: " + arg);

            args.add(arg);

            arg = "";
        }
        pcb.setCounter(counter);
        return args;
    }

    public void makeStep() throws Exception {
        //System.out.println("weszlismy do make step (interpreter)");
        //Memory.printRawRam();
        //VirtualMemory.printPageTable();

        processManagement.scheduler.check();
        System.out.println("id wykonywanego procesu w obecnym : "+processManagement.scheduler.running.getID());
        this.pcb= processManagement.scheduler.running;

        this.counter = this.pcb.getCounter();
        this.id = this.pcb.getID();

        boolean step = true;
        List<String> args = new LinkedList<>();
        String cmd = "";
        Character tmp = '.';

        //czytanie rozkazu
        while (step) {
            do {
                tmp = virtual.readChar(pcb,counter);
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


            if (czyDoRamu) {                                             //wlacza sie tylko jesli operujemy na adresach
                int ktoryDoRamu = Integer.parseInt(String.valueOf(ktoryArgDoRamu));

                if (ktoryDoRamu == 0) {                          //wstawianie do ramu

                    if (args.get(1).equals("A")) {
                        memoryCounter = Memory.writeNumMC(pcb.getAX(), Integer.parseInt(args.get(0)));//WKLADAMY A
                    } else if (args.get(1).equals("B")) {
                        memoryCounter = Memory.writeNumMC(pcb.getBX(), Integer.parseInt(args.get(0)));//B
                    } else if (args.get(1).equals("C")) {
                        memoryCounter = Memory.writeNumMC(pcb.getCX(), Integer.parseInt(args.get(0)));//C
                    } else if (args.get(1).equals("D")) {
                        memoryCounter = Memory.writeNumMC(pcb.getDX(), Integer.parseInt(args.get(0)));//D

                    } else
                        memoryCounter = Memory.writeNumMC(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(0)));//przypadek jak jest liczba wkładana
                }
                if (ktoryDoRamu == 1) {                                     //pobieranie z ramu
                    //arg 0 to rejestr gdzie wsadzimy cos z ramu
                    //arg 1 to liczzba, ktora siedzi w ramie
                    if (args.get(0).equals("A")) {
                        pcb.setAX(Memory.readNumMC(Integer.parseInt(args.get(1))));
                    } else if (args.get(0).equals("B")) {
                        pcb.setBX(Memory.readNumMC(Integer.parseInt(args.get(1))));
                    } else if (args.get(0).equals("C")) {
                        pcb.setCX(Memory.readNumMC(Integer.parseInt(args.get(1))));
                    } else if (args.get(0).equals("D")) {
                        pcb.setDX(Memory.readNumMC(Integer.parseInt(args.get(1))));
                    }

                }
            }

            else if (args.get(0).equals("A")) {
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

            args = getArgs(counter, 1);
            if(    pcb.getAX()==0 || pcb.getBX()==0 || pcb.getCX()==0 || pcb.getDX()==0  ){
                String temp = args.get(0);
                int result = Integer.parseInt(temp);
                pcb.setCounter(result);
            }

        }

        //CP
        if(    (cmd.equals("CP"))  ){
            args = getArgs(counter, 3);
            int prio = Integer.parseInt(args.get(1));
            //PCB.setCountProcess(PCB.getCountProcess()-1);
            PCB New=processManagement.fork(processManagement.findPCB(pcb.getName()), args.get(0), prio, args.get(2));
        }

        //CS = create shorter
        if(    (cmd.equals("CS"))  ){
            args = getArgs(counter, 2);
            int prio = Integer.parseInt(args.get(1));
            //PCB.setCountProcess(PCB.getCountProcess()-1);
            PCB New=processManagement.fork(processManagement.findPCB(pcb.getName()), args.get(0), prio, "");
        }

        //KP
        if(    (cmd.equals("KP"))  ){
            args = getArgs(counter, 1);
            processManagement.kill(processManagement.findPCB(args.get(0)));
            if(processManagement.ProcessList.size()==1){
                VirtualMemory.nowyproces(processManagement.init);
            }
        }

        
        //CF
        if (   (cmd.equals("CF"))  ) {

            args = getArgs(counter, 2);
            String name = args.get(0);
            String userName = args.get(1);
            disc.createFile(name, userName);

        }

        //OF
        if(    (cmd.equals("OF"))  ){

            args = getArgs(counter, 2);
            String name = args.get(0);
            disc.openFile(name, processManagement.findPCB(args.get(1)));

        }

        //WF
        if(    (cmd.equals("WF"))  ){

            args = getArgs(counter, 3);
            String name = args.get(0);
            String user = args.get(1);
            String data = args.get(2);
            disc.writeFile(name, user, data);
        }

        //AF
        if(    (cmd.equals("AF"))  ) {
            args = getArgs(counter, 3);
            String name = args.get(0);
            String user = args.get(1);
            String newData = args.get(2);
            disc.appendFile(name, user, newData);
        }

        //DF
        if(    (cmd.equals("DF"))  ){
            args = getArgs(counter, 2);
            String name = args.get(0);
            String user = args.get(1);
            System.out.println("przed lukaszem");
            disc.deleteFile(name, user);
        }

        //RF
        if(    (cmd.equals("RF"))  ){
            args = getArgs(counter, 3);
            String name = args.get(0);
            String user = args.get(1);
            int amount = Integer.parseInt(args.get(2));
            System.out.println("\nz pliku przeczytano: "+disc.readFile(name, user, amount));
        }

        //FC
        if(    (cmd.equals("FC"))  ){
            args = getArgs(counter, 2);
            String name = args.get(0);
            disc.closeFile(name, processManagement.findPCB(args.get(1)));
        }


        //PC
        if(    (cmd.equals("PC"))  ){
            IPC.closePipe();
        }

        //PW
        if(    (cmd.equals("PW"))  ){
            IPC.WritePipe();
        }

        //PR
        if(    (cmd.equals("PR"))  ){
            IPC.readFromPipe();
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
    }
}
