package Processor;



import java.util.LinkedList;
import java.util.*;
import Process_Manager.*;

public class Scheduler {
   /* public void setRunning(PCB Running) {
        Running = running;
    }

    */


    LinkedList<PCB> Heap = new LinkedList<PCB>();

    long CurrTime;// zmienna inkrementujaca sie  co wykonany rozka, marcin ciechan ja obsluzy
    long min_granularity = 1; // minimalny czas, na jaki proces moze otryzmać procesor
    long period = 20; // okres, ustalany z gory, czas w jakim powinien każdy proces otrzymać procesor
    //PCB running = new PCB("proces2","filename2" );

    // pm.fork("p6", 4,"f6"); // tej zmiennej bedziemy przypisywać PCB procesu dzialajacego
    //PCB running=p7;

    public PCB dummy =new PCB("init","init.txt");
    public PCB running = dummy;

    LinkedList<PCB> sort(LinkedList<PCB> Heap) // funkcja sortujaca kolejke po CurrTime, CurrTime= vruntime +Currtime
    {

        Collections.sort(Heap, Comparator.comparingLong(PCB::getVirtualTime));

        return Heap;
    }

    void PrioToWeight(PCB pcb) //funkcja zwracajca przeliczone priorytety na wagi
    {
        int weight = pcb.getPriority() * 1; // przyklad
        pcb.setWeight(weight);
    }


    long HeapWeight() // funkcja zwracajaca sume wag w kolejce
    {
        long sum = 0;
        for (int i = 0; i < Heap.size(); i++) {
            long weight;
            weight = Heap.get(i).getWeight();
            sum += weight;
        }
        return sum;
    }

    PCB min() { // funkcja wywolujaca process o najmniejszym czasie wirtualnym

        return Heap.get(0);
    }

    public LinkedList<PCB> Insert(PCB pcb) { // dodaje proces do kolejki i ja sortuje

        PrioToWeight(pcb);
        pcb.setVirtualTime(0);
        Heap.add(pcb);
        return sort(Heap);
    }

    public LinkedList<PCB> InsertFirst(PCB pcb) { // dodaje proces do kolejki i ja sortuje

        PrioToWeight(pcb);
        pcb.setVirtualTime(999);
        Heap.add(pcb);
        return sort(Heap);
    }

    public Scheduler() {
        dummy.setState(PCB.StateList.Running);
    }


    public LinkedList<PCB> Delete(PCB pcb) // pytanie czy tu caly blok kontrolny procesu dostane czy tylko jego id
    {
        Heap.remove(pcb);
        return sort(Heap);
        /// wysylanie procesu do dominika mozna by tu zmiane stanu zrobic
    }

    void TimeSlice(PCB pcb) { // wylicza time slice dla procesu, tylko nie wiem czy
        if(Heap.size()==0){
            long timeslice = period * pcb.getWeight() / HeapWeight();
            if (timeslice < min_granularity)
                timeslice = min_granularity;
            pcb.setTimeSlice((int) timeslice);
        }

    }

    void VirtualTime(PCB pcb) // zakładam,ze przy zmianie stanu na aktywny w baseTime wpiszemy czas, podczas ktorego process dostał procesor
    {
        long weight = pcb.getWeight();
        // long currTime=CurrTime;
        // long baseTime=pcb.getBaseTime();
        long VirtualTime = pcb.getVirtualTime();
        //long NewVirtualTime=(currTime-baseTime)*weight+VirtualTime;
        long NewVirtualTime = 1 * weight + VirtualTime;
        pcb.setVirtualTime((int) NewVirtualTime);
        // sort(Heap);
        //return NewVirtualTime; // nie wiem czy ten return bedzie potrzebny czy nie przerzuce sie na void
    }

    public void showProTimes(PCB proc){
        System.out.println(proc.getName()+" timeslice "+proc.getTimeSlice()+ " virtual "+proc.getVirtualTime());
    }

    public void check() {    //CurrTime=System.nanoTime();

        running.setTimeSlice(running.getTimeSlice() - 1);
        PCB min = min();

        if (running.getState() == PCB.StateList.Terminated && (Heap.size() == 0)) // jesli proces został wykonany oraz wielkosc kolejki rowna sie zero to wykonujemy dummy
        {
            Delete(running);
            running = dummy;
        }// sprawdz czy heap.size gdy jet puste daje nam 0

        else {
            if (running.getState() == PCB.StateList.Terminated) //  jesli aktywny proces sie wykonał to go usunąć
            {
                Delete(running);
                running = min;
                TimeSlice(running);
                //running.setBaseTime(CurrTime);
                running.setState(PCB.StateList.Running);

            } else {
                VirtualTime(running);

                sort(Heap);
                PCB min1 = min();

                if (running.getTimeSlice() <= 0) {
                    running.setState(PCB.StateList.Ready);
                    running = min1;
                    TimeSlice(running);
                    //running.setBaseTime(CurrTime);
                    running.setState(PCB.StateList.Running);

                } else if (running.getVirtualTime() > min1.getVirtualTime()) //jesli czas wirtualny dzialajacego jest mniejszy od minimalnego
                {
                    running.setState(PCB.StateList.Ready);
                    running = min1;
                    TimeSlice(running);
                    //running.setBaseTime(CurrTime);
                    running.setState(PCB.StateList.Running);

                }
            }
        }
    }
}

