import memory.memory;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 512; i++){
            memory.write('P', i);
        }
        memory.printRawRam();
        memory.writeToFrame(' ', 3, 0);
        for (int i = 0; i < 16; i++){
            memory.writeMC('x', i);
        }
        memory.printRawRam();
        for (int i = 0; i < 16; i++){
            memory.writePipe('Z', i);
        }
        memory.clearMC();
        memory.printRawRam();
    }
}
