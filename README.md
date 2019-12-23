# kokOS
. Wydaje mi się, że mój kod jest skończony, pozostaje go przetestować i dodać tablice przeliczania priorytetów do wag, ale to już powinniśmy ustalić wspólnie. 

Działanie tej klasy wymaga:
1. Istnienie zmiennej int CurrTime => zmienna licząca ilość rozkazów assemblerowych, które wykonały się od uruchomienia programu. 
2. Zmian w PCB:
a) dodania do PCB zmiennych i dodania do nich getterów i setterów:
  private int BaseTime; 
  
  private int VirtualTime;
  private weight;
