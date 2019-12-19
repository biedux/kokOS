package processor;



import java.util.LinkedList;
import java.util.*;

public class Scheduler
{   PCB pcb = new PCB("dod", 19, 10, "mata",12);

    LinkedList<PCB> Heap= new LinkedList<PCB>();
    int CurrTime; // zmienna inkrementujaca sie  co wykonana instrukcje ciechan ja obsluzy

    int min_granularity = 4; // przykladowe jak z tymi krokami procesora to okreslic?

    int period = 20; // jw
    PCB running; // tej zmiennej bedziemy PCB procesu dzialajacego


    PCB dummy;// -> zmienna ktora bedzie wykonywana gdy wielkosc kolejki bedzie rowna 0

    LinkedList<PCB> sort(LinkedList<PCB> Heap) // funkcja sortujaca kolejke po CurrTime, CurrTime= vruntime +Currtime
    {
        this.Heap=Heap;
      Collections.sort(Heap,Comparator.comparingLong(PCB::getCurrTime));

        return Heap;
    }

    void PrioToWeight(PCB pcb) //funkcja zwracajca przeliczone priorytety na wagi, dane teraz podane sa losowe czy nie powinno to z pcb isc
    {
        int weight = pcb.getCurrPriority() *10;
        pcb.setWeight(weight);
    }


    int HeapWeight(LinkedList<PCB>Heap) // funkcja zwracajaca sume wag w kolejce
    {
        int sum=0;
        for(int i=0;i<=Heap.size();i++) {
            int weight = Heap.get(i).getWeight();
            sum+=weight;
        }
        return sum;
    }

    PCB min (LinkedList<PCB> Heap){ // funkcja wywolujaca process o najmniejszym czasie wirtualnym
        this.Heap=Heap;
        return Heap.get(0); // pytanie czy get tylko pobiera dane czy tez uuswa z kolejki
    }

    LinkedList<PCB> Insert (PCB pcb){ // dodaje proces do kolejki i ja sortuje
        this.pcb=pcb;
        PrioToWeight(pcb);
        Heap.add(pcb);
        return sort(Heap);
    }


    LinkedList<PCB> Delete (PCB pcb) // pytanie czy tu caly blok kontrolny procesu dostane czy tylko jego id
    {  this.pcb=pcb;
       Heap.remove(pcb);
        return sort(Heap);
        /// wysylanie procesu do dominika mozna by tu zmiane stanu zrobic
    }

   void TimeSlice(int period, PCB pcb){ // wylicza time slice dla procesu, tylko nie wiem czy
        this.pcb=pcb;
      int timeslice= period*pcb.getWeight()/HeapWeight(Heap);
      if (timeslice<min_granularity)
          timeslice=min_granularity;
      pcb.setCurrTime(timeslice);
   }

   void VirtualTime(PCB pcb,int CurrTime) // zakładam,ze przy zmianie stanu na aktywny w baseTime wpiszemy czas, podczas ktorego process dostał procesor
   { this.pcb=pcb;
    int weight = pcb.getWeight();
    int currTime=CurrTime;
    int baseTime=pcb.getBaseTime();
    int VirtualTime=pcb.getVirtualTime();
    int NewVirtualTime=(currTime-baseTime)*weight+VirtualTime;
    pcb.setVirtualTime(NewVirtualTime);
    //return NewVirtualTime; // nie wiem czy ten return bedzie potrzebny czy nie przerzuce sie na void
   }


//dodac vruntime do PCB i curr time i base time tez
   void check()
   {    if(running.getState()== PCB.StateList.Terminated && (HeapWeight(Heap)==0))
       running=dummy;

       if(running.getState() == PCB.StateList.Terminated) //  jesli aktywny proces sie wykonał to go usunąć
         { Delete(running);}
         else {
           VirtualTime(running, CurrTime); }

         PCB min=min(Heap);

       if(running.getVirtualTime()>min.getVirtualTime()) //nie wiem czy dobrze jeszcze
       {
           running.setState(PCB.StateList.Ready);// tu tylko zmiana nazwy chyba powinny byc jeszcze funkcje to wyłapująca i usuwająca proces z kolejki gotowych
           min.setState(PCB.StateList.Running);
           running=min;
           TimeSlice(period,running);
           running.setBaseTime(CurrTime);

       }
   }

   /* void ChangeStateToReady(PCB pcb) // funkcja wykorzystyanw przez Dominika do zmieniania stanu na gotowy
    { pcb.setState(PCB.StateList.Ready);
        PrioToWeight(pcb);
        TimeSlice(period,pcb);
        Insert(pcb);
    }*/




  //ponizej te stare check sa zakomentowane wszystkie
// sprawdz te funkcje jeszcze czy tam przy zmienianiu stanow itp nie robisz bledow kuurde kroki mi sie nie dekrementuja ehh
    // nie zabezpieczylem rowniez przed min_granularity
    // czy to check wszystko powinno wykonywac czy jakiegos maina na funkcje tez dac
   /* void check_new(PCB nowy,PCB running) // funkcja chek sie wykonuje gdy kroki -kroki aktualne jest wieksze od 0 tzn przerwanie co kazdy rozkaz
   {

       if(running.getState()== PCB.StateList.Terminated)
        { Delete(running);}
        else {int vruntime=vRuntime(running, kroki, kroki2);
        running.setCurrTime(vruntime);}

        if(nowy.getID()>0) // jesli nowy istnieje zalatwie to jakos inaczej ale nie wiem jak jeszcze
         {running.setState(PCB.StateList.Ready);
      nowy.setState(PCB.StateList.Running);
      TimeSlice(period,nowy);//timeslice ustawie currtime jako timeslice
             PrioToWeight(nowy);
              Insert(running);
      running=nowy;}

   }

    void check_old(PCB running, PCB min) // funkcja chek sie wykonuje gdy kroki -kroki aktualne jest wieksze od 0 tzn przerwanie co kazdy rozkaz
    {

        if(running.getState()== PCB.StateList.Terminated)
        { Delete(running);}
        else {int vruntime=vRuntime(running, kroki, kroki2);
            running.setCurrTime(vruntime);}



        if(min.getCurrTime()<running.getCurrTime())//currtime jest wczesniej aktualizowane w tej funkcji
        { running.setState(PCB.StateList.Ready);
            min.setState(PCB.StateList.Running);
            int vruntime1=vRuntime(running,kroki,kroki2);
            running.setCurrTime(vruntime1);
            Insert(running);
            running=min; }


    }
   void check (PCB nowy, PCB min, PCB running) {
        if (HeapWeight(Heap)==0) {
            running=dummy;
        }
       if (nowy.getState() == PCB.StateList.Ready) {
         check_new(nowy,running)}
       else check_old(running,min);}*/

}

