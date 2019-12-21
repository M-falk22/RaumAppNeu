package de.fhbielefeld.swe.sweprojekt;

public class Raum {
    int Nummer;
    int AnzahlBuchungen;
    Buchung[] Buchungen = new Buchung[10];

    public Raum()
    {
        Nummer = 0;
        AnzahlBuchungen = 0;
    }

    public Raum(int pNummer)
    {
        Nummer = pNummer;
        AnzahlBuchungen = 0;
    }

    public void BuchenStartzeit(int pStartzeitStunde, int pStartzeitMinute)
    {
        Buchungen[AnzahlBuchungen] = new Buchung(pStartzeitStunde, pStartzeitMinute);
        AnzahlBuchungen++;
    }

    public void BuchenEndzeit(int pEndzeitStunde, int pEndzeitMinute, int pIndex)
    {
        Buchungen[pIndex].setEndzeitStunde(pEndzeitStunde);
        Buchungen[pIndex].setEndzeitMinute(pEndzeitMinute);
    }

    public int getAnzahlBuchungen()
    {
        return AnzahlBuchungen;
    }

    public Buchung getBuchung(int pIndex)
    {
        if(pIndex < AnzahlBuchungen)
        {
            return (Buchungen[pIndex]);
        }

        //TODO: Exception stuff hier machen?

        return null;
    }
}

