# kokOS
Pierdolnik jest w tym kodzie, ale jutro to ogarne i zaktualizuje. W sumie to co nadole jest zakomentowane jest niaktualne, ale to jutro zmienei i ułożę, żeby było git. 

Działanie tej klasy wymaga:
1. Istnienie zmiennej int CurrTime => zmienna licząca ilość rozkazów assemblerowych, które wykonały się od uruchomienia programu. 
2. Zmian w PCB:
a) dodania do PCB zmiennych i dodania do nich getterów i setterów:
  private int BaseTime; 
  private int CurrTime; // nie jestem pewny, czy ta zmienna będzie potrzebna, czy nie będzie zastąpiona zmieną globalną
  private int VirtualTime;
  private weight;
