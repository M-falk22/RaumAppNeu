package de.fhbielefeld.swe.sweprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    private Bus EventBus = new Bus();
    //NOTE(Moritz): Alle Arrays sind zu demonstrativen Zwecken auf 10 Elemente beschränkt. Eine verlinkte Liste oder ähnliches zu nutzen, wäre bei einer vollwertigen App besser.
    Raum[] RaumListe = new Raum[10];
    Nutzer[] NutzerListe = new Nutzer[10];
    Nutzer MomentanerNutzer;
    int AnzahlNutzer = 0;
    int NutzerIndex;
    int AnzahlRaum = 0;
    String RaumName;
    int RaumIndex;
    int RaumBuchungIndex = 0;
    int MomentanerNutzerIndex = 0;
    String[] NutzerStringListe = new String[] {"","","","","","","","","","" };
    String[] RaumNamen = new String[3];

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

        RaumNamen[0] = "C013";
        RaumNamen[1] = "D010";
        RaumNamen[2] = "E406";


        //NOTE(Moritz): Räume sind Hardcoded. In der Praxis wäre es sicherlich schöner eine Input-Textdatei zu parsen und dementsprechend das Array zu erstellen...
        RaumListe[AnzahlRaum++] = new Raum(RaumNamen[0]);
        RaumListe[AnzahlRaum++] = new Raum(RaumNamen[1]);
        RaumListe[AnzahlRaum++] = new Raum(RaumNamen[2]);

        EventBus.register(this);

    }

    @Subscribe public void FragmentBuchungEvent(BuchungEvent pEvent)
    {
        //TODO(Moritz): Abbruch-Möglichkeit einführen!
        if(!pEvent.StornierSicht)
        {
            if(Zustand.equals(BuchungZustand.STARTZEITEINGABE))
            {
                if(!pEvent.NutzerWechsel)
                {
                    RaumName = pEvent.RaumName;

                    RaumIndex = RaumArrayIndex(RaumName);

                    if(RaumIndex >= 0)
                    {
                        Raum MomentanerRaum = RaumListe[RaumIndex];
                        if(MomentanerRaum.getAnzahlBuchungen() < 10)
                        {
                            //Check all buchungen for this room

                            /*int RaumAnzahlBuchungen = MomentanerRaum.AnzahlBuchungen;
                            for(int BuchungenCount = 0; BuchungenCount < RaumAnzahlBuchungen; BuchungenCount++)
                            {
                                MomentanerRaum.Buchungen[BuchungenCount].StartzeitStunde
                                MomentanerRaum.Buchungen[BuchungenCount].EndzeitStunde;
                            }*/
                            MomentanerRaum.BuchenStartzeit(pEvent.ZeitStunde, pEvent.ZeitMinute);

                            RaumBuchungIndex = RaumListe[RaumIndex].getAnzahlBuchungen() - 1;

                            System.out.println("STARTZEITEINGABE erfolgreich!");

                            EventBus.post(new MainActivityCallbackBuchung(true, false));

                            Zustand = BuchungZustand.ENDZEITEINGABE;
                        }
                        else
                        {
                            System.out.println("Raum ist ausgebucht!");
                        }
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
            }
            else if(Zustand.equals(BuchungZustand.ENDZEITEINGABE))
            {
                Zustand = BuchungZustand.STARTZEITEINGABE;

                RaumListe[RaumIndex].BuchenEndzeit(pEvent.ZeitStunde, pEvent.ZeitMinute, RaumBuchungIndex);

                MomentanerNutzer.Buchungen[MomentanerNutzer.AnzahlBuchung] = RaumListe[RaumIndex].getBuchung(RaumBuchungIndex);
                MomentanerNutzer.AnzahlBuchung = MomentanerNutzer.AnzahlBuchung+1;

                EventBus.post(new MainActivityCallbackBuchung(true, true));

                System.out.println("ENDZEITEINGABE erfolgreich!");
            }
        }
        else
        {
            //Hier String Array erstellen und stornierfragment laden!
            for(int NutzerBuchungenCount = 0;  NutzerBuchungenCount <MomentanerNutzer.AnzahlBuchung; NutzerBuchungenCount++)
            {
                Buchung MomentaneBuchung = MomentanerNutzer.Buchungen[NutzerBuchungenCount];
                String Raum = MomentaneBuchung.RaumName;
                String StartzeitStunde = Integer.toString(MomentaneBuchung.StartzeitStunde);
                String StartzeitMinute = Integer.toString(MomentaneBuchung.StartzeitMinute);
                String EndzeitStunde = Integer.toString(MomentaneBuchung.EndzeitStunde);
                String EndzeitMinute = Integer.toString(MomentaneBuchung.EndzeitMinute);

                NutzerStringListe[NutzerBuchungenCount] = Raum+" Von "+StartzeitStunde+":"+StartzeitMinute+" bis "+EndzeitStunde+":"+EndzeitMinute;
            }
            EventBus.post(new MainActivityCallbackStornierung(true));
            setViewPager(2);
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
        MomentanerNutzer = NutzerListe[NutzerIndex];
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

    public String[] getNutzerStringListe() {
        return NutzerStringListe;
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
        return RaumBuchungIndex;
    }

    public void setBuchungIndex(int buchungIndex) {
        RaumBuchungIndex = buchungIndex;
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