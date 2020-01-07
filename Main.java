package com.poznan.put;

public class Main {

    public static void main(String[] args) {
        try
        {
            Disc d = new Disc();
            d.createFile("dupa", "user1");
            PCB first = new PCB("first", "abc");
            d.openFile("dupa",first);
            d.writeFile("dupa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            d.closeFile("dupa", first);

            d.createFile("lol", "user1");
            PCB sec = new PCB("sec", "cba");
            d.openFile("lol",sec);
            d.writeFile("lol", "aaaaaaaaaaaaaaaaaaaaaaaaaa");
            d.closeFile("lol", sec);

            d.openFile("lol", first);
            d.appendFile("lol", "bbbbbbbb");
            d.closeFile("lol", first);

            //int x = d.findFreeSpace();
           // System.out.println(x);
           /* for(int i = 0; i < 32; i++)
            {
                d.inodes_table[i].printInode();
            }*/

            d.deleteFile("dupa");


           // for (int i = 0; i < 1024; i++)
            //{
              //  System.out.println(d.disc[i] + " ");
            //}

            for(int j = 0; j < d.IndexBlocks.size(); j++)
            {
                System.out.println(d.IndexBlocks.get(j) + "\n");
            }


            System.out.println("Bity po utworzeniu pliku:\n");
            for(int i = 0; i < 32; i++)
            {
                System.out.println(d.bitArray.get(i));
            }

            System.out.println("Tablica i-wezlow po utworzeniu:\n");
            d.inodes_table[1].printInode();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
