package Pipes;
import java.util.*;
public class PipeQueue
{
    public int descW;
    public int descR;
    public Queue<char> eQueue = new ArrayDeque<>(64);
    public PipeQueue(int a, int b)
    {
        descW=a;
        descR=b;
    }

}
