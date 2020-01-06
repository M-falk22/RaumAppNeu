package de.fhbielefeld.swe.sweprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Bus EventBus = new Bus();
    //NOTE(Moritz): Alle Arrays sind zu demonstrativen Zwecken auf 10 Elemente beschränkt. Eine verlinkte Liste oder ähnliches zu nutzen, wäre in der Praxis besser.
    Raum[] RaumListe = new Raum[10];
    Nutzer[] NutzerListe = new Nutzer[10];
    int AnzahlNutzer = 0;
    int NutzerIndex;
    int AnzahlRaum = 0;
    String RaumName;
    int RaumIndex;
    int BuchungIndex = 0;
    int MomentanerNutzerIndex = 0;

    enum BuchungZustand
    {
        STARTZEITEINGABE,
        ENDZEITEINGABE
    }

    BuchungZustand Zustand = BuchungZustand.STARTZEITEINGABE;

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
        mSectionsStatePagerAdapter.addFragment(new FragmentStornierung(), "Stornierung");

        mViewPager.setAdapter(mSectionsStatePagerAdapter);

        //NOTE(Moritz): Räume sind Hardcoded. In der Praxis wäre es sicherlich schöner eine Input-Textdatei zu parsen und dementsprechend das Array zu erstellen...
        RaumListe[AnzahlRaum++] = new Raum("C013");
        RaumListe[AnzahlRaum++] = new Raum("D010");
        RaumListe[AnzahlRaum++] = new Raum("E406");

        EventBus.register(this);

    }

    @Subscribe public void FragmentBuchungEvent(BuchungEvent pEvent)
    {
        //TODO(Moritz): Abbruch-Möglichkeit einführen!
        if(Zustand.equals(BuchungZustand.STARTZEITEINGABE))
        {
            if(!pEvent.NutzerWechsel)
            {
                RaumName = pEvent.RaumName;

                RaumIndex = RaumArrayIndex(RaumName);

                if(RaumIndex >= 0)
                {
                    RaumListe[RaumIndex].BuchenStartzeit(pEvent.ZeitStunde, pEvent.ZeitMinute);

                    BuchungIndex = RaumListe[RaumIndex].getAnzahlBuchungen() - 1;

                    System.out.println("STARTZEITEINGABE erfolgreich!");
                    Zustand = BuchungZustand.ENDZEITEINGABE;
                }
                else
                {
                    System.out.println("STARTZEITEINGABE fehlgeschlagen! Ungueltiger Raum-Name!");
                }
            }
            else
            {
                setViewPager(0);
            }
            //Post event zur Button-Änderung bzw Toast-anzeigen!
        }
        else if(Zustand.equals(BuchungZustand.ENDZEITEINGABE))
        {
            Zustand = BuchungZustand.STARTZEITEINGABE;

            RaumListe[RaumIndex].BuchenEndzeit(pEvent.ZeitStunde, pEvent.ZeitMinute, BuchungIndex);
            //Post event zur Button-Änderung

            //NutzerListe[NutzerIndex].Buchungen[] <--- Buchung in NutzerBuchungsArray eintragen!

            System.out.println("ENDZEITEINGABE erfolgreich!");
        }
    }

    @Subscribe public void FragmentLogInEvent(LogInEvent pEvent)
    {
        NutzerIndex = NutzerArrayIndex(pEvent.NutzerName);
        if(NutzerIndex >= 0)
        {
            System.out.println("Angemeldet als: "+pEvent.NutzerName);
        }
        else
        {
            NutzerListe[AnzahlNutzer] = new Nutzer(pEvent.NutzerName);
            NutzerIndex = AnzahlNutzer++;
        }
    }

    public int NutzerArrayIndex(String pNutzerName)
    {
        int Result = -1;
        for(int NutzerCount = 0; NutzerCount < AnzahlNutzer; NutzerCount++)
        {
            if(NutzerListe[NutzerCount].Name.equals(pNutzerName))
            {
                Result = NutzerCount;
            }
        }
        return Result;
    }

    public int RaumArrayIndex(String pRaumName)
    {
        int Result = -1;
        for(int RaumCount = 0; RaumCount < AnzahlRaum; RaumCount++)
        {
            if(RaumListe[RaumCount].RaumName.equals(pRaumName))
            {
                Result = RaumCount;
            }
        }
        return Result;
    }

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

    public void setMomentanerNutzerIndex(int pMomentanerNutzerIndex)
    {
        MomentanerNutzerIndex = pMomentanerNutzerIndex;
    }

    public Bus getEventBus()
    {
        return EventBus;
    }
}