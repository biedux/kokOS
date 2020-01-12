package Interpreter;
class Registry {
    //definicje potrzebnych pol
    int registerA;
    int registerB;
    int registerC;
    int registerD;
    int counter;
    public Registry(){ //
        registerA = 0;       //glowny rejestr
        registerB = 0;       //pomocniczy
        registerC = 0;       //tu bedzie ilosc petli
        registerD = 0;       //inne dodatkowe
        counter = 0;         //licznik rozkazów
    }

    //pobieranie stanu rejestrow i licznika
    int getA(){
        return registerA;
    }
    int getB(){
        return registerB;
    }
    int getC(){
        return registerC;
    }
    int getD(){
        return registerD;
    }
    int getCounter(){
        return counter;
    }

    //ustawianie stanu rejestrow i licznika
    void setA(int a){
        registerA = a;
    }
    void setB(int b){
        registerB = b;
    }
    void setC(int c){
        registerC = c;
    }
    void setD(int d){
        registerD = d;
    }
    void setCounter(int counter_){
        counter = counter_;
    }

}