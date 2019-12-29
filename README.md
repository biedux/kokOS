# kokOS
Algorytm prawdopodobnie działa prawidłowo, wymaga jedynie kosmetycznych zmian w kodzie, żeby to lepiej wyglądało. 
Insert() --> powinna zostać użyta, gdy zmieniamy stan procesu na gotowy 
check() --> funkcja sprawdzająca, który proces powinien się teraz wykonywać wykonywana co każdy krok w naszym procesorze. 

check() --> korzysta ze wszystkich innych pomniejszych funkcji, których nie ma sensu tutaj opisywac. 

Sprawdzenie odbywało się poprzez imitację wybranego momentu w w pracy naszego systemu tzn. 
utworzenie procesów i dodanie ich do kolejki z wymyślonym przez nas czasem wirtualnym.
Na samym początku używamy funkcin InsertFirst(), która jest nam potrzebna tylkod do sprawdzanie, w późniejszym czasie 
zostanie usunięta i będziemy korzystac tylkoz funkcji Insert().

Za pomocą if() imitujemy w danym kroku jakieś działanie tzn. wykonanie się procesu, skończenie się dla niego timeslice itp., 
czy pojawienie się innego warunku, który powinien spowodować zmiany w wykonywanme procesie. 

Przykładowy program, którym sprawdzaliśmy działanie algorytmu. Każde wciśnięcie 0 w konsoli to kolejny krok naszego procesora.


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scheduler s=new Scheduler();
        Process_Management PM=new Process_Management();
        PCB P1=PM.fork(PM.init, "P1",3,"");
        PCB P2=PM.fork(PM.init,"P2",4,"");
        PCB P3=PM.fork(PM.init,"P3",2,"");
        System.out.println("\nDrzewo procesow:");
        PM.showTree(PM.init);
        long k=32;
        P1.setVirtualTime(8);
        P2.setVirtualTime(12);
        P3.setVirtualTime(4);
        s.InsertFirst(P1);
        s.InsertFirst(P2);
        s.InsertFirst(P3);
       /* for(PCB p:s.Heap){
            p.printProcessInfo();
        }
        s.running.printProcessInfo();*/


        while(true){




                Scanner scan=new Scanner(System.in);
                int cos=scan.nextInt();

                if(cos==0){
                    System.out.println("krok"+k);
                    s.CurrTime=k;
                }
                k++;
               

      

            //if(k==36){s.Delete(P2);}
            /*if(k==36)
            {
                System.out.println(P3.getState());P3.setState(PCB.StateList.Terminated);
                System.out.println(P3.getState());}*/
            if(k==39)
            {PCB P4=PM.fork(PM.init,"P4",5,"");
            s.InsertFirst(P4);}
            if(k==40)
            {PCB P5=PM.fork(PM.init,"P5",6,"");
            s.Insert(P5);}


            s.check();

            System.out.println("CurrTime  "+s.CurrTime);
            for(PCB p:s.Heap){
                p.printProcessInfo();
            }

        }



