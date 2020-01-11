import java.util.Hashtable;
import java.util.Map;

public class OpenFileTab
{
    public Hashtable<Integer,File> tab;

    public OpenFileTab()
    {
        tab = new Hashtable<>();
    }

    public void addFile(PCB pcb ,File file)
    {
        int num = file.number;
        Inode[] inodeTab = Disc.getInodeTableInstance();
        boolean flag = inodeTab[num].lock.acquire(pcb);
        if(flag)
        {
            inodeTab[num].state = inodeTab[num].lock.tryLock();
            tab.put(pcb.getID(),file);
        }
    }

    public void removeFile(PCB pcb, File file)
    {
        int num = file.number;
        Inode[] inodeTab = Disc.getInodeTableInstance();
        int flag = inodeTab[num].lock.release(pcb);
        if(flag == -1)
        {
            inodeTab[num].state = inodeTab[num].lock.tryLock();
            tab.remove(pcb.getID());
        }
        else
        {
            int PID = pcb.getID();
            tab.remove(PID, file);
            int s = tab.size();
            System.out.println(s);
            tab.put(flag,file);
        }
    }

    public void printTab() throws Exception
    {
        if(tab.isEmpty())
        {
            throw new Exception("Zaden plik nie jest otwarty");
        }
        else
        {
            System.out.println("||||||||||||| TABLICA OTWARTYCH PLIKOW |||||||||||||\n");
            for(Map.Entry<Integer, File> e : tab.entrySet())
            {
                File file = e.getValue();
                int PID = e.getKey();

                System.out.println("\n*********************************\n");
                System.out.println("Nazwa pliku:: " + file.name);
                System.out.println("ID procesu:: " + PID);
                System.out.println("\n*********************************\n");
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
        }
    }
}
