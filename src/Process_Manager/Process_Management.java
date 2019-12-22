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

    // Zabijanie procesu
    public void kill(PCB process){
        if(process.getState() == PCB.StateList.Waiting){
            for(PCB child : process.ChildrenList){
                child.setParentID(process.getParentID());
            }
            process.setState(PCB.StateList.Terminated);
        }
        ProcessList.remove(process);
    }

    // Wyświetlanie wszystkich procesów
    public void showAllProcesses(){
        for(PCB process:ProcessList){
            process.printProcessInfo();
        }
    }
}
