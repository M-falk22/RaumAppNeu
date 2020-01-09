package de.fhbielefeld.swe.sweprojekt;

public class MainActivityCallbackBuchung {
    boolean StartzeitErfolg;
    boolean EndzeitErfolg;
    String InfoString;

    public MainActivityCallbackBuchung(boolean startzeitErfolg, boolean endzeitErfolg, String infoString) {
        StartzeitErfolg = startzeitErfolg;
        EndzeitErfolg = endzeitErfolg;
        InfoString = infoString;
    }
}
