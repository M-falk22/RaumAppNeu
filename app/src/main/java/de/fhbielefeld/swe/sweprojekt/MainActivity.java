package de.fhbielefeld.swe.sweprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Bus EventBus = new Bus();
    Raum[] RaumListe = new Raum[10];
    Nutzer[] NutzerListe = new Nutzer[10];
    int AnzahlRaum = 0;
    int RaumIndex = 0;
    int BuchungIndex = 0;
    int MomentanerNutzerIndex = 0;

    enum AppZustand
    {
        STARTZEITEINGABE,
        ENDZEITEINGABE
    }

    AppZustand Zustand = AppZustand.STARTZEITEINGABE;

    //GUI Stuff
    SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);

        mSectionsStatePagerAdapter.addFragment(new FragmentLogIn(), "LogIn");
        mSectionsStatePagerAdapter.addFragment(new FragmentBuchung(), "Buchung");

        mViewPager.setAdapter(mSectionsStatePagerAdapter);

        RaumListe[AnzahlRaum] = new Raum(AnzahlRaum++);

        if(Zustand.equals(AppZustand.STARTZEITEINGABE))
        {
            Zustand = AppZustand.ENDZEITEINGABE;

            RaumIndex = Integer.parseInt(RaumEingabe.getText().toString());

            if (RaumIndex < AnzahlRaum) {
                int StartzeitStunde = Picker.getHour();
                int StartzeitMinute = Picker.getMinute();

                RaumListe[RaumIndex].BuchenStartzeit(StartzeitStunde, StartzeitMinute);
                BuchungIndex = RaumListe[RaumIndex].getAnzahlBuchungen()-1; //nicht safe

                Button.setText("Endzeit festlegen");

                //NOTE(Moritz): Folgende drei Zeilen sind lediglich Test-Code...
                int AnzahlBuchungen = RaumListe[RaumIndex].AnzahlBuchungen;
                Buchung TestBuchung = RaumListe[RaumIndex].getBuchung(BuchungIndex);
                System.out.println("Raum: " + RaumListe[RaumIndex] + " Startzeit: " + TestBuchung.StartzeitStunde + ":" + TestBuchung.StartzeitMinute);
            }
        }
        else if(Zustand.equals(AppZustand.ENDZEITEINGABE))
        {
            Zustand = AppZustand.STARTZEITEINGABE;

            int EndzeitStunde = Picker.getHour();
            int EndzeitMinute = Picker.getMinute();

            RaumListe[RaumIndex].BuchenEndzeit(EndzeitStunde, EndzeitMinute, BuchungIndex);
        }

    }

    @Subscribe public void FragmentEvent(String pEvent)
    {
        if(pEvent.equals("BUCHEN"))
        {
            UpdateBuchung();
        }
    }

    public void UpdateBuchung();
    public void setViewPager(int FragmentIndex)
    {
        mViewPager.setCurrentItem(FragmentIndex);
    }

    public Raum[] getRaumListe() {
        return RaumListe;
    }

    public void setRaumListe(Raum[] raumListe) {
        RaumListe = raumListe;
    }

    public Nutzer[] getNutzerListe() {
        return NutzerListe;
    }

    public void setNutzerListe(Nutzer[] nutzerListe) {
        NutzerListe = nutzerListe;
    }

    public int getAnzahlRaum() {
        return AnzahlRaum;
    }

    public void setAnzahlRaum(int anzahlRaum) {
        AnzahlRaum = anzahlRaum;
    }

    public int getRaumIndex() {
        return RaumIndex;
    }

    public int getMomentanerNutzerIndex() {
        return MomentanerNutzerIndex;
    }

    public void setRaumIndex(int raumIndex) {
        RaumIndex = raumIndex;
    }

    public int getBuchungIndex() {
        return BuchungIndex;
    }

    public void setBuchungIndex(int buchungIndex) {
        BuchungIndex = buchungIndex;
    }

    public AppZustand getZustand() {
        return Zustand;
    }

    public void setZustand(AppZustand zustand) {
        Zustand = zustand;
    }

    public void setMomentanerNutzerIndex(int pMomentanerNutzerIndex)
    {
        MomentanerNutzerIndex = pMomentanerNutzerIndex;
    }

    public Bus getEventBus()
    {
        return EventBus;
    }
}