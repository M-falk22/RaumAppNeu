package de.fhbielefeld.swe.sweprojekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.otto.Bus;

public class FragmentStornierung extends Fragment {
    ListView Liste;
    Bus EventBus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentstornierung_layout, container, false);

        String[] myStringArray = ((MainActivity)getActivity()).getNutzerStringListe();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((MainActivity)getActivity()),
                android.R.layout.simple_list_item_1, myStringArray); //Create string array in main activity when button is pressed and just grab it here in the ugly way or whatever i dont care...

        Liste = view.findViewById(R.id.BuchungListe);

        EventBus = ((MainActivity)getActivity()).getEventBus();

        /*ButtonLogIn = view.findViewById(R.id.buttonNavFrgBuchung);
        NutzerNameEingabe = view.findViewById(R.id.EditTextNutzerID);

        ButtonLogIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {

               String NutzerName = NutzerNameEingabe.getText().toString();

               if(!NutzerName.equals(""))
               {
                   EventBus.post(new LogInEvent(NutzerName));
                   ((MainActivity) getActivity()).setViewPager(1);

                   Toast.makeText(getActivity(), "Gehe zum Buchungs-Fragment", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast.makeText(getActivity(), "Nutzername muss angegeben werden!", Toast.LENGTH_SHORT).show();
               }
           }
        });*/

        return view;
    }
}
