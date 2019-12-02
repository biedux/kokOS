package Process_Manager;

import java.util.LinkedList;
import java.util.List;

public class Process_Management {

    // Lista procesów
    public List<PCB> ProcessList=new LinkedList<PCB>();

    // Proces główny
    public PCB init;

    public Process_Management(){

        // Inicjalizacja głównego procesu przy starcie rodzica
        init=new PCB("init",0,0,"",0);
        init.setState(init.State.Ready);
        ProcessList.add(init);
    }

    // Nowy proces
    public PCB fork(PCB parent, int priority, int tim, String fName, int fSize){

        // Nowy proces
        PCB process=new PCB(parent.getName(),priority,tim,fName,fSize);
        process.setParentID(parent.getID());
        parent.ChildrenList.add(process);
        return process;
    }

    // Zabijanie procesu
    public void kill(PCB process){
        process.setState(PCB.StateList.Terminated);
        for(PCB child : process.ChildrenList){
            child.setParentID(0);
        }
        for(PCB processFromList:ProcessList){
            if(process.getID()==processFromList.getID()){
                ProcessList.remove(processFromList);
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
