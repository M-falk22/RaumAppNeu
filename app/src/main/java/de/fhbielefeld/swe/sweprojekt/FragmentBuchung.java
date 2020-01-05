package de.fhbielefeld.swe.sweprojekt;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentBuchung extends Fragment {
    private Button ButtonBack;
    TimePicker Picker;
    Button Button;
    EditText RaumEingabe;

    /*Raum[] RaumListe = new Raum[10];
    Nutzer[] NutzerListe = new Nutzer[10];
    int AnzahlRaum = 0;
    int RaumIndex = 0;
    int BuchungIndex = 0;

    enum AppZustand
    {
        STARTZEITEINGABE,
        ENDZEITEINGABE
    }

    AppZustand Zustand = AppZustand.STARTZEITEINGABE;*/

    Activity Main = ((MainActivity)getActivity());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentbuchung_layout, container, false);

        OnItemSelectedLi

        ButtonBack = view.findViewById(R.id.buttonNavFrgLogIn);

        Button = view.findViewById(R.id.button);
        Picker = view.findViewById(R.id.time_picker);
        Picker.setIs24HourView(true);
        RaumEingabe = view.findViewById(R.id.editText);

        Main.RaumListe[AnzahlRaum] = new Raum(AnzahlRaum++);

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


        ButtonBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               Toast.makeText(getActivity(), "Gehe zum Login-Fragment", Toast.LENGTH_SHORT).show();

               ((MainActivity)getActivity()).setViewPager(0);
           }
        });

        return view;
    }
}
