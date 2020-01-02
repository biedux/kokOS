package Virtual;

import java.util.*;

        public class VirtualMemory {
           static memory Ram;
            /**Plik wymiany.
             * Integer is processID
             * wektor wektorów z kodem*/
            static Map<Integer, Vector<Vector<Character>>> PageFile = new HashMap<>(4096);
            // mapa wszystkich użytych tablic stonic
            static Map<Integer, Vector<Page>>  PageTables = new HashMap<>();
            /**Kolejka  ofiar*/
            static Vector<Integer> victimQueue = new Vector<>();


            //metoda która pobiera nowy proces,wczytuje do pliku wymiany program, nadaje tablice stronic i wcztytuje 1 stronice do ramu
            void nowyproces(PCB proces) {
                Vector<Vector<Character>> program = new Vector<>(new Vector<>());
                Vector<Page> pageTable = proces.getPageTable();
                int maxPageID = proces.code.size() / 16;
                for (int currentPageID = 0; currentPageID < maxPageID; currentPageID++) {
                    Vector<Character> page = new Vector<>();
                    pageTable.add(new Page());
                    for (int j = 0; j < 16; j++) {
                        try {
                            page.add(proces.code.get(j + currentPageID * 16));
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    program.add(page);
                    putInfoToPageTable(proces.ID, pageTable);
                    putProcessToPageFile (proces.ID, program);
                    putPageInRam(proces, proces.ID, 0);

                }
            }
            static void putPageInRam(PCB proces,int procID, int pageID) {

                Vector<Page> pageTable = proces.getPageTable();
                Vector<Character> Programwstornie = PageFile.get(procID).get(pageID);
               System.out.println(pageID +" "+  Programwstornie);
                int i =0;
                if(victimQueue.size() <32)
                {
                    System.out.println("Mamy ramke");
                    for(int fID = 0; fID < 32; fID++)
                    {
                        if(putPageIn(fID, Programwstornie))
                            {
                                updatePageTables(procID, pageID, fID, true);
                                victimQueue.add(fID);
                                break;
                            }
                        }

                }
                else {
                    /**Jeśli nie ma ramki musimy znalezc ofiarę*/
                    while (i != victimQueue.size())
                    { if(pageTable.get(i).valid = true)
                    {
                        pageTable.get(i).valid= false;
                        victimQueue.add(pageTable.get(i).nrramki);

                    }
                    else
                    {
                        System.out.println("Wyjmujemy stone");
                        takepageout(proces,pageID);
                        if(putPageIn(pageTable.get(i).nrramki,Programwstornie))
                        {
                            updatePageTables(procID, pageID, pageTable.get(i).nrramki, true);
                            victimQueue.add(pageTable.get(i).nrramki);
                        }
                    }
                        i++;
                    }
                }

            }
            static boolean putPageIn(int FrameID, Vector<Character> Page)
            {
            /**Method that puts certain page from pageFile into RAM*/
                return Ram.writeFrame(Page, FrameID);
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

            void putProcessToPageFile(int pID,  Vector<Vector<Character>> program)
            {
                PageFile.put(pID, program);
            }

            //Uzywana przez ram żeby dostać konkretny numer ramki w której znaduje się stronica
            //pcb do odebrania tablicy storinic procesu
            static int demandPage(PCB proces, int PageID)
            {
                if (!PageTables.get(proces.ID).get(PageID).valid)
                {
                    putPageInRam(proces,proces.ID, PageID);
                }
                return PageTables.get(proces.ID).get(PageID).nrramki;
            }

            static void takepageout(PCB proces,int frameID)
            {    Vector<Page> pageTable = proces.getPageTable();
                int pageID = 0;
              for(int i =0; i<pageTable.size();i++)
              {
                  if( pageTable.get(i).nrramki == frameID)
                   pageID = i;
              }
                Vector <Character> page;
                page = Ram.readFrame(frameID);

                putPageInPageFile(pageID, proces.ID, page);
                updatePageTables(-1, -1, -1, false);
            }
            static void updatePageTables(int procID, int pageID, int frameID, boolean value)
            {
                PageTables.get(procID).get(pageID).nrramki = frameID;
                PageTables.get(procID).get(pageID).valid = value;
            }
            static void putPageInPageFile(int pageID, int procID, Vector<Character> page)
            {
                Vector<Vector<Character>> tmp =  PageFile.get(procID);
                tmp.set(pageID, page);
                PageFile.put(procID, tmp);
            }
            void putInfoToPageTable(int pID, Vector<Page> pageTable)
            {
                PageTables.put(pID,  pageTable);
            }
            static boolean processExists(int procID){
                return PageFile.containsKey(procID);
            }


            /**Funkcje dla Ciechana*/
             int matchPage(PCB proces,int pageID)
             { Vector<Page> pageTable = proces.getPageTable();
                if(pageTable.get(pageID).valid)
                {
                    System.out.println("Stronica jest w ramie");
                       return pageTable.get(pageID).nrramki;
                }
               else putPageInRam(proces,proces.ID,pageID);
                   return 0;
             }
             int find(PCB proces,int adress)
             {
                 int pageid=((adress - adress %16)/16);
                 int frameid = matchPage(proces,pageid);
                 return  frameid;
             }
             char readChar(PCB proces,int adress)
             {
                 int frameid = find(proces,adress);
                 char czytany = memory.readFromFrame(adress,frameid);
                 return  czytany;
             }


            /**Metody do wyświetlania*/

            public static void printPageTable (int processID)
            {
                if(processExists(processID))
                {
                    for(int i=0; i<PageTables.get(processID).size(); i++)
                    {
                        System.out.println(i + "   \t" + PageTables.get(processID).get(i).nrramki + "   \t" + PageTables.get(processID).get(i).valid);
                    }
                } else System.out.println("Error: process with given ID doesn't exist.");
            }
            public static void printQueue()
            {
                Queue<Integer> tmp = new LinkedList<>(victimQueue);

                for(int i=0; i< tmp.size(); i++)
                {
                    System.out.println(tmp.poll() + " ");
                }
                System.out.println(" ");
            }
            public static void printPageFile(int processID)
            {
                if(processExists(processID))
                {
                    Vector<Vector<Character>> pages = PageFile.get(processID);
                    for(int i=0; i<pages.size(); i++)
                    {
                        System.out.println("Page no."+i);
                        for(int j=0; j<pages.get(i).size(); j++)
                        {
                            System.out.println(String.format("%d", pages.get(i).get(j))+"\t");
                        }
                        System.out.println("");
                    }
                    System.out.println("");
                } else System.out.println("Error: process with given ID doesn't exist.");
            }
