package Interpreter;

//import Processor.*;
//import VirtualMemory.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Process_Management {

    public Memory RAM;
    public VirtualMemory vm = new VirtualMemory(RAM);

    // Lista procesów
    public List<PCB> ProcessList=new LinkedList<PCB>();

    // Proces główny
    public PCB init;

    public Scheduler scheduler=Shell.getScheduler();

    public Process_Management() throws Exception {
        // Inicjalizacja głównego procesu przy starcie rodzica
        init=scheduler.dummy;
        scheduler.InsertFirst(init);
        ProcessList.add(init);
        init.setCode(readFile("init.txt"));
        VirtualMemory.nowyproces(init);
    }

    public String readFile(String fName) throws Exception {

        String programm="";
        if (fName != "") {
            File file = new File(fName);
            Scanner prog = new Scanner(file);
            while(prog.hasNextLine()){
                programm+=prog.nextLine()+" ";
            }
            return programm;
        }
        return programm;
    }

    // Nowy proces
    public PCB fork(PCB parent, String name, int priority, String fName) throws Exception {
        boolean possible=true;

        PCB.setCountProcess(PCB.getCountProcess()+1);
        PCB process=new PCB(name,fName);
        // Nowy proces
        for(PCB p:ProcessList){
            if(p.getName().equals(name)){
                possible=false;
            }
        }
        if(99>priority || priority>140){
            PCB.setCountProcess(PCB.getCountProcess()-1);
            throw new Exception("Nieprawidlowy priorytet");
        }
        else if(possible==true) {
            try{
                process.setParentID(parent.getID());
                process.setState(process.State.Ready);
                process.setPriority(priority);
                ProcessList.add(process);
                parent.ChildrenList.add(process);
                System.out.println("Id nowego procesu: "+process.getID());
                this.scheduler.Insert(process);
                process.setCode(readFile(fName));
                VirtualMemory.nowyproces(process);
                return process;
            }
            catch(Exception e) {
                System.out.println(e.getLocalizedMessage());
                parent.ChildrenList.remove(process);
                scheduler.Delete(process);
                ProcessList.remove(process);
                PCB.setCountProcess(PCB.getCountProcess()-1);
            }
        }
        else if(possible==false){
            PCB.setCountProcess(PCB.getCountProcess()-1);
            throw new Exception("Nie mozna utworzyc procesu o danej nazwie");
        }
        return null;
    }

    public PCB getInit(){
        return this.init;
    }

    public PCB findPCB(String pName) throws Exception{
        boolean exists=false;
        for(PCB proc:ProcessList){
            if(pName.equals(proc.getName())){
                exists=true;
                return proc;
            }
        }
        if(exists==false){
            throw new Exception("Nie ma procesu o tej nazwie");
        }
        return null;
    }

    public PCB findById(int ID) throws Exception{
        boolean exists=false;
        for(PCB proc:ProcessList){
            if(proc.getID()==ID){
                return proc;
            }
        }
        if(exists==false){
            exists=true;
            throw new Exception("Nie ma procesu o tej nazwie");
        }
        return null;
    }

    // Zabijanie procesu (usuniecie procesu i przekazanie dzieci rodzicowi)

    //Dwie opcje, zabija dzieci lub nie (pgit=null)

    //skok do inita w ostatnim ifie
    public void kill(PCB process) throws Exception{
        if(process.getID()==0){
            throw new Exception("Nie mozna usunac procesu dummy");
        }
        else{
            boolean possible=false;
            for(PCB proc:ProcessList){
                if(proc.equals(process)){
                    possible=true;
                }
            }
            if(possible==true){
                for (PCB parent : ProcessList) {
                    if (process.getParentID() == parent.getID()) {
                        parent.ChildrenList.remove(process);
                        for (PCB child : process.ChildrenList) {
                            child.setParentID(process.getParentID());
                            parent.ChildrenList.add(child);
                        }
                        process.setState(PCB.StateList.Terminated);

                    }
                }
                ProcessList.remove(process);
                scheduler.Delete(process);
                VirtualMemory.usunproces(process);
                scheduler.check();
                //Shell.open_file_table.removeFile(process,Shell.open_file_table.findFile(process));
            }
            if(possible==false){
                throw new Exception("Nie mozna zabic procesu ktory nie istnieje");
            }
        }
    }

    private List<PCB> toKill=new LinkedList<PCB>();

    public void killByGroup(PCB process) throws Exception {
        if(process.ChildrenList!=null){
            for(PCB child:process.ChildrenList){
                toKill.add(child);
                if(child.ChildrenList!=null){
                    for(PCB great:child.ChildrenList){
                        toKill.add(great);
                        if(great.ChildrenList!=null){
                            for(PCB greatgrand:great.ChildrenList){
                                toKill.add(greatgrand);
                            }
                        }
                    }
                }
            }
        }
        for(PCB p:toKill){
            kill(p);
        }
        kill(process);
        //        kill(process);
//        for(PCB child:process.ChildrenList){
//            killByGroup(child);
//            kill(child);
//        }
    }

    String lev="";
    // Drzewo procesów
    public void showTree(PCB proc){
//        System.out.println(proc.getName()+" (ID: "+proc.getID()+")");
//        System.out.println("|");
//        int i=0;
//        int j=0;
//        int k=0;
//        for(PCB p:proc.ChildrenList){
//            System.out.println("+--->"+p.getName()+" (ID: "+p.getID()+")");
//                for(PCB child:p.ChildrenList){
//                    System.out.println("|    |");
//                    lev+="|    +--->";
//                    System.out.println(lev+child.getName()+" (ID: "+child.getID()+")");
//                    if(child.ChildrenList.size()!=0){
//                        System.out.println("|         |");
//                    }
//                    lev="";
//                    i++;
//                    if(i==proc.ChildrenList.size() && proc.ChildrenList.size()!=0){
//                        System.out.println("|");
//                    }
//                    j=0;
//                    for(PCB grand:child.ChildrenList){
//                        lev="|         +--->";
//                        System.out.println(lev+grand.getName()+" (ID: "+grand.getID()+")");
//                        if(grand.ChildrenList.size()!=0){
//                            System.out.println("|              |");
//                        }
//                        lev="";
//                        j++;
//                        if(j==proc.ChildrenList.size() && child.ChildrenList.size()!=0){
//                            System.out.println("|");
//                        }
//                        k=0;
//                        for(PCB greatgrand:grand.ChildrenList){
//                            lev="|              +--->";
//                            System.out.println(lev+greatgrand.getName()+" (ID: "+greatgrand.getID()+")");
//                            if(greatgrand.ChildrenList.size()!=0){
//                                System.out.println("|              |");
//                            }
//                            lev="";
//                            k++;
//                            if(k==proc.ChildrenList.size() && grand.ChildrenList.size()!=0){
//                                System.out.println("|");
//                            }
//                        }
//                    }
//                }
//            }


        String children="";
        String parent="";
        if(ProcessList.contains(proc)){
            String lev="";
            for(PCB p:proc.ChildrenList){
                children+=(p.getID()+" ");
            }
            if(children=="") children+="Brak";
            if(proc.getID()!=0) parent+=Integer.toString(proc.getParentID());
            else parent+="Proces dummy nie ma rodzica ";
            System.out.println(lev+"ID rodzica: "+parent+" Nazwa procesu: "+proc.getName()+" ID dzieci procesu: "+children);
            //proc.printProcessInfo();
            for(PCB child:proc.ChildrenList){
                showTree(child);
            }
        }
    }

    // Wyświetlanie wszystkich procesów
    public void showAllProcesses(){
        for(PCB process:ProcessList){
            process.printProcessInfo();
        }
    }
}
