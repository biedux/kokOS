import java.util.LinkedList;

public class Lock {

    //Kluczowe (hehe) pole zamka. Zamknięty zamek - true, otwarty zamek - false
    private boolean isLocked = false;

    //ID procesu "trzymającego" zamek
    private int holdersID = 0;

    //Lista ID procesów, które chcą wejśc do zamka, ale nie mogą ponieważ jest zablokowany
    private LinkedList<Integer> queue = new LinkedList<Integer>();

    //PM uzupełniany przez jedyny systemowy Management, aby móc sterować procesami
    private Process_Management PM;

    //Blokowanie zamka
    public void lock (int ProcessID)
    {
        if (isLocked)
        {
            queue.add(ProcessID);
            PM.ProcessList.forEach(pcb->
            {
                if(pcb.getID() == ProcessID)
                {
                    pcb.setState(PCB.StateList.Waiting);
                }
            });
        }
        else
        {
            holdersID = ProcessID;
            isLocked = true;
        }
    }

    //Odblokowanie zamka i ewnetualne przekazanie następnemu procesowi w kolejce
    public void unlock (int ProcessID)
    {
        if (isLocked && (holdersID == ProcessID)) {
            isLocked = false;
            holdersID = 0;
            if(queue.size()>0)
            {
                holdersID = queue.removeFirst();
                PM.ProcessList.forEach(pcb ->
                {
                    if (pcb.getID() == holdersID) {
                        pcb.setState(PCB.StateList.Ready);
                        isLocked = true;
                    }
                    else
                    {
                        holdersID = 0;
                    }
                });
            }
        }
    }

    //Konstruktor zamka
    public Lock (Process_Management ProcessManagement)
    {
        PM = ProcessManagement;
    }

}