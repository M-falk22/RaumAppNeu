package de.fhbielefeld.swe.sweprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.util.Arrays;
import java.util.LinkedList;

public class FragmentStornierung extends Fragment {
    ListView Liste;
    Bus EventBus;
    LinkedList<String> myStringArray = new LinkedList<String>();
    Button ButtonBack;
    boolean isRegistered = false;
    ArrayAdapter<String> adapter;
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

        Liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> ViewAdapter, View view, int position, long id) { //maybe check if adapter exists first?
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                EventBus.post(new StornierEvent(position));
                //adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Subscribe public void MainActivityCallback (MainActivityCallbackStornierung pEvent)
    {
        if(pEvent.Update)
        {
            //Minus symbol anstells von leeren Symbol nehmen!
            String[] TempArray = new String[10];
            TempArray = ((MainActivity)getActivity()).getNutzerStringListe();
            if(myStringArray != null)
            {
                myStringArray.clear();
            }
            LinkedList<String> Temp = new LinkedList<String>(Arrays.asList(TempArray));

            while(!Temp.isEmpty())
            {
                myStringArray.add(Temp.getFirst());
                Temp.removeFirst();
            }

            if(adapter == null) {
                adapter = new ArrayAdapter<String>(((MainActivity) getActivity()),
                        android.R.layout.simple_list_item_1, myStringArray); //Create string array in main activity when button is pressed and just grab it here in the ugly way or whatever i dont care...
                Liste.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        }
    }
}
