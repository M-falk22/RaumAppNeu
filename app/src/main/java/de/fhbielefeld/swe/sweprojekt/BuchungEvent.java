package de.fhbielefeld.swe.sweprojekt;

public class BuchungEvent{
    String RaumName;
    int ZeitStunde;
    int ZeitMinute;

    public BuchungEvent(String raumName, int zeitStunde, int zeitMinute) {
        RaumName = raumName;
        ZeitStunde = zeitStunde;
        ZeitMinute = zeitMinute;
    }
}
