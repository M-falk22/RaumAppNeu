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

import com.squareup.otto.Bus;

public class FragmentBuchung extends Fragment {
    private Button ButtonBack;
    TimePicker Picker;
    Button Button;
    EditText RaumEingabe;
    Bus EventBus;

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

        ButtonBack = view.findViewById(R.id.buttonNavFrgLogIn);


        EventBus = ((MainActivity)getActivity()).getEventBus();
        Button = view.findViewById(R.id.button);
        Picker = view.findViewById(R.id.time_picker);
        Picker.setIs24HourView(true);
        RaumEingabe = view.findViewById(R.id.editText);

        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                //EventBus.post("BUCHEN");
                String RaumName = RaumEingabe.getText().toString();
                System.out.println("NAME: "+RaumName);
                EventBus.post(new BuchungEvent(RaumName, Picker.getHour(), Picker.getMinute()));
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
