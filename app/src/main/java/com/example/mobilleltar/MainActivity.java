package com.example.mobilleltar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager mangaer = getSupportFragmentManager();
        Fragment fragment = mangaer.findFragmentById(R.id.frag_container);

        if(fragment == null)
        {
            fragment = new MainFragment();
            mangaer.beginTransaction().add(R.id.frag_container,fragment).commit();
        }
    }
}