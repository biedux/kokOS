package put.poznan.pl;


        public class Memory {
            private static byte[] memory = new byte[256];


            //ZAPIS POJEDYNCZEGO BAJTU
            void write(byte data, int i){
                try {
                    memory[i] = data;
                }
                catch () {
                    //tutaj jak cos jebnie
                }
            }


            //ODCZYT POJEDYNCZEGO BAJTU
            public byte read(int i) {
                try {
                    return memory[i];
                }
                catch () {
                    //tu jak jebnie
                    return 0;
                }
            }


            //tu bedzie ZAPIS STRONICY W RAMCE
            public static boolean writeFrame(Vector<Byte> data, int frame){
                if ((frame < 0) || (frame > 15)){
                    return false;
                }
                for (int i = 0; i < data.size(); i++){
                    memory[frame * 16 + i] = memory.get(i);
                }
                return true;
            }




            //tu bedzie ODCZYT STRONICY Z RAMKI




            //tu bedzie WYSWIETLENIE ZAWARTOSCI RAMU (do debugowania)




            //tu bedzie ZAPIS BAJTU ZGODNIE Z TABLICA STRONIC




            //tu bedzie ODCZYT BAJTU ZGODNIE Z TABLICA STRONIC
        }