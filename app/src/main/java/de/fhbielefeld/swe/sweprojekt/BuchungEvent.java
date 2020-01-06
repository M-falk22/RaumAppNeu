package de.fhbielefeld.swe.sweprojekt;

public class BuchungEvent{
    String RaumName;
    int ZeitStunde;
    int ZeitMinute;
    boolean Abbruch;
    boolean NutzerWechsel;
    boolean StornierSicht;

    public BuchungEvent(String raumName, int zeitStunde, int zeitMinute, boolean abbruch, boolean nutzerWechsel, boolean stornierSicht) {
        RaumName = raumName;
        ZeitStunde = zeitStunde;
        ZeitMinute = zeitMinute;
        Abbruch = abbruch;
        NutzerWechsel = nutzerWechsel;
        StornierSicht = stornierSicht;
    }
}
