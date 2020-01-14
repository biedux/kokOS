package Interpreter;
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
        if(Disc.inodes_table[num] != null)
        {
            Inode[] inodeTab = Disc.getInodeTableInstance();
            boolean flag = inodeTab[num].lock.acquire(pcb);
            if(flag)
            {
                inodeTab[num].state = inodeTab[num].lock.tryLock();
                file.positionPtr = 0;
                tab.put(pcb.getID(),file);
                System.out.println("Otworzono plik o nazwie " + file.name);
            }
            else
            {
                System.out.println("Plik " + file.name + " juz jest otwarty przez inny proces");
            }
        }
    }

    public void removeFile(PCB pcb, File file)
    {
        int num = file.number;
        if(Disc.inodes_table[num] != null)
        {
            Inode[] inodeTab = Disc.getInodeTableInstance();
            int flag = inodeTab[num].lock.release(pcb);
            if(flag == -1)
            {
                inodeTab[num].state = inodeTab[num].lock.tryLock();
                tab.remove(pcb.getID());
                System.out.println("Zamknieto plik o nazwie " + file.name);
            }
            else
            {
                int PID = pcb.getID();
                tab.remove(PID, file);
                file.positionPtr = 0;
                tab.put(flag,file);
                System.out.println("Plik " + file.name + " zostal otworzony przez inny proces");
            }
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

    public int findPID(String name) //zwraca id procesu
    {
        if(tab.size() == 0)
        {
            return -1;
        }
        else
        {
            for(Map.Entry<Integer, File> e : tab.entrySet())
            {
                File file = e.getValue();
                int PID = e.getKey();
                if(file.name.equals(name))
                {
                    return PID;
                }
            }
        }
        return -1;
    }

    public File findFile(PCB pcb)
    {
        if(tab.size() == 0)
        {
            return null;
        }
        else
        {
            for(Map.Entry<Integer, File> e : tab.entrySet())
            {
                File file = e.getValue();
                int PID = e.getKey();
                if(PID == pcb.getID())
                {
                    System.out.println("Znaleziono plik o danym PCB");
                    return file;
                }
                else
                {
                    return null;
                }
            }
        }
        return null;
    }
}