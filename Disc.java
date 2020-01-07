package com.poznan.put;

import javax.swing.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Arrays;
import java.util.Hashtable;

public class Disc
{
    private static int discSize = 1024; //rozmiar dysku
    private static int blockSize = 32; //rozmiar bloku
    private static int blocksAmount = 32; //ilość bloków na dysku
    private static int freeBlockAmount = 32; //ilość wolnych bloków (startowo)
    private static int inodesTableSize = 32; //rozmiar tablicy i-węzłów

    public char[] disc;
    public Inode[] inodes_table;
    public BitSet bitArray;
    public Hashtable<String,File> catalog;
    public ArrayList<File> open_file_table;

    //KONTENER POMOCNICZY - MOŻE SIĘ NIE PRZYDAĆ, A MOŻE SIĘ SPIERDOLIĆ
    public ArrayList<Integer> IndexBlocks;

    public Disc()
    {
        disc = new char [discSize];
        bitArray = new BitSet(blocksAmount);
        inodes_table = new Inode[inodesTableSize];
        catalog = new Hashtable<String,File>(32);
        open_file_table = new ArrayList<File>();

        // Wypełnianie tablicy bitów wartościami 1 (true) - wszystkie bloki startowo wolne
        for(int i = 0; i < blocksAmount; i++)
        {
            bitArray.set(i);
        }

        //Wypełnianie dysku znakami 0
        for(int i = 0; i < discSize; i++)
        {
            disc[i] = (char)0;
        }
        IndexBlocks = new ArrayList<>();
    }

    public int findFreeSpace()
    {
        for (int i = 0; i < blocksAmount; i++)
        {
            if (bitArray.get(i))
            {
                //zajmowanie zajętego bloku
                bitArray.clear(i); //ustawiam bit na 0 (false) o indeksie równemu danemu nr bloku
                Arrays.fill(disc, i * blockSize, i * blockSize + blockSize, (char)(-1)); //wypełniam dany blok -1
                --freeBlockAmount; //zmniejszam ilość wolnych bloków
                return i;
            }
        }
        return -1; //Wszystkie bloki zajęte
    }

    private int freeInodeNum()
    {
        for (int i = 0; i < 32; i++)
        {
            if (inodes_table[i] == null)
            {
                return i; //indeks wolnego i-węzła
            }
        }
        return -1; //brak wolnych i-węzłów
    }

    public void createFile(String name, String userName) throws Exception //działa (jeszcze bez uprawnień)
    {
        int space = findFreeSpace();
        if (catalog.get(name) != null) //kiedy nazwa zostala znaleziona w katalogu
        {
            throw new Exception("Plik o takiej nazwie juz istnieje"); //tu dodać swój wyjątek
        }
        else if ((catalog.get(name) == null) && (space != -1))
        {
            File file = new File();
            int num = freeInodeNum();
            Inode inode = new Inode();
            inode.state = false;
            inode.lock = new Lock();
            inode.LinkCounter = 1; //pierwsze dowiązanie
            inode.size = 0; //poczatkowo 0, bo nic nie zawiera
            inode.user = new User(userName);

            //PIERWSZY NUMER BLOKU DYSKOWEGO//
            inode.blocks[0] = space; // nadanie nr pierwszego bloku bezposredniego
            //poczatkowo nie jest wykorzystywane adresowanie indeksowe
            //ponieważ size = 0 to tylko jeden blok zajety
            inode.blocks[1] = -1;

            file.name = name;
            file.number = num;
            file.positionPtr = 0;
            catalog.put(name, file);
            //Wpisanie do tablicy i-węzłów
            inodes_table[file.number] = inode;

            System.out.println("Plik został utworzony\n"); //testowe sprawdzenie
        }
        else if (space == -1)
        {
            throw new Exception("Brak miejsca na dysku"); //tu dodać swój wyjątek
        }
    }

    public void openFile(String name, PCB pcb) throws Exception //działa
    {
        if (catalog.get(name) != null)
        {
            for (int i = 0; i < 32; i++)
            {
                if (inodes_table[i] != null)
                {
                    if (inodes_table[i].state == true || inodes_table[i].lock.tryLock() == true)
                    {
                        throw new Exception("Plik jest otwarty"); //tu dodać swój wyjątek
                    }
                    else
                    {
                        inodes_table[i].lock.acquire(pcb);
                        inodes_table[i].state = true;
                        File file = catalog.get(name);
                        open_file_table.add(file);
                    }
                }
            }
        }
        else
        {
            throw new Exception("Plik o takiej nazwie nie istnieje");
        }
    }

    public void closeFile(String name, PCB pcb) throws Exception //działa
    {
        if (catalog.get(name) != null)
        {
            for (int i = 0; i < 32; i++)
            {
                if (inodes_table[i] != null)
                {
                    if (inodes_table[i].state == false || inodes_table[i].lock.tryLock() == false)
                    {
                        throw new Exception("Plik nie jest otwarty"); //tu dodać swój wyjątek
                    }
                    else
                    {
                        inodes_table[i].lock.release(pcb);
                        inodes_table[i].state = false;
                        File file = catalog.get(name);
                        open_file_table.remove(file);
                    }
                }
            }
        }
        else
        {
            throw new Exception("Plik o takiej nazwie nie istnieje");
        }
    }

    public void writeFile(String name, String data) throws Exception //dopracowywane, ale działa
    {
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            if (inodes_table[num] != null)
            {
                if (inodes_table[num].lock.tryLock() == true || inodes_table[num].state == true)
                {
                    file.positionPtr = 0;
                    if(data.length() <= 32)
                    {
                        int direct = inodes_table[num].blocks[0];
                        for (int i = 0; i < data.length(); i++)
                        {
                            disc[direct*32+i] = data.charAt(i);
                        }
                        inodes_table[num].size = data.length();
                    }
                    else if(((data.length()+32-1)/32) <= freeBlockAmount) //dzięki temu wiemy czy nie przekracza ilości wolnych bloków
                    {
                        int restS = data.length() - 32;
                        int n = (restS+32-1)/32; //pomocnicze
                        int direct = inodes_table[num].blocks[0];
                        int inDirect = findFreeSpace();
                        IndexBlocks.add(inDirect);

                        inodes_table[num].blocks[1] = inDirect;
                        int index = 0;
                        for(; index < 32; index++)
                        {
                            disc[direct*32+index] = data.charAt(index);
                        }
                        for(int j = 0; j < n; j++)
                        {
                            disc[inDirect*32+j] = (char)findFreeSpace();
                        }
                        for(int j = 0; j < n; j++)
                        {
                            int x = disc[inDirect*32+j];
                            for(int i = 0; index < data.length(); i++, index++)
                            {
                                disc[x*32+i] = data.charAt(i);
                            }
                        }
                        inodes_table[num].size = data.length();
                    }
                    else
                    {
                        throw new Exception("Brak miejsca na dysku");
                    }
                }
                else
                {
                    throw new Exception("Plik nie jest otwarty");
                }
            }
            else
            {
                throw new Exception("Dany i-wezel nie istnieje");
            }
        }
        else
        {
            throw new Exception("Plik o podanej nazwie nie istnieje");
        }
    }
   public void appendFile(String name, String newData) throws Exception //działa, zobaczymy jak długo XD
   {
       if(catalog.get(name) != null)
       {
           File file = catalog.get(name);
           int num = file.number;
           if(inodes_table[num] != null)
           {
               if (inodes_table[num].lock.tryLock() == true || inodes_table[num].state == true)
               {
                   boolean flag = false; //sprawdzacz miejsca
                   int dataSize = inodes_table[num].size;
                   int newDataSize = newData.length();
                   int total = dataSize + newDataSize;
                   int direct;

                   if(total <= 32)
                   {
                       flag = true;
                       int index = 0;
                       direct = inodes_table[num].blocks[0];
                       for(int i = dataSize; index < newData.length(); index++, i++)
                       {
                           disc[direct*32+i] = newData.charAt(index);
                       }
                       inodes_table[num].size += newData.length();
                   }
                   else if (dataSize < 32 && ((newDataSize - (32 - dataSize) + 32 - 1) / 32) + 1 <= freeBlockAmount)
                   {
                       flag = true;
                       direct = inodes_table[num].blocks[0];
                       int index = 0;
                       for(int i = dataSize; i < 32; i++, index++)
                       {
                           disc[direct*32+i] = newData.charAt(index);
                           --newDataSize;
                       }

                       int n = (newDataSize + 32 - 1)/32; //pomocnicze
                       int inDirect = findFreeSpace();
                       IndexBlocks.add(inDirect);
                       inodes_table[num].blocks[1] = inDirect;

                       for(int j = 0; j < n; j++)
                       {
                           disc[inDirect*32+j] = (char)findFreeSpace();
                       }
                       for(int j = 0; j < n; j++)
                       {
                           int x = disc[inDirect*32+j];
                           for(int i = 0; index < newData.length(); i++, index++)
                           {
                               disc[x*32+i] = newData.charAt(index);
                           }
                       }

                       inodes_table[num].size += newData.length();
                   }
                   else
                   {
                       int restS = dataSize - 32;
                       int indexBlock = (restS + 32 - 1) / 32;
                       int inDirectBlock = inodes_table[num].blocks[1];
                       int amount = (indexBlock * 32) - restS; //ilość wolnych miejsc w bloku
                       if((((newData.length() - amount) + 32 - 1) / 32) <= freeBlockAmount)
                       {
                           flag = true;
                           if(newData.length() > amount)
                           {
                               int newBlocks = ((newData.length() - amount) + 32 - 1) / 32; //ilość bloków do nadpisu
                               int save = disc[inDirectBlock*32+indexBlock-1]; //liczone od 0
                               int index = 0;
                               for(int i = 32 - amount; i < 32; i++, index++)
                               {
                                   disc[save*32+i] = newData.charAt(index);
                               }

                               for(int j = 0, tmp = indexBlock; j < newBlocks; j++, tmp++)
                               {
                                   disc[inDirectBlock*32+tmp] = (char)findFreeSpace();
                               }
                               for(int j = 0, tmp = indexBlock; j < newBlocks; j++, tmp++)
                               {
                                   int x = disc[inDirectBlock*32+tmp];
                                   for(int i = 0; index < newData.length(); i++, index++)
                                   {
                                       disc[x*32+i] = newData.charAt(index);
                                   }
                               }
                           }
                           else
                           {
                               int save = disc[inDirectBlock*32+indexBlock-1];
                               int nextIndex = 0;
                               for(int i = 32 - amount; nextIndex < newData.length(); i++, nextIndex++)
                               {
                                   disc[save*32+i] = newData.charAt(nextIndex);
                               }
                           }
                           inodes_table[num].size += newData.length();
                       }
                   }
                   if(flag == false)
                   {
                       throw new Exception("Brak miejsca na dysku");
                   }
               }
               else
               {
                   throw new Exception("Plik nie jest otwarty");
               }
           }
           else
           {
               throw new Exception("Dany i-wezel nie istnieje");
           }
       }
       else
       {
           throw new Exception("Plik o podanej nazwie nie istnieje");
       }
   }

   public void readFile(String name)
   {

   }

   public void deleteFile(String name) throws Exception
   {
       if(catalog.get(name) != null)
       {
           File file = catalog.get(name);
           int num = file.number;
           if(inodes_table[num] != null)
           {
               if (inodes_table[num].lock.tryLock() == false || inodes_table[num].state == false)
               {
                   if(inodes_table[num].LinkCounter > 1)
                   {
                       catalog.remove(name);
                       --inodes_table[num].LinkCounter;
                   }
                   else
                   {
                       int amountIn = inodes_table[num].size > 32 ? 2:1;
                       int direct;
                       int inDirect;
                       if(amountIn == 1)
                       {
                           direct = inodes_table[num].blocks[0];
                           Arrays.fill(disc, direct*32, direct*32+32, (char)0);
                           bitArray.set(direct);
                           inodes_table[num] = null;
                           catalog.remove(name);
                           ++freeBlockAmount;
                       }
                       else if (amountIn == 2)
                       {
                           direct = inodes_table[num].blocks[0];
                           Arrays.fill(disc, direct*32, direct*32+32, (char)0);
                           bitArray.set(direct);
                           ++freeBlockAmount;
                           inDirect = inodes_table[num].blocks[1];
                           int tmp = 0;

                           while((int)disc[inDirect*32+tmp] != 65535)
                           {
                               int f = disc[inDirect*32+tmp];
                               Arrays.fill(disc, f*32, f*32+32, (char)0);
                               bitArray.set(f);
                               ++freeBlockAmount;
                               tmp++;

                           }

                           Arrays.fill(disc, inDirect*32, inDirect*32+32, (char)0);
                           bitArray.set(inDirect);
                           inodes_table[num] = null;
                           catalog.remove(name);
                       }
                   }
               }
               else
               {
                   throw new Exception("Plik jest otwarty");
               }
           }
           else
           {
               throw new Exception("Dany i-wezel nie istnieje");
           }
       }
       else
       {
           throw new Exception("Plik o podanej nazwie nie istnieje");
       }
   }

}
