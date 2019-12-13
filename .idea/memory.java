package put.poznan.pl;
//JEDZIEMY CHARAMI

        public class Memory {
            private static char[] memory = new char[256];


            //ZAPIS POJEDYNCZEGO BAJTU
            void write(char data, int i){
                try {
                    memory[i] = data;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }


            //ODCZYT POJEDYNCZEGO BAJTU
            public char read(int i) {
                try {
                    return memory[i];
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                    return 0;
                }
            }


            //tu bedzie ZAPIS STRONICY W RAMCE
            public static boolean writeFrame(Vector<Char> data, int frame){
                if ((frame < 0) || (frame > 15)){
                    return false;
                }
                for (int i = 0; i < data.size(); i++){
                    memory[frame * 16 + i] = data[i];
                }
                return true;
            }




            //tu bedzie ODCZYT STRONICY Z RAMKI
            public static Vector<Char> readFrame(int frame){
                Vector<Char> odczyt = new Vector<Char>();
                try {
                    for (int i = 0; i < 16; i++){
                        odczyt.add(memory[frame * 16 + i]);
                    }
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return odczyt;
            }



            //tu bedzie WYSWIETLENIE ZAWARTOSCI RAMU (do debugowania)
            public static void printRawRam() {
                for (int i = 0; i < 16; i++) {
                    System.out.println(memory[i]);
                    for (int j = 0; j < 16; j++) {
                        System.out.println(String.format(memory[i * 16 + j]));
                    }
                }
                System.out.println("");

                Utils.log("showing raw contents of RAM");
            }



            //tu bedzie ZAPIS BAJTU ZGODNIE Z TABLICA STRONIC




            //tu bedzie ODCZYT BAJTU ZGODNIE Z TABLICA STRONIC
        }