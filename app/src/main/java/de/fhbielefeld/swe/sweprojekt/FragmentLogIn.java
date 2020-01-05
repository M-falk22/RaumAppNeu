package de.fhbielefeld.swe.sweprojekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentLogIn extends Fragment {
    private Button ButtonLogIn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentlogin_layout, container, false);

        ButtonLogIn = view.findViewById(R.id.buttonNavFrgBuchung);

        ButtonLogIn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view)
           {
               Toast.makeText(getActivity(), "Gehe zum Buchungs-Fragment", Toast.LENGTH_SHORT).show();

               ((MainActivity)getActivity()).setViewPager(1);
           }
        });

        return view;
    }
}
