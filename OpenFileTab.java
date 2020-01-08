import java.util.Hashtable;

public class OpenFileTab
{
    public Hashtable<PCB, File> tab;

    public OpenFileTab()
    {
        tab = new Hashtable<PCB, File>();
    }

    public void addFile(PCB pcb ,File file)
    {
        int num = file.number;
        Inode[] inodeTab = Disc.getInodeTableInstance();
        inodeTab[num].state = true;
        inodeTab[num].lock.acquire(pcb);
        tab.put(pcb, file);
    }

    public void removeFile(PCB pcb, File file)
    {
        int num = file.number;
        Inode[] inodeTab = Disc.getInodeTableInstance();
        inodeTab[num].state = false;
        inodeTab[num].lock.release(pcb);
        tab.remove(pcb);
    }
}
