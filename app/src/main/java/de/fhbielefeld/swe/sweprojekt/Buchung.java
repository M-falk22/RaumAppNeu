package de.fhbielefeld.swe.sweprojekt;

public class Buchung {
    int StartzeitStunde;
    int StartzeitMinute;
    int EndzeitStunde;
    int EndzeitMinute;
    String RaumName;
    //boolean Vollstaendig; //NOTE(Moritz): Erstmal nur in Bezug auf die Zeit, ungeachtet der NutzerID

    public Buchung() {
        StartzeitStunde = 0;
        StartzeitMinute = 0;
        EndzeitStunde = 0;
        EndzeitMinute = 0;
        RaumName = "UNBENANNT";
        //Vollstaendig = false;
    }

    public Buchung(int startzeitStunde, int startzeitMinute, int endzeitStunde, int endzeitMinute) {
        StartzeitStunde = startzeitStunde;
        StartzeitMinute = startzeitMinute;
        EndzeitStunde = endzeitStunde;
        EndzeitMinute = endzeitMinute;
        RaumName = "UNBENANNT";

    }

    public Buchung(int startzeitStunde, int startzeitMinute) {
        StartzeitStunde = startzeitStunde;
        StartzeitMinute = startzeitMinute;
        EndzeitStunde = StartzeitStunde+1;
        EndzeitMinute = StartzeitMinute;
       // Vollstaendig = true;
    }

    public Buchung(int startzeitStunde, int startzeitMinute, int endzeitStunde, int endzeitMinute, int nutzerID) {
        StartzeitStunde = startzeitStunde;
        StartzeitMinute = startzeitMinute;
        EndzeitStunde = endzeitStunde;
        EndzeitMinute = endzeitMinute;
    }

    public int getStartzeitStunde() {
        return StartzeitStunde;
    }

    public void setStartzeitStunde(int startzeitStunde) {
        StartzeitStunde = startzeitStunde;
    }

    public int getStartzeitMinute() {
        return StartzeitMinute;
    }

    public void setStartzeitMinute(int startzeitMinute) {
        StartzeitMinute = startzeitMinute;
    }

    public int getEndzeitStunde() {
        return EndzeitStunde;
    }

    public void setEndzeitStunde(int endzeitStunde) {
        EndzeitStunde = endzeitStunde;
    }

    public int getEndzeitMinute() {
        return EndzeitMinute;
    }

    public void setEndzeitMinute(int endzeitMinute) {
        EndzeitMinute = endzeitMinute;
    }

    public void setRaumName(String raumName) { RaumName = raumName; }
}
