package processor;



import java.util.LinkedList;
import java.util.*;

public class Scheduler
{   PCB pcb = new PCB("dod", 19, 10, "mata",12);

    LinkedList<PCB> Heap= new LinkedList<PCB>();
    int CurrTime; // zmienna inkrementujaca sie  co wykonany rozka, marcin ciechan ja obsluzy
    int min_granularity = 4; // minimalny czas, na jaki proces moze otryzmać procesor
    int period = 20; // okres, ustalany z gory, czas w jakim powinien każdy proces otrzymać procesor
    PCB running; // tej zmiennej bedziemy przypisywać PCB procesu dzialajacego
    PCB dummy;// zmienna ktora bedzie wykonywana gdy wielkosc kolejki bedzie rowna 0 (jest to rozkaz, który skacze sam dos siebie, ciechan go ma u siebie)

    LinkedList<PCB> sort(LinkedList<PCB> Heap) // funkcja sortujaca kolejke po CurrTime, CurrTime= vruntime +Currtime
    {

        Collections.sort(Heap,Comparator.comparingLong(PCB::getCurrTime));

        return Heap;
    }

    void PrioToWeight(PCB pcb) //funkcja zwracajca przeliczone priorytety na wagi
    {
        int weight = pcb.getCurrPriority() *10;
        pcb.setWeight(weight);
    }


    int HeapWeight() // funkcja zwracajaca sume wag w kolejce
    {
        int sum=0;
        for(int i=0;i<=Heap.size();i++) {
            int weight = Heap.get(i).getWeight();
            sum+=weight;
        }
        return sum;
    }

    PCB min (){ // funkcja wywolujaca process o najmniejszym czasie wirtualnym

        return Heap.get(0);
    }

    LinkedList<PCB> Insert (PCB pcb){ // dodaje proces do kolejki i ja sortuje

        PrioToWeight(pcb);
        Heap.add(pcb);
        return sort(Heap);
    }


    LinkedList<PCB> Delete (PCB pcb) // pytanie czy tu caly blok kontrolny procesu dostane czy tylko jego id
    {
        Heap.remove(pcb);
        return sort(Heap);
        /// wysylanie procesu do dominika mozna by tu zmiane stanu zrobic
    }

    void TimeSlice(PCB pcb){ // wylicza time slice dla procesu, tylko nie wiem czy
        int timeslice= period*pcb.getWeight()/HeapWeight();
        if (timeslice<min_granularity)
            timeslice=min_granularity;
        pcb.setCurrTime(timeslice);
    }

    void VirtualTime(PCB pcb) // zakładam,ze przy zmianie stanu na aktywny w baseTime wpiszemy czas, podczas ktorego process dostał procesor
    {
        int weight = pcb.getWeight();
        int currTime=CurrTime;
        int baseTime=pcb.getBaseTime();
        int VirtualTime=pcb.getVirtualTime();
        int NewVirtualTime=(currTime-baseTime)*weight+VirtualTime;
        pcb.setVirtualTime(NewVirtualTime);
        //return NewVirtualTime; // nie wiem czy ten return bedzie potrzebny czy nie przerzuce sie na void
    }



    void check()
    {    if(running.getState()== PCB.StateList.Terminated && (Heap.size()==0)) // jesli proces został wykonany oraz wielkosc kolejki rowna sie zero to wykonujemy dummy
    {  Delete(running);
        running=dummy; }// sprawdz czy heap.size gdy jet puste daje nam 0

        if(running.getState() == PCB.StateList.Terminated) //  jesli aktywny proces sie wykonał to go usunąć
        { Delete(running);}
        else { VirtualTime(running); }

        PCB min=min();

        if(running.getVirtualTime()>min.getVirtualTime()) //jesli czas wirtualny dzialajacego jest mniejszy od minimalnego
        {
            running.setState(PCB.StateList.Ready);
            running=min;
            TimeSlice(running);
            running.setBaseTime(CurrTime);
            running.setState(PCB.StateList.Running);;
        }
        sort(Heap);
    }}
  
