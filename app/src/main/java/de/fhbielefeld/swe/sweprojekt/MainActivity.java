package de.fhbielefeld.swe.sweprojekt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //GUI Stuff
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

        mViewPager.setAdapter(mSectionsStatePagerAdapter);


    }

    public void setViewPager(int FragmentIndex)
    {
        mViewPager.setCurrentItem(FragmentIndex);
    }
}