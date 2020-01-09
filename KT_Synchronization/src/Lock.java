import java.util.LinkedList;

public class Lock {


    //Kluczowe (hehe) pole zamka. Zamknięty zamek - true, otwarty zamek - false
    private boolean isLocked = false;

    //ID procesu "trzymającego" zamek
    private int holdersID = 0;

    //Lista PCB procesów, które chcą wejśc do zamka, ale nie mogą ponieważ jest zablokowany
    private LinkedList<PCB> queue = new LinkedList<PCB>();

    //Blokowanie (pozyskanie) zamka
    public void acquire (PCB ProcessCB)
    {
        if (isLocked)
        {
            queue.add(ProcessCB);
            ProcessCB.setState(PCB.StateList.Waiting);
        }
        else
        {
            holdersID = ProcessCB.getID();
            isLocked = true;
        }
    }

    //Odblokowanie (zwolnienie) zamka i ewnetualne przekazanie następnemu procesowi w kolejce
    public void release (PCB ProcessCB)
    {
        if (isLocked && (holdersID == ProcessCB.getID())) {
            isLocked = false;
            holdersID = 0;

            boolean check = true;
            while (check) {
                if (queue.size() > 0) {
                    ProcessCB = queue.removeFirst();
                    if(Shell.getPM().ProcessList.contains(ProcessCB))
                    {
                        holdersID = ProcessCB.getID();
                        ProcessCB.setState(PCB.StateList.Ready);
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
