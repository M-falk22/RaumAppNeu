package de.fhbielefeld.swe.sweprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import android.widget.TimePicker;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;





public class MainActivity extends AppCompatActivity {

    //GUI Stuff
    SectionsStatePagerAdapter mSectionsStatePagerAdapter;
    ViewPager mViewPager;

    TimePicker Picker;
    Button Button;
    Raum[] RaumListe = new Raum[10];
    EditText RaumEingabe;
    int AnzahlRaum = 0;
    int RaumIndex = 0;
    int BuchungIndex = 0;

    enum AppZustand
    {
        STARTZEITEINGABE,
        ENDZEITEINGABE
    }

    AppZustand Zustand = AppZustand.STARTZEITEINGABE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);

        Button = findViewById(R.id.button);
        Picker = findViewById(R.id.time_picker);
        Picker.setIs24HourView(true);
        RaumEingabe = findViewById(R.id.editText);


        RaumListe[AnzahlRaum] = new Raum(AnzahlRaum++);

        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button

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
        });
    }

}