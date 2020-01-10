package Virtual;

import java.util.*;

public class VirtualMemory {
    static Memory Ram;

    public VirtualMemory(Memory ram)
    {
        this.Ram = ram;
        setRamStatus();
    }
    /**
     * Plik wymiany.
     * Integer is processID
     * wektor wektorów z kodem
     */
    static Map<Integer, Vector<Vector<Character>>> PageFile = new HashMap<>(4096);
    // mapa wszystkich użytych tablic stonic
    static Map<Integer, Vector<Page>> PageTables = new HashMap<>();
    /**
     * Kolejka  ofiar
     */
    static Queue<Integer> victimQueue = new LinkedList<>();

    class WhatsInside
    {
        int ProcessID;
        int PageID;
        WhatsInside()
        {
            this.PageID = -1;
            this.ProcessID = -1;
        }
    }

    static WhatsInside[] RamStatus = new WhatsInside[16];
    void setRamStatus()
    {
        for(int i=0; i<15; i++)
        {
            RamStatus[i] = new WhatsInside();
        }
    }

    //metoda która pobiera nowy proces,wczytuje do pliku wymiany program, nadaje tablice stronic i wcztytuje 1 stronice do ramu
    public static void nowyproces(PCB proces) {
        Vector<Vector<Character>> program = new Vector<>(new Vector<>());
       // proces.setPageTable(new Vector<Page>());
        Vector<Page> pageTable = proces.getPageTable();
        System.out.println("Kod: " + proces.getCode());
        int maxPageID = (int) Math.ceil(proces.getCode().length() / 32)+1;
        System.out.println("Max pageID: " +maxPageID);
       // if(maxPageID==0) maxPageID=1;
        for (int currentPageID = 0; currentPageID < maxPageID; currentPageID++) {
            Vector<Character> page = new Vector<>();
            pageTable.add(new Page());
            for (int j = 0; j < 32; j++) {
                try {
                    //jbc zmienic
                    page.add(proces.getCode().charAt(j + currentPageID * 32));

                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                }

            }
            program.add(page);
        }
        putInfoToPageTable(proces.getID(), pageTable);
        putProcessToPageFile(proces.getID(), program);
        putPageInRam(proces, proces.getID(), 0);
    }

    static void putPageInRam(PCB proces, int procID, int pageID) {

        Vector<Page> pageTable = proces.getPageTable();
        Vector<Character> Programwstornie = PageFile.get(procID).get(pageID);
       // System.out.println(pageID + " " + Programwstornie);
        int i = 0;
       // System.out.println(" --------------------------" + victimQueue.size());
        //printQueue();
        //printPageTable(procID);
        if (victimQueue.size() < 15) {
            for (int fID = 0; fID < 15; fID++) {
                if(RamStatus[fID].ProcessID == -1)
                {
                    if (putPageIn(fID, Programwstornie)) {
                        updatePageTables(procID, pageID, fID, true);
                        updateRamStatus(procID, pageID, fID);
                        //printPageTable(procID);
                        victimQueue.add(fID);
                        break;
                    }
               }
            }
        }
        else
        {
            System.out.println("RAM jest pełny, szukamy ramki ofiary...");
            int fID = findVictim();
            System.out.println("Id ramki do wyrzucenia "+fID);
            takepageout(proces,fID);
            if(putPageIn(fID, Programwstornie))
            {
                updatePageTables(procID, pageID, fID, true);
                updateRamStatus(procID, pageID, fID);
                victimQueue.add(fID);
                System.out.println("Page: " + pageID + " processID: " + procID + " had been put into RAM");
            }
        }
       /** else {
            System.out.println(victimQueue.size());
           // Jeśli nie ma ramki musimy znalezc ofiarę
            for (int j=0;j<victimQueue.size();j++) {
                if (pageTable.get(j).valid = true) {
                    pageTable.get(j).valid = false;
                    victimQueue.add(pageTable.get(j).nrramki);

                } else {
                    System.out.println("Wyjmujemy stone");
                    takepageout(proces, pageID);
                    if (putPageIn(pageTable.get(j).nrramki, Programwstornie)) {
                        updatePageTables(procID, pageID, pageTable.get(j).nrramki, true);
                        victimQueue.add(pageTable.get(j).nrramki);
                    }
                }
            }
        }*/

    }

    private static int findVictim() {
        return victimQueue.poll();
    }


    static boolean putPageIn(int FrameID, Vector<Character> Page) {
        /**Method that puts certain page from pageFile into RAM*/
        return Ram.writeFrame(Page, FrameID);
    }

    static boolean pageExists(int procID, int pageID) {

        try {
            PageTables.get(procID).get(pageID);
            return true;
        } catch (Exception e) {
            System.out.println("Stronica nie istnieje");
        }
        return false;
    }

    static void putProcessToPageFile(int pID, Vector<Vector<Character>> program) {
        PageFile.put(pID, program);
    }

    //Uzywana przez ram żeby dostać konkretny numer ramki w której znaduje się stronica
    //pcb do odebrania tablicy storinic procesu
    static int demandPage(PCB proces, int PageID) {
        if (!PageTables.get(proces.ID).get(PageID).valid) {
            putPageInRam(proces, proces.ID, PageID);
        }
        return PageTables.get(proces.ID).get(PageID).nrramki;
    }

    static void takepageout(PCB proces, int frameID) {
        Vector<Page> pageTable = proces.getPageTable();
        int pageID = 0;
        for (int i = 0; i < pageTable.size(); i++) {
            if (pageTable.get(i).nrramki == frameID)
                pageID = i;
        }
        Vector<Character> page;
        page = Ram.readFrame(frameID);

        putPageInPageFile(pageID, proces.ID, page);
        updatePageTables(proces.getID(),pageID,-1,false);
        updateRamStatus(-1, -1, frameID);
    }

    static void updatePageTables(int procID, int pageID, int frameID, boolean value) {
        PageTables.get(procID).get(pageID).nrramki = frameID;
        PageTables.get(procID).get(pageID).valid = value;
    }
    static void updateRamStatus(int procID, int pageID, int fID)
    {
        RamStatus[fID].ProcessID = procID;
        RamStatus[fID].PageID = pageID;
    }
    static void putPageInPageFile(int pageID, int procID, Vector<Character> page) {
        Vector<Vector<Character>> tmp = PageFile.get(procID);
        tmp.set(pageID, page);
        PageFile.put(procID, tmp);
    }

    static void putInfoToPageTable(int pID, Vector<Page> pageTable) {
        PageTables.put(pID, pageTable);
    }

    static boolean processExists(int procID) {
        return PageFile.containsKey(procID);
    }


    public static void usunproces(PCB proces)
    {
        System.out.println("Removing Process " + proces.getID());
        Queue<Integer> tmpQueue = victimQueue, beginQ = new LinkedList<>();
        int frame, Qsize;
        for(int i = 0; i<15; i++)
        {
            if(RamStatus[i].ProcessID == proces.getID())
            {
                Qsize = tmpQueue.size();
                for(int j=0; j<Qsize; j++)
                {
                    frame = tmpQueue.poll();
                    if(frame != i)
                    {
                        beginQ.add(frame);
                    } else break;
                }
                beginQ.addAll(tmpQueue);
                tmpQueue.removeAll(tmpQueue);
                tmpQueue.addAll(beginQ);
                beginQ.removeAll(beginQ);
                RamStatus[i].ProcessID = -1;
                RamStatus[i].PageID = -1;
                System.out.println("Victim queue updated, removed: pID:" + proces.getID() + " from queue position: " + i);
            }
        }
        //czyszczenie kolejki glownej
        //victimQueue.removeAll(victimQueue);
        //aktualizacja zawartosci kolejki glownej
        //victimQueue.addAll(tmpQueue);
        /**Removing all records from PageFile and PageTables maps*/
        PageFile.remove(proces.getID());
        PageTables.remove(proces.getID());
        System.out.println("Process " + proces.getID() + " has been removed");
    }



    /**
     * Funkcje dla Ciechana
     */
    int matchPage(PCB proces, int pageID) {
        Vector<Page> pageTable = proces.getPageTable();
        if (PageTables.get(proces.getID()).get(pageID).valid) {
            System.out.println("Stronica jest w ramie");
            return PageTables.get(proces.getID()).get(pageID).nrramki;
        } else {
            putPageInRam(proces, proces.ID, pageID);
            return PageTables.get(proces.getID()).get(pageID).nrramki;
        }
    }

    int find(PCB proces, int adress) {
        int pageid = ((adress - adress % 32) / 32);
        int frameid = matchPage(proces, pageid);
        return frameid;
    }

    char readChar(PCB proces, int adress) {
        int frameid = find(proces, adress);
        char czytany = Memory.readFromFrame(adress, frameid);
        return czytany;
    }
    void writechar(PCB proces, int adress,char data) {
        int frameid = find(proces, adress);
         Memory.writeToFrame(data,adress, frameid);
    }


    /**
     * Metody do wyświetlania
     */

    public static void printPageTable(int processID) {
        if (processExists(processID)) {
            System.out.println("Tablica stronic dla procesu o id: " + processID);
            System.out.println("Page id " +"   \t" + "Frame id" + "   \t" + " Bit valid ");
            for (int i = 0; i < PageTables.get(processID).size(); i++) {
                System.out.println(i + "          \t" + PageTables.get(processID).get(i).nrramki + "         \t" + PageTables.get(processID).get(i).valid);
            }
        } else System.out.println("Error: process with given ID doesn't exist.");
    }

    public static void printQueue() {
        System.out.println(victimQueue.toString());

    }

    public static void printPageFile(int processID) {
        if (processExists(processID)) {
            System.out.println("Stronice w Pliku Wymiany dla procesu o id: " + processID);
            Vector<Vector<Character>> pages = PageFile.get(processID);
            System.out.println(pages);
            int size=pages.size();
            System.out.println("Wielkosc" + size);
            //if(size<1) size-=1;
            for (int i = 0; i < size; i++) {
                System.out.println("Page no." + i);
                for (int j = 0; j < pages.get(i).size(); j++) {
                    System.out.print(pages.get(i).get(j)+" ");
                }
                System.out.println("");
            }
            System.out.println("");
        } else System.out.println("Error: process with given ID doesn't exist.");
    }

    public static void printPageFile() {
            for ( Integer key : PageFile.keySet() ) {
                System.out.println("Page file dla procesu o id: " + key);
                for (int j = 0; j < PageFile.get(key).size(); j++) {
                    System.out.println("Page no." + j);
                   // System.out.println("Nr procesu w pliku wymiany "+i);
                    Vector<Vector<Character>> pages = PageFile.get(key);
                    System.out.println(PageFile.get(key).get(j) );
                    System.out.println("");
                }
            }
            System.out.println(" ");
            System.out.println("");
    }
    public static void printPageFile2()
    {
        System.out.println("Page file: ");
        System.out.println(PageFile.toString());
        System.out.println("");
    }
    public  static void printPageTable()
    {
        System.out.println(PageTables.toString());
        System.out.println(" ");
    }
    public static void printRamStatus()
    {
        System.out.println("#### Printing current RAM status ####");
        for(int i=0; i<15; i++)
        {
            System.out.println("Frame ID: " + i + " PageID " + RamStatus[i].PageID + "\t ProcessID " + RamStatus[i].ProcessID);
        }
    }

}

