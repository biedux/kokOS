package Virtual;

import java.util.*;

    public class VirtualMemory {

        //plik wymiany (procesID,Program)
        static Map<Integer, Vector<Vector<Character>>> PageFile = new HashMap<>();
        // mapa wszystkich użytych tablic stonic (procesID , tablica )
        static Map<Integer, Vector<Page> >PageTables =new HashMap<>();
        /**Kolejka ramek*/
        static Vector<Page> victimQueue = new Vector<>();


        //metoda która pobiera nowy proces,wczytuje do pliku wymiany program, nadaje tablice stronic i wcztytuje 1 stronice do ramul
        void nowyproces(/*PCB proces*/)
        {
            Vector<Vector<Character>> program = new Vector<>(new Vector<>());
            Vector<Page> pageTable = new Vector<>();
            int maxPageID =  16;
            for (int currentPageID = 0; currentPageID <= maxPageID; currentPageID++)
            {
                Vector<Character> page = new Vector<>();
                pageTable.add(new Page());

                for (int j = 0; j < 16; j++)
                {
                    //  page.add(proces.code.get(j + currentPageID * 16));
                }
                program.add(page);
            }
            //  putPageInRam(proces.processId, 0);
        }

        static void putPageInRam(int procID, int pageID) {
            Vector<Character> Programwstronie = PageFile.get(procID).get(pageID);
            System.out.println(procID + " +  " + pageID +" "+  Programwstronie);

            if(victimQueue.size() >16)
            {

            }
            else { int i =0;
                while (i != victimQueue.size())
                { if(victimQueue.get(i).odniesienia = true)
                {
                    victimQueue.get(i).odniesienia = false;
                    //takepageout(pageID)
                    //updatequeue --dodać strone na koniec
                    i++;

                }
                else
                {
                    System.out.println("Wyjmujemy stone!");
                    //takepageout(pageID)
                    //tu wsadzamy stonice
                }

                }
            }

        }

        static boolean pageExists(int procID, int pageID){

            try{
                PageTables.get(procID).get(pageID);
                return true;
            } catch (Exception e) {
                System.out.println("Stronica nie istnieje");
            }
            return false;
        }

        void putProcessToPageFile(int pID, Vector<Vector<Character>> pr)
        {
            PageFile.put(pID, pr);
        }

        static void demandPage(int ProcessID, int PageID)
        {
            putPageInRam(ProcessID, PageID);
        }

    public static class Main {

        public static void main(String[] args) {
          demandPage(1,1);
           // putProcessToPageFile(1, )

        }
    }


}
