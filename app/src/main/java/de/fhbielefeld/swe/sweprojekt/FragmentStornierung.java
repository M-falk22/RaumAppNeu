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
import com.squareup.otto.Subscribe;

public class FragmentStornierung extends Fragment {
    ListView Liste;
    Bus EventBus;
    String[] myStringArray;
    Button ButtonBack;
    boolean isRegistered = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentstornierung_layout, container, false);



        Liste = view.findViewById(R.id.BuchungListe);



        EventBus = ((MainActivity)getActivity()).getEventBus();

        if(!isRegistered) {
            EventBus.register(this);
            isRegistered = true;
        }

        ButtonBack = view.findViewById(R.id.imageButton);

        ButtonBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
                   ((MainActivity)getActivity()).setViewPager(1);
                   //EventBus.unregister(this);
           }
        });

        return view;
    }

    @Subscribe public void MainActivityCallback (MainActivityCallbackStornierung pEvent)
    {
        if(pEvent.Update)
        {
            myStringArray = ((MainActivity)getActivity()).getNutzerStringListe();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(((MainActivity)getActivity()),
                    android.R.layout.simple_list_item_1, myStringArray); //Create string array in main activity when button is pressed and just grab it here in the ugly way or whatever i dont care...
            Liste.setAdapter(adapter);
        }
    }
}
