package Interpreter;
import javax.swing.*;
import java.util.*;

public class Disc
{
    private static int discSize = 1024; //rozmiar dysku
    private static int blockSize = 32; //rozmiar bloku
    private static int blocksAmount = 32; //ilość bloków na dysku
    private static int freeBlockAmount = 32; //ilość wolnych bloków (startowo)
    private static int inodesTableSize = 32; //rozmiar tablicy i-węzłów

    public char[] disc;
    public static Inode[] inodes_table;
    public BitSet bitArray;
    public Hashtable<String,File> catalog;

    //KONTENER POMOCNICZY - MOŻE SIĘ NIE PRZYDAĆ, A MOŻE SIĘ SPIERDOLIĆ
    public ArrayList<Integer> IndexBlocks;

    public Disc()
    {
        disc = new char [discSize];
        bitArray = new BitSet(blocksAmount);
        inodes_table = new Inode[inodesTableSize];
        catalog = new Hashtable<>(32);

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

    public static Inode[] getInodeTableInstance(){
        return inodes_table;
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

    public void createFile(String name, String userName) throws Exception //działa
    {
        int space = findFreeSpace();
        if (catalog.get(name) != null) //kiedy nazwa zostala znaleziona w katalogu
        {
            Arrays.fill(disc, space * blockSize, space * blockSize + blockSize, (char)(0));
            throw new Exception("Plik o takiej nazwie juz istnieje"); //tu dodać swój wyjątek
        }
        else if ((catalog.get(name) == null) && (space != -1))
        {
            File file = new File();
            int num = freeInodeNum();

            //*INODE*
            Inode inode = new Inode();
            inode.state = false;
            inode.lock = new Lock();
            inode.LinkCounter = 1; //pierwsze dowiązanie
            inode.size = 0; //poczatkowo 0, bo nic nie zawiera
            inode.owner = new User(userName);
            inode.permissions = new Perm_List(userName);
            //inode.permissions.printAllPermission();

            //PIERWSZY NUMER BLOKU DYSKOWEGO//
            inode.blocks[0] = space; // nadanie nr bloku bezposredniego
            //poczatkowo nie jest wykorzystywane adresowanie indeksowe
            //ponieważ size = 0 to tylko jeden blok zajety - zarezerwowany
            inode.blocks[1] = -1;

            //*FILE*
            file.name = name;
            file.number = num;
            file.positionPtr = 0;
            file.type = File.Types.FILE;
            catalog.put(name, file);
            //Wpisanie do tablicy i-węzłów
            inodes_table[file.number] = inode;

            System.out.println("Plik został utworzony"); //testowe sprawdzenie
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
            File file = catalog.get(name);
            int i = file.number;
            if (inodes_table[i] != null)
            {
                Shell.getOpenFileTab().addFile(pcb, file);
                file.positionPtr = 0;
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
            File file = catalog.get(name);
            int i = file.number;
            if (inodes_table[i] != null)
            {
                Shell.getOpenFileTab().removeFile(pcb, file);
                file.positionPtr = 0;
            }
        }
        else
        {
            throw new Exception("Plik o takiej nazwie nie istnieje");
        }
    }

    public void writeFile(String name, String userName, String data) throws Exception //działa
    {
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            if (inodes_table[num] != null)
            {
                for (int i = 0; i < inodes_table[num].permissions.PermList.size(); i++)
                {
                    if(inodes_table[num].permissions.PermList.get(i).getUserID().equals(userName) && inodes_table[num].permissions.PermList.get(i).getRW() >= 2)
                    {
                        if (inodes_table[num].state)
                        {
                            file.positionPtr = 0;
                            if(data.length() <= 32)
                            {
                                int direct = inodes_table[num].blocks[0];
                                //Arrays.fill(disc, direct * blockSize, direct * blockSize + blockSize, (char)(-1));
                                for (int k = 0; k < data.length(); k++)
                                {
                                    disc[direct*32+k] = data.charAt(k);
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
                                //Arrays.fill(disc, inDirect * blockSize, inDirect * blockSize + blockSize, (char)(-1));

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
                                    for(int k = 0; index < data.length(); k++, index++)
                                    {
                                        disc[x*32+k] = data.charAt(k);
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
                        throw new Exception("Brak uprawnien");
                    }
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
        System.out.println("Zapisano dane do pliku");
    }
    public void appendFile(String name, String userName, String newData) throws Exception //działa, zobaczymy jak długo XD
    {
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            if(inodes_table[num] != null)
            {
                for(int l = 0; l < inodes_table[num].permissions.PermList.size(); l++)
                {
                    if(inodes_table[num].permissions.PermList.get(l).getUserID().equals(userName) && inodes_table[num].permissions.PermList.get(l).getRW() >= 2)
                    {
                        if (inodes_table[num].state)
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
                            if(!flag)
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
                        throw new Exception("Brak uprawnien");
                    }
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
        System.out.println("Zapisano dane na koniec pliku");
    }

    //amount - ilość znaków, które chcemy sczytać
    public String readFile(String name, String userName, int amount) throws Exception
    {
        String ret = "";
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            if(inodes_table[num] != null)
            {
                for(int l = 0; l < inodes_table[num].permissions.PermList.size(); l++)
                {
                    if(inodes_table[num].permissions.PermList.get(l).getUserID().equals(userName) && (inodes_table[num].permissions.PermList.get(l).getRW() == 1 || inodes_table[num].permissions.PermList.get(l).getRW() == 3))
                    {
                        if (inodes_table[num].state)
                        {
                            String c = "";
                            int s = inodes_table[num].size;
                            if(s <= 32)
                            {
                                int direct = inodes_table[num].blocks[0];
                                for(int i = file.positionPtr; i < (amount >(s - file.positionPtr)?s:amount+file.positionPtr); i++)
                                {
                                    c += disc[direct*32+i];
                                }
                                if(amount+file.positionPtr >= s)
                                {
                                    file.positionPtr = s;
                                }
                                else
                                {
                                    file.positionPtr += amount;
                                }
                            }
                            else
                            {
                                int direct = inodes_table[num].blocks[0];
                                int inDriect = inodes_table[num].blocks[1];
                                int tmp = amount;
                                int in = file.positionPtr;
                                int in_last = in;
                                if(in < 32)
                                {
                                    for(;in < (amount>(32 - file.positionPtr)?32:amount+file.positionPtr); in++)
                                    {
                                        c += disc[direct*32+in];
                                        --tmp;
                                    }
                                }
                                if(tmp>(32-in) && in < s)
                                {
                                    int restS = tmp;
                                    int tmp2;
                                    if(restS > s - 32)
                                    {
                                        tmp = s - 32;
                                        restS = tmp;
                                    }
                                    tmp2 = tmp;
                                    int j = (((in)/32)-1);
                                    int z = j;

                                    int firstL = (in - 32 * (j + 1));
                                    int i;

                                    int con = (restS > s - in ? (in + s - in) / 32 : (in + restS) / 32);
                                    int dif = con - j;
                                    for(; j < con; j++)
                                    {
                                        int w = disc[inDriect*32+j];
                                        for(i = firstL; i < (dif > 1 ? 32 : (tmp >= (s-in)?(firstL+(s-in)):(firstL+tmp))); i++)
                                        {
                                            c += disc[w*32+i];
                                            --tmp2;
                                        }
                                        if(firstL == 0)
                                        {
                                            in+=i;
                                        }
                                        else
                                        {
                                            in+=(i-firstL);
                                        }
                                        dif--;
                                        firstL=0;
                                        tmp=tmp2;
                                    }
                                }
                                if(amount+in_last >= s)
                                {
                                    file.positionPtr = s;
                                }
                                else
                                {
                                    file.positionPtr += amount;
                                }
                            }
                            ret = c + "\n";
                            if(amount >= inodes_table[num].size)
                            {
                                file.positionPtr = 0;
                            }
                        }
                        else
                        {
                            throw new Exception("Plik nie jest otwarty");
                        }
                    }
                    else
                    {
                        throw new Exception("Brak uprawnien");
                    }
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
        System.out.println("Odczytywanie zakonczone powodzeniem");
        return ret;
    }

    public void deleteFile(String name, String userName) throws Exception
    {
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            if(inodes_table[num] != null)
            {
                if(!inodes_table[num].permissions.PermList.isEmpty())
                {
                    //inodes_table[num].permissions.printAllPermission();
                    int sizeP = inodes_table[num].permissions.PermList.size();
                    for(int l = 0; l < sizeP; l++)
                    {
                        if(inodes_table[num].permissions.PermList.get(l).getUserID().equals(userName) && inodes_table[num].permissions.PermList.get(l).getRW() == 3)
                        {
                            if (!inodes_table[num].state)
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
                            throw new Exception("Brak uprawnien");
                        }
                        sizeP--;
                    }
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
        System.out.println("Usunieto plik " + name);
    }

    public void createLink(String name, String newName) throws Exception
    {
        if(catalog.get(name) != null)
        {
            if(catalog.get(newName) == null)
            {
                File file = catalog.get(name);
                File newFile = new File();
                newFile.name = newName;
                newFile.number = file.number;
                newFile.positionPtr = file.positionPtr;
                inodes_table[file.number].LinkCounter++;
                newFile.type = File.Types.LINK;
                catalog.put(newName,newFile);
            }
            else
            {
                throw new Exception("Nazwa zajeta");
            }
        }
        else
        {
            throw new Exception("Plik o podanej nazwie nie istnieje");
        }
        System.out.println("Utworzono dowiazanie");
    }

    public void renameFile(String name, String userName, String newName) throws Exception
    {
        if(catalog.get(name) != null)
        {
            if(catalog.get(newName) == null)
            {
                File file = catalog.get(name);
                int num = file.number;
                if(inodes_table[num] != null)
                {
                    for(int l = 0; l < inodes_table[num].permissions.PermList.size(); l++)
                    {
                        if(inodes_table[num].permissions.PermList.get(l).getUserID().equals(userName) && inodes_table[num].permissions.PermList.get(l).getRW() == 3)
                        {
                            if (!inodes_table[num].state)
                            {
                                catalog.get(name).name = newName;
                                catalog.put(newName, file);
                                catalog.remove(name);
                            }
                            else
                            {
                                throw new Exception("Plik jest otwarty");
                            }
                        }
                        else
                        {
                            throw new Exception("Brak uprawnien");
                        }
                    }
                }
                else
                {
                    throw new Exception("Dany i-wezel nie istnieje");
                }
            }
            else
            {
                throw new Exception("Nazwa zajeta");
            }
        }
        else
        {
            throw new Exception("Plik o podanej nazwie nie istnieje");
        }
        System.out.println("Zmiana nazwy zakonczona powodzeniem");
    }

    public void ListDirectory()
    {
        System.out.println("||||||||||||| KATALOG |||||||||||||\n");
        for(Map.Entry<String, File> e : catalog.entrySet())
        {
            File file = e.getValue();
            int num = file.number;
            System.out.println("*********************************");
            System.out.println("  " + e.getKey());
            System.out.println("  " + inodes_table[num].size + " B");
            System.out.println("  " + file.type);
            System.out.println("  Stan: " + inodes_table[num].state);
            System.out.println("  Numer bloku bezposredniego: " + inodes_table[num].blocks[0]);
            System.out.println("  Numer bloku indeksowego: " + inodes_table[num].blocks[1]);
            if(inodes_table[num].blocks[1] > -1)
            {
                int tmp = 0;
                System.out.print("  Zawartosc bloku indeksowego: ");
                while((int)disc[inodes_table[num].blocks[1]*32+tmp] != 65535 && inodes_table[num].blocks[1] != -1)
                {
                    int f = disc[inodes_table[num].blocks[1]*32+tmp];
                    System.out.print(f + " ");
                    tmp++;
                }
                System.out.println("\n*********************************\n");
            }
            }

        System.out.println("|||||||||||||||||||||||||||||||||||\n");
    }

    public void printDisc()
    {
        System.out.println("|||||||||||||||||||||||||||||| DYSK ||||||||||||||||||||||||||||||\n");
        int tmp = 0;
        for(int i = 0; i < 1024; i++)
        {
            System.out.print(disc[i] + "|");
            if(tmp % 32 == 31)
            {
                System.out.print("\n");
            }
            tmp++;
        }
        System.out.println("\n||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n");
    }

    public void printSingleInode(String name) throws Exception
    {
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            for(int i = 0; i < inodesTableSize; i++)
            {
                if(inodes_table[num] != null)
                {
                    if(num == i)
                    {
                        System.out.println("||||||||||||| POJEDYNCZY I-WEZEL |||||||||||||\n");
                        System.out.println("*********************************");
                        inodes_table[i].printInode();
                        System.out.println("*********************************");
                        System.out.println("||||||||||||||||||||||||||||||||||||||||||||||\n");
                    }
                }
                else
                {
                    throw new Exception("Dany i-wezel nie istnieje");
                }
            }
        }
        else
        {
            throw new Exception("Plik o podanej nazwie nie istnieje");
        }
    }

    public void printInodeTable()
    {
        for(int i = 0; i < inodesTableSize; i++)
        {
            if(inodes_table[i] != null)
            {
                System.out.println("||||||||||||| TABLICA I-WEZLOW |||||||||||||\n");
                System.out.println("*********************************");
                inodes_table[i].printInode();
                System.out.println("*********************************");
                System.out.println("||||||||||||||||||||||||||||||||||||||||||||||\n");
            }
            //else
            //{
                //throw new Exception("Dany i-wezel nie istnieje");
            //}
        }
    }

    public Perm_List getPermissions(String name)
    {
        if(catalog.get(name) != null)
        {
            File file = catalog.get(name);
            int num = file.number;
            if(inodes_table[num] != null)
            {
                return inodes_table[num].permissions;
            }
        }
        else
        {
            return null;
        }
        return null;
    }
}