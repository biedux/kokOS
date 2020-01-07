package Process_Manager;

import Processor.*;
import VirtualMemory.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Process_Management {
    public Scheduler scheduler=new Scheduler();

    public VirtualMemory vm=new VirtualMemory();

    // Lista procesów
    public List<PCB> ProcessList=new LinkedList<PCB>();

    // Proces główny
    public PCB init;

    public Process_Management(){

        // Inicjalizacja głównego procesu przy starcie rodzica
        init=new PCB("init","");
        init.setState(init.State.Ready);
        init.setID(0);
        PCB.setCountProcess(PCB.getCountProcess()-1);
        scheduler.dummy=init;
        ProcessList.add(init);
    }

    public String readFile(String fName) throws FileNotFoundException {
        String programm="";
        if (fName != "") {
            File file = new File(fName);
            Scanner prog = new Scanner(file);
            while(prog.hasNextLine()){
                programm+=prog.nextLine()+" ";
            }
        }
        return programm;
    }

    // Nowy proces
    public PCB fork(PCB parent, String name, int priority, String fName) throws FileNotFoundException {
        // Nowy proces
        PCB process=new PCB(name,fName);
        process.setParentID(parent.getID());
        process.setState(process.State.Ready);
        process.setPriority(priority);
        ProcessList.add(process);
        parent.ChildrenList.add(process);
        if(parent.getID()!=0){
            parent.setState(PCB.StateList.Waiting);
        }

        process.setCode(readFile(fName));

        VirtualMemory.nowyproces(process);

        scheduler.Insert(process);
        return process;
    }

    public PCB findPCB(String pName){
        PCB P=new PCB("","");
        for(PCB proc:ProcessList){
            if(pName==proc.getName()){
                P=proc;
            }
        }
        return P;
    }

    // Zabijanie procesu (usuniecie procesu i przekazanie dzieci rodzicowi)

    //Dwie opcje, zabija dzieci lub nie (pgit=null)
    public void kill(PCB process){
        for(PCB parent:ProcessList){
            if(process.getParentID()==parent.getID()){
                parent.ChildrenList.remove(process);
                if(process.getState() == PCB.StateList.Waiting){
                    for(PCB child : process.ChildrenList){
                        child.setParentID(process.getParentID());
                        parent.ChildrenList.add(child);
                    }
                    process.setState(PCB.StateList.Terminated);
                    //process release RAM i virtual
                }
            }
        }
        ProcessList.remove(process);
    }

    private List<PCB> Killed=new LinkedList<PCB>();

    public void setRemoved(PCB process){
        for(PCB parent:ProcessList){
            if(process.getParentID()==parent.getID()){
                parent.ChildrenList.remove(process);
                if(process.getState() == PCB.StateList.Waiting || process.getChildrenList()!=null) {
                    for (PCB child : process.ChildrenList) {
                        setRemoved(child);
                    }
                }
            }
        }
        process.setState(PCB.StateList.Terminated);
        //process release RAM i virtual
        Killed.add(process);
    }

    public void killByGroup(PCB process){
        setRemoved(process);
        for(PCB proc:Killed){
            ProcessList.remove(proc);
        }
    }

    // Wait

    //bez argumentu - wykonanie ostatniego procesu
    //argument - wykonanie procesu o danym id
    //dziecko się wykona - staje się zombie
    public boolean wait(PCB proc){
        for(PCB child:proc.ChildrenList){
            if(child.getState() != PCB.StateList.Terminated){
                return false;
            }
        }
        return true;
    }



    // Drzewo procesów
    public void showTree(PCB proc){
        if(ProcessList.contains(proc)){
            String lev="";
            if(proc.getID()!=0){
                lev+="+-";
                for(int i=proc.getParentID();i>=0;i--){
                    if(proc.getParentID()>1) i--;
                    lev+="--";
                }
            }
            System.out.println(lev+proc.getName());
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
