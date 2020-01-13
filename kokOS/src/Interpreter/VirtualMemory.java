package Interpreter;

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
        for(int i=0; i<16; i++)
        {
            RamStatus[i] = new WhatsInside();
        }
    }

    //metoda która pobiera nowy proces,wczytuje do pliku wymiany program, nadaje tablice stronic i wcztytuje 1 stronice do ramu
    public static void nowyproces(PCB proces) {
        Vector<Vector<Character>> program = new Vector<>(new Vector<>());
        // proces.setPageTable(new Vector<Page>());
        Vector<Page> pageTable = proces.getPageTable();
        //System.out.println(proces.getCode().length());
        double maxPageID = Math.ceil(proces.getCode().length() / 32)+1;
        //System.out.println(maxPageID);
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
        // System.out.println("=================wstawiamy ramke  ");
        // System.out.println(PageFile.toString());
        boolean wstawianieramki = false;
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
                        updateodniesienia(procID,pageID,true);
                        updateRamStatus(procID, pageID, fID);
                        //printPageTable(procID);
                        victimQueue.add(fID);
                        break;
                    }
                }
            }
        }  /**else
         {
         System.out.println("RAM is full, searching for victim...");
         printPageTable();
         printQueue();
         printRamStatus();
         int fID = findVictim();
         takepageout(proces, fID);
         //System.out.println("Id ramki do wyrzucenia " + fID);
         if (putPageIn(fID, Programwstornie))
         {
         updatePageTables(procID, pageID, fID, true);
         updateRamStatus(procID, pageID, fID);
         victimQueue.add(fID);
         System.out.println(("Page: " + pageID + " processID: " + procID + " had been put into RAM"));
         }
         }*/
        else {

            System.out.println("RAM jest pełny, szukamy ramki ofiary...");
            // VirtualMemory.printQueue();
            int fID = victimQueue.element();
            //   System.out.println(fID);
            //  printPageTable();

            for (Integer key : PageTables.keySet()) {
                for (int j = 0; j < PageTables.get(key).size(); j++) {
                    //  System.out.println("WIelkosc tablicy" +  PageTables.get(key).size());

                    fID = victimQueue.element();
                    //  System.out.println("Frameid w pętli "+fID);
                    if (PageTables.get(key).get(j).nrramki == fID) {
                        if (PageTables.get(key).get(j).odniesienia == true) {

                            victimQueue.remove(fID);
                            updateodniesienia(key, j, false);

                            victimQueue.add(fID);
                            //printQueue();

                        } else if (PageTables.get(key).get(j).odniesienia == false) {
                            // if(j) {
                            if (wstawianieramki == false) {
                                wstawianieramki = true;
                                // System.out.println("Id ramki do wyrzucenia " + fID);
                                // for(int k=0;k<1;k++){

                                takepageout(proces, fID);
                                // Memory.printRawRam();
                                if (PageTables.get(key).get(j).valid == false) {
                                    if (putPageIn(fID, Programwstornie) == true) {
                                        int f = 0;
                                        //      if(f>0) {

                                        updatePageTables(key, j, -1, false);
                                        updateodniesienia(key, j, false);
                                        updatePageTables(procID, pageID, fID, true);
                                        updateodniesienia(procID, pageID, true);
                                        updateRamStatus(procID, pageID, fID);
                                        victimQueue.remove(fID);
                                        victimQueue.add(fID);
                                        System.out.println("Strona: " + pageID + " procesu o ID: " + procID + " została włożona do Ramu");
                                        //   VirtualMemory.printQueue();
                                        //   printPageTable();
                                        //   printRamStatus();
                                        //   Memory.printRawRam();
                                    }
                                }
                            }
                        }

                    }

                }

            }

            wstawianieramki = false;
        }
    }

    static boolean putPageIn(int FrameID, Vector<Character> Page) {
        /**Method that puts certain page from pageFile into RAM*/
        System.out.println("Wstawiamy kolejna stronice do ramki nr: " + FrameID);
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
        //  System.out.println("-----Wyjmujemy stronice --------------- z ramki nr:" + frameID + "Dla id procesu : " + proces.getID());
        Vector<Page> pageTable = proces.getPageTable();
        int pageID = 0;
        int procID = 0;
        for (Integer key : PageTables.keySet()) {
            for (int i = 0; i < PageTables.get(key).size(); i++) {
                if (PageTables.get(key).get(i).nrramki == frameID){
                    pageID = i;
                    procID = key;
                    //   System.out.println("Page id w petli " + pageID + "Proc id w petli " + procID);
                    break;}

            }
        }
        Vector<Character> page;
        page = Ram.readFrame(frameID);
        // printPageTable();
        putPageInPageFile(pageID, procID, page);
        //System.out.println("PageID" + pageID);
        updatePageTables(procID,pageID,-1,false);
        updateodniesienia(procID,pageID,false);
        //  printPageTable();
        updateRamStatus(-1, -1, frameID);
    }

    static void updatePageTables(int procID, int pageID, int frameID, boolean value) {
        PageTables.get(procID).get(pageID).nrramki = frameID;
        PageTables.get(procID).get(pageID).valid = value;
    }
    public  static void updateodniesienia(int procID,int pageID,boolean value)
    {
        PageTables.get(procID).get(pageID).odniesienia = value;
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
        System.out.println("Usuwamy proces " + proces.getName() + " o ID " + proces.getID());
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
                // System.out.println("Victim queue updated, removed: pID:" + proces.getID() + " from queue position: " + i);
            }
        }
        //czyszczenie kolejki glownej
        //victimQueue.removeAll(victimQueue);
        //aktualizacja zawartosci kolejki glownej
        //victimQueue.addAll(tmpQueue);
        /**Removing all records from PageFile and PageTables maps*/
        PageFile.remove(proces.getID());
        PageTables.remove(proces.getID());
        System.out.println("Proces " + proces.getName() + " o ID " + proces.getID() + " pomyslnie usunieto");
    }


    /**
     * Funkcje dla Ciechana
     */
    public static int matchPage(PCB proces, int pageID) {
        //System.out.println("WESZLISMY DO MATCGH PAGE");
        // Interpreter.processManagement.showAllProcesses();
        //System.out.println(proces.getID());
        // printPageTable();
        //printPageTable(proces.getID());
        //printPageFile(proces.getID());
        // printPageFile();
        //System.out.println(PageTables.get(proces.getID()).get(pageID).valid);
        //Vector<Page> pageTable = proces.getPageTable();
        if (PageTables.get(proces.getID()).get(pageID).valid) {

            //System.out.println("Stronica jest w ramie, match page");
            //System.out.println(PageTables.get(proces.getID()).get(pageID).nrramki);
            updateodniesienia(proces.getID(),pageID,true);
            return PageTables.get(proces.getID()).get(pageID).nrramki;
        } else {
            //System.out.println("KONIEC STRONY, BEDZIEMY WKLADAC-------------------------------------------");
            //System.out.println("PROCES ID przed wkladaniem strony do ramu czytania znaku: " + proces.getID());

            putPageInRam(proces, proces.getID(), pageID);
            //System.out.println("PROCES ID po wlozeniu do ramu: " + proces.getID());
            return PageTables.get(proces.getID()).get(pageID).nrramki; //bylo return 0,

        }
    }

    public static int find(PCB proces, int adress) {
        //System.out.println("find");

        int pageid = (((adress - (adress % 32))) / 32);
        int frameid = matchPage(proces, pageid);

        // System.out.println("w FIND:----------page id:   " +pageid + " frame id: "+ frameid);

        return frameid;
    }

    public  static char readChar(PCB proces, int adress) {
        //System.out.println("read char");

        int frameid = find(proces, adress);

        char czytany = Memory.readFromFrame(adress, frameid);

        //System.out.println("w READCHAR:-----------frame id: " + frameid+"  czytany: " + czytany+".");
        return czytany;
    }
    public static void writechar(PCB proces, int adress, char data) {
        int frameid = find(proces, adress);
        Memory.writeToFrame(data,adress, frameid);
    }

    public static int saveString(PCB proces, int adress, String data){
        int frameid = find(proces, adress);
        int bajty = Memory.writeString(data, adress, frameid);
        return bajty;
    }
    /**
     * Metody do wyświetlania
     */

    public static void printPageTable(int processID) {
        if (processExists(processID)) {
            System.out.println("Tablica stronic dla procesu o id: " + processID);
            System.out.println("Page id " +"   \t" + "Frame id" + "   \t" + " Bit valid " + "   \t" + " Bit odniesienia ");
            for (int i = 0; i < PageTables.get(processID).size(); i++) {
                System.out.println(i + "          \t" + PageTables.get(processID).get(i).nrramki + "         \t" + PageTables.get(processID).get(i).valid + "         \t" + PageTables.get(processID).get(i).odniesienia);
            }
        } else System.out.println("Error: process with given ID doesn't exist.");
    }

    public static void printPageTable()   {
        for ( Integer key : PageTables.keySet() ) {
            System.out.println("Tablica stronic dla procesu o id: " + key);
            System.out.println("Page id " +"   \t" + "Frame id" + "   \t" + " Bit valid " + "   \t" + " Bit odniesienia ");
            for (int j = 0; j < PageTables.get(key).size(); j++) {
                System.out.println("Page no." + j);
                // System.out.println("Nr procesu w pliku wymiany "+i);
                System.out.println(j + "          \t" + PageTables.get(key).get(j).nrramki + "         \t" + PageTables.get(key).get(j).valid + "         \t" + PageTables.get(key).get(j).odniesienia );
            }
        }
        System.out.println(" ");

    }

    public static void printQueue() {

        System.out.println(victimQueue.toString());
    }

    public static void printPageFile(int processID) {
        if (processExists(processID)) {
            Vector<Vector<Character>> pages = PageFile.get(processID);
            int size=pages.size();
            if(size>1) size-=1;
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

    public static void printRamStatus() {
        System.out.println("#### Printing current RAM status ####");
        for(int i=0; i<16; i++)
        {
            System.out.println("Frame ID: " + i + " PageID " + RamStatus[i].PageID + "\t ProcessID " + RamStatus[i].ProcessID);
        }
        System.out.println(" ");
        System.out.println("");
    }
}