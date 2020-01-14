package Interpreter;
import java.util.LinkedList;


public class Lock {


    //Kluczowe (hehe) pole zamka. Zamknięty zamek - true, otwarty zamek - false
    private boolean isLocked = false;

    //ID procesu "trzymającego" zamek
    private int holdersID = -1;

    //Lista PCB procesów, które chcą wejśc do zamka, ale nie mogą ponieważ jest zablokowany
    private LinkedList<PCB> queue = new LinkedList<PCB>();

    //Blokowanie (pozyskanie) zamka
    //Zwraca true, jeśli PCB dostał zamek lub false, jeśli PCB wszedł do kolejki
    public boolean acquire (PCB ProcessCB)
    {
        if (isLocked)
        {
            queue.add(ProcessCB);
            ProcessCB.setState(PCB.StateList.Waiting);
            Shell.getPM().scheduler.running = Shell.getPM().scheduler.dummy;
            Shell.getPM().scheduler.Delete(ProcessCB);
            return false;
        }
        else
        {
            holdersID = ProcessCB.getID();
            isLocked = true;
            return true;
        }
    }

    //Odblokowanie (zwolnienie) zamka i ewnetualne przekazanie następnemu procesowi w kolejce
    //Zwraca holdersID, jeżeli kolejne PCB w kolejce dostało zamek (plik nadal otwarty) lub -1 jeżeli w kolejce nie ma nic
    public int release (PCB ProcessCB)
    {
        if (isLocked && (holdersID == ProcessCB.getID())) {
            isLocked = false;
            holdersID = -1;

            boolean check = true;
            while (check) {
                if (queue.size() > 0) {
                    PCB nextProcessCB = queue.removeFirst();
                    if(Shell.getPM().ProcessList.contains(nextProcessCB))
                    {
                        holdersID = nextProcessCB.getID();
                        nextProcessCB.setState(PCB.StateList.Ready);
                        Shell.getPM().scheduler.Insert(nextProcessCB);
                        check = false;
                        isLocked = true;
                    }
                }
                else {
                    check = false;
                }
            }

            /* Backup code
            boolean check = true;
            while (check) {
                try {
                    if (queue.size() > 0) {
                        ProcessCB = queue.removeFirst();
                        holdersID = ProcessCB.getID();
                        ProcessCB.setState(PCB.StateList.Ready);
                        check = false;
                        isLocked = true;
                    }
                } catch (Exception e) {
                    check = true;
                    isLocked = false;
                    System.out.println(e + " PCB pobrane z kolejki nie istnieje, pobieranie kolejnego.");
                }
            }
            */
        }
        return holdersID;
    }

    //Sprawdzenie zamka (zwraca false (0) jeśli zamek zajęty; true (1) jeśli zamek wolny)
    public boolean tryLock ()
    {
        return isLocked;
    }

    //Konstruktor zamka
    public Lock ()
    {
    }

}