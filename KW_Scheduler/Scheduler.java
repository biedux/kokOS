package processor;



import java.util.LinkedList;
import java.util.*;

public class Scheduler
{
    Process_Management PM=new Process_Management();
   /* public void setRunning(PCB Running) {
        Running = running;
    }

    */

    Process_Management pm= new Process_Management();
    LinkedList<PCB> Heap= new LinkedList<PCB>();

    long CurrTime;// zmienna inkrementujaca sie  co wykonany rozka, marcin ciechan ja obsluzy
    int min_granularity = 4; // minimalny czas, na jaki proces moze otryzmać procesor
    int period = 20; // okres, ustalany z gory, czas w jakim powinien każdy proces otrzymać procesor
    //PCB running = new PCB("proces2","filename2" );

    // pm.fork("p6", 4,"f6"); // tej zmiennej bedziemy przypisywać PCB procesu dzialajacego
    //PCB running=p7;

    PCB dummy =pm.init;
    PCB running = dummy;
    LinkedList<PCB> sort(LinkedList<PCB> Heap) // funkcja sortujaca kolejke po CurrTime, CurrTime= vruntime +Currtime
    {

        Collections.sort(Heap,Comparator.comparingLong(PCB::getVirtualTime));

        return Heap;
    }

    void PrioToWeight(PCB pcb) //funkcja zwracajca przeliczone priorytety na wagi
    {
        int weight = pcb.getPriority() *10; // przyklad
        pcb.setWeight(weight);
    }


    long HeapWeight() // funkcja zwracajaca sume wag w kolejce
    {
        long sum=0;
        for(int i=0;i<=Heap.size();i++) {
            long weight = Heap.get(i).getWeight();
            sum+=weight;
        }
        return sum;
    }

    PCB min (){ // funkcja wywolujaca process o najmniejszym czasie wirtualnym

        return Heap.get(0);
    }

    LinkedList<PCB> Insert (PCB pcb){ // dodaje proces do kolejki i ja sortuje

        PrioToWeight(pcb);
        pcb.setVirtualTime(0);
        Heap.add(pcb);
        return sort(Heap);
    }

    LinkedList<PCB> InsertFirst (PCB pcb){ // dodaje proces do kolejki i ja sortuje

        PrioToWeight(pcb);
        //pcb.setVirtualTime(0);
        Heap.add(pcb);
        return sort(Heap);
    }

    Scheduler(){}


    LinkedList<PCB> Delete (PCB pcb) // pytanie czy tu caly blok kontrolny procesu dostane czy tylko jego id
    {
        Heap.remove(pcb);
        return sort(Heap);
        /// wysylanie procesu do dominika mozna by tu zmiane stanu zrobic
    }

    void TimeSlice(PCB pcb){ // wylicza time slice dla procesu, tylko nie wiem czy
        long timeslice= period*pcb.getWeight()/HeapWeight();
        if (timeslice<min_granularity)
            timeslice=min_granularity;
        pcb.setTimeSlice(timeslice);

    }

    void VirtualTime(PCB pcb) // zakładam,ze przy zmianie stanu na aktywny w baseTime wpiszemy czas, podczas ktorego process dostał procesor
    {
        int weight = pcb.getWeight();
        long currTime=CurrTime;
        long baseTime=pcb.getBaseTime();
        long VirtualTime=pcb.getVirtualTime();
        long NewVirtualTime=(currTime-baseTime)*weight+VirtualTime;
        pcb.setVirtualTime(NewVirtualTime);
        sort(Heap);
        //return NewVirtualTime; // nie wiem czy ten return bedzie potrzebny czy nie przerzuce sie na void
    }



    void check() {    //CurrTime=System.nanoTime();

         running.setTimeSlice(running.getTimeSlice()-1);
        if (running.getState() == PCB.StateList.Terminated && (Heap.size() == 0)) // jesli proces został wykonany oraz wielkosc kolejki rowna sie zero to wykonujemy dummy
        {
            Delete(running);
            running = dummy;
        }// sprawdz czy heap.size gdy jet puste daje nam 0

      else  { if (running.getState() == PCB.StateList.Terminated) //  jesli aktywny proces sie wykonał to go usunąć
        {
            Delete(running);
        } else {
            VirtualTime(running);


        PCB min = min();

        if(running.getTimeSlice()<=0)
        {
            running.setState(PCB.StateList.Ready);
            running = min;
            TimeSlice(running);
            running.setBaseTime(CurrTime);
            running.setState(PCB.StateList.Running);
        }



        else if(running.getVirtualTime() > min.getVirtualTime()) //jesli czas wirtualny dzialajacego jest mniejszy od minimalnego
        {
            running.setState(PCB.StateList.Ready);
            running = min;
            TimeSlice(running);
            running.setBaseTime(CurrTime);
            running.setState(PCB.StateList.Running);

        }}}

        sort(Heap); // nie wiem do koncza czyt o jest potrzebne
    }





        //------------------------------------------------------------------------------------------
       /* System.out.println(CurrTime);
        System.out.println("running: " + running.getName() + " ID "+running.getID()+ " virtualTime "+ running.getVirtualTime()
        + " base time: "+ running.getBaseTime() + "currTime" + running.getCurrTime() );

        for (int k=0; k<Heap.size(); k++)
        {System.out.println("proces numer: "+k+ " w kolejce to " + Heap.get(k).getName()+ " o id: "+ Heap.get(k).getID() +
                " vruntime: "+ Heap.get(k).getVirtualTime()+" base time: " +Heap.get(k).getBaseTime() + "currTime"+Heap.get(k).getCurrTime());}
*/

   /* public static void main(String args[]){
        Scheduler s= new Scheduler();
        PCB pcb= new PCB("poces", "plik");
    }*/


}


