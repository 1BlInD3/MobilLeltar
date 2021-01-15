package com.example.mobilleltar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mobilleltar.Fragments.MainFragment;
import com.example.mobilleltar.Fragments.TabbedFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange {

    private TabbedFragment tabbedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabbedFragment = new TabbedFragment();
        //mainFragment = new MainFragment();
       /* FragmentManager mangaer = getSupportFragmentManager();
        Fragment fragment = mangaer.findFragmentById(R.id.frag_container);

        if(fragment == null)
        {
            fragment = new TabbedFragment();
            mangaer.beginTransaction().add(R.id.frag_container,fragment).commit();
        }*/
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,tabbedFragment).commit();
    }

    @Override
    public void tabChangeListener(int index) {
        tabbedFragment.updateTabView(index);
    }
}