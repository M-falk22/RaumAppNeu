package de.fhbielefeld.swe.sweprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.LinkedList;

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
                            boolean zeitFrei = true;
                            int RaumAnzahlBuchungen = MomentanerRaum.AnzahlBuchungen;
                            for(int BuchungenCount = 0; BuchungenCount < RaumAnzahlBuchungen; BuchungenCount++)
                            {
                                int StartVergleichStunde = MomentanerRaum.Buchungen[BuchungenCount].StartzeitStunde;
                                int StartVergleichMinute = MomentanerRaum.Buchungen[BuchungenCount].StartzeitMinute;
                                int EndVergleichStunde = MomentanerRaum.Buchungen[BuchungenCount].EndzeitStunde;
                                int EndVergleichMinute = MomentanerRaum.Buchungen[BuchungenCount].EndzeitMinute;

                                if(pEvent.ZeitStunde >= StartVergleichStunde && pEvent.ZeitStunde <= EndVergleichStunde)
                                {
                                    if(pEvent.ZeitMinute >= StartVergleichMinute && pEvent.ZeitMinute <= EndVergleichMinute)
                                    {
                                        String StartStunde = Integer.toString(StartVergleichStunde);
                                        String StartMinute = Integer.toString(StartVergleichMinute);
                                        String EndStunde = Integer.toString(EndVergleichStunde);
                                        String EndMinute = Integer.toString(EndVergleichMinute);
                                        EventBus.post(new MainActivityCallbackBuchung(false, false, "Der Raum ist von "+StartStunde+":"+StartMinute+" bis "+EndStunde+":"+EndMinute+" besetzt!"));
                                        zeitFrei = false;
                                    }
                                }
                            }
                            if(zeitFrei)
                            {
                                MomentanerRaum.BuchenStartzeit(pEvent.ZeitStunde, pEvent.ZeitMinute);

                                RaumBuchungIndex = RaumListe[RaumIndex].getAnzahlBuchungen() - 1;

                                System.out.println("STARTZEITEINGABE erfolgreich!");

                                EventBus.post(new MainActivityCallbackBuchung(true, false, "CLEAR"));

                                Zustand = BuchungZustand.ENDZEITEINGABE;
                            }
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

                EventBus.post(new MainActivityCallbackBuchung(true, true, "CLEAR"));

                System.out.println("ENDZEITEINGABE erfolgreich!");
            }
        }
        else
        {
            //Hier String Array erstellen und stornierfragment laden!
            if(MomentanerNutzer.AnzahlBuchung != 0) {
                for (int NutzerBuchungenCount = 0; NutzerBuchungenCount < 10; NutzerBuchungenCount++) {
                    Buchung MomentaneBuchung = MomentanerNutzer.Buchungen[NutzerBuchungenCount];
                    if(MomentanerNutzer.Buchungen[NutzerBuchungenCount] != null) {
                        String Raum = MomentaneBuchung.RaumName;
                        String StartzeitStunde = Integer.toString(MomentaneBuchung.StartzeitStunde);
                        String StartzeitMinute = Integer.toString(MomentaneBuchung.StartzeitMinute);
                        String EndzeitStunde = Integer.toString(MomentaneBuchung.EndzeitStunde);
                        String EndzeitMinute = Integer.toString(MomentaneBuchung.EndzeitMinute);

                        NutzerStringListe[NutzerBuchungenCount] = Raum + " Von " + StartzeitStunde + ":" + StartzeitMinute + " bis " + EndzeitStunde + ":" + EndzeitMinute;
                    }
                    else
                    {
                        NutzerStringListe[NutzerBuchungenCount] = "";
                    }
                }
            }
            EventBus.post(new MainActivityCallbackStornierung(true)); //vllt auch noch in if ding rein hauen
            setViewPager(2);
        }

    }

    @Subscribe public void FragmentStornierEvent(StornierEvent pEvent)
    {
        String[] TempArray = new String[10];
        Buchung[] TempBuchungen = new Buchung[10];

        if(pEvent.EntfernIndex != 0 && pEvent.EntfernIndex != 9)
        {
            System.arraycopy(NutzerStringListe, 0, TempArray, 0, pEvent.EntfernIndex - 1);
            System.arraycopy(NutzerStringListe, pEvent.EntfernIndex+1, TempArray, pEvent.EntfernIndex, NutzerStringListe.length-1-pEvent.EntfernIndex);

            System.arraycopy(MomentanerNutzer.Buchungen, 0, TempBuchungen, 0, pEvent.EntfernIndex - 1);
            System.arraycopy(MomentanerNutzer.Buchungen, pEvent.EntfernIndex+1, TempBuchungen, pEvent.EntfernIndex, MomentanerNutzer.Buchungen.length-1-pEvent.EntfernIndex);
        }
        else if(pEvent.EntfernIndex == 0)
        {
            System.arraycopy(NutzerStringListe, 1, TempArray, 0, NutzerStringListe.length-2);

            System.arraycopy(MomentanerNutzer.Buchungen, 1, TempBuchungen, 0, MomentanerNutzer.Buchungen.length-2);
        }
        else if(pEvent.EntfernIndex == 9)
        {
            System.arraycopy(NutzerStringListe, 0, TempArray, 0, NutzerStringListe.length-2);

            System.arraycopy(MomentanerNutzer.Buchungen, 0, TempArray, 0, MomentanerNutzer.Buchungen.length-2);
        }

        System.arraycopy(TempArray, 0, NutzerStringListe, 0, TempArray.length);
        System.arraycopy(TempBuchungen, 0, MomentanerNutzer.Buchungen, 0, TempBuchungen.length);

        MomentanerNutzer.AnzahlBuchung = MomentanerNutzer.AnzahlBuchung-1;
        //EventBus.post(new MainActivityCallbackStornierung(true));
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