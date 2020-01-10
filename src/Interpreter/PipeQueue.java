package Interpreter;

import java.util.*;
public class PipeQueue
{
    public int descW;
    public int descR;
    public Queue<Character> eQueue = new ArrayDeque<>(64);
    public PipeQueue(int a, int b)
    {
        descW=a;
        descR=b;
    }

}