import Memory.Memory;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 512; i++){
            Memory.write('P', i);
        }
        Memory.printRawRam();
        Memory.writeToFrame(' ', 3, 0);
        for (int i = 0; i < 16; i++){
            Memory.writeMC('x', i);
        }
        Memory.printRawRam();
        for (int i = 0; i < 16; i++){
            Memory.writePipe('Z', i);
        }
        Memory.clearMC();
        Memory.printRawRam();
        int y = Memory.writeNumMC(134, 0);
        System.out.println(y);
        Memory.printRawRam();
        int x = Memory.readNumMC(0);
        System.out.println(x);
        Memory.writeNumMC(222,0+y);
        Memory.printRawRam();
        x = Memory.readNumMC(0+y);
        System.out.println(x);
    }
}
