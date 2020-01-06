package de.fhbielefeld.swe.sweprojekt;

public class Nutzer {
    String Name;
    int AnzahlBuchung = 0;
    Buchung[] Buchungen = new Buchung[10];

    public Nutzer(String name) {
        Name = name;
    }
}
