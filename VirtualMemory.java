package Virtual;

import java.util.*;

    public class VirtualMemory {

        //plik wymiany (procesID,Program)
        static Map<Integer, Vector<Vector<Character>>> PageFile = new HashMap<>();
        // mapa wszystkich użytych tablic stonic (ProcesID , tablica )
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

        static void putPageInRam(int ProcessID, int pageID) {
           Vector<Character> Programwstronie = PageFile.get(ProcessID).get(pageID);
        System.out.println(ProcessID + " +  " + pageID +" "+  Programwstronie);
            int i =0;
            if(victimQueue.size() >16)
            {
                System.out.println("Mamy ramke");
            }
            else {
                while (i != victimQueue.size())
                { if(victimQueue.get(i).valid = true)
                {
                    victimQueue.get(i).valid= false;
                    takepageout(ProcessID,pageID);
                    //updatequeue --dodać strone na koniec
                    i++;
                }
                else
                {
                    System.out.println("Wyjmujemy stone!");
                    takepageout(ProcessID,pageID);
                }

                }
            }

        }

        static boolean pageExists(int ProcessID, int pageID){

            try{
                PageTables.get(ProcessID).get(pageID);
                return true;
            } catch (Exception e) {
                System.out.println("Stronica nie istnieje");
            }
            return false;
        }

        void putProcessToPageFile(int procesID, Vector<Vector<Character>> pr)
        {
            PageFile.put(procesID, pr);
        }

 //Uzywana przez ram żeby dostać konkretny numer ramki w której znaduje się stronica
        static int demandPage(int ProcessID, int PageID)
        {
            if (!PageTables.get(ProcessID).get(PageID).valid)
           {
              putPageInRam(ProcessID,PageID);
            }
            return PageTables.get(ProcessID).get(PageID).nrramki;
        }
        static void updatePageTable(int ProcesID,int PageID, int FrameID)
        {
            int numerdozastapienia;
            numerdozastapienia = PageTables.get(ProcesID).get(PageID).nrramki;
       //  PageTables.replace(ProcesID,numerdozastapienia,FrameID);
        }

        static void takepageout(int ProcesID, int pageID)
        {
            PageTables.remove(pageID);
            updatePageTable(ProcesID,pageID,-1);
        }

        public void readPageTable(int ProcesID,int PageID,Vector<Page> wektor){
            for (int i=0;i<wektor.size();i++)
            {
                System.out.println(PageID + " " + PageTables.get(ProcesID).get(PageID).nrramki + " " + PageTables.get(ProcesID).get(PageID).valid );
            }
        }
        public void readPageFile(int ProcesID,int PageID){
                for (int i=0;i<PageFile.size();i++)
                {
                    System.out.println(PageFile.get(ProcesID).get(PageID) + " " );
                }
            }


