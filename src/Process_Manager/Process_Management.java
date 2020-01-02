package Process_Manager;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Process_Management {

    // Lista procesów
    public List<PCB> ProcessList=new LinkedList<PCB>();

    // Proces główny
    public PCB init;

    public Process_Management(){

        // Inicjalizacja głównego procesu przy starcie rodzica
        init=new PCB("init","");
        init.setState(init.State.Ready);
        ProcessList.add(init);
    }

    // Nowy proces
    public PCB fork(PCB parent, String name, int priority, String fName){

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
        //ustawianie czasu i priorytetu
        return process;
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
                }
            }
        }
        ProcessList.remove(process);
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
