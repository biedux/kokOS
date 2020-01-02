import memory.memory;

public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 512; i++){
            memory.write('X', i);
        }
        memory.printRawRam();
        memory.writeToFrame(' ', 3, 0);
        memory.printRawRam();
    }
}
