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
import com.squareup.otto.Subscribe;

public class FragmentBuchung extends Fragment {
    private Button ButtonBack;
    TimePicker Picker;
    Button Button;
    Button ButtonAbbruch;
    Button ButtonStornier;
    EditText RaumEingabe;
    Bus EventBus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentbuchung_layout, container, false);

        EventBus = ((MainActivity)getActivity()).getEventBus();
        Button = view.findViewById(R.id.button);
        ButtonBack = view.findViewById(R.id.buttonNavFrgLogIn);
        ButtonStornier = view.findViewById(R.id.buttonstornierung);
        ButtonAbbruch = view.findViewById(R.id.buttonabbruch);
        Picker = view.findViewById(R.id.time_picker);
        Picker.setIs24HourView(true);
        RaumEingabe = view.findViewById(R.id.editText);

        EventBus.register(this);

        Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String RaumName = RaumEingabe.getText().toString();
                System.out.println("NAME: "+RaumName);
                EventBus.post(new BuchungEvent(RaumName, Picker.getHour(), Picker.getMinute(), false, false, false));
            }
        });


        ButtonBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               Toast.makeText(getActivity(), "Gehe zum Login-Fragment", Toast.LENGTH_SHORT).show();

               EventBus.post(new BuchungEvent("", 0, 0, false, true, false));
           }
        });

        ButtonAbbruch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EventBus.post(new BuchungEvent("", 0, 0, true, false, false));
            }
        });

        ButtonStornier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EventBus.post(new BuchungEvent("", 0, 0, false, false, true));
            }
        });

        return view;
    }

    @Subscribe public void MainActivityCallback (MainActivityCallbackBuchung pEvent)
    {
        if(pEvent.StartzeitErfolg && !pEvent.EndzeitErfolg)
        {
            Button.setText("Endzeit festlegen");
        }
        else if(pEvent.StartzeitErfolg && pEvent.EndzeitErfolg)
        {
            Button.setText("Startzeit festlegen");

            Toast.makeText(getActivity(), "Buchung erfolgreich", Toast.LENGTH_SHORT).show();
        }
    }
}
