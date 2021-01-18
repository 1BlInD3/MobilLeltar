package com.example.mobilleltar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.mobilleltar.Fragments.CikklekerdezesFragment;
import com.example.mobilleltar.Fragments.LoginFragment;
import com.example.mobilleltar.Fragments.MainFragment;
import com.example.mobilleltar.Fragments.MenuFragment;
import com.example.mobilleltar.Fragments.TabbedFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange {

    private TabbedFragment tabbedFragment;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment loginFragment = new LoginFragment();
        getSupportActionBar().hide();
        //mainFragment = new MainFragment();
       /* FragmentManager mangaer = getSupportFragmentManager();
        Fragment fragment = mangaer.findFragmentById(R.id.frag_container);

        if(fragment == null)
        {
            fragment = new TabbedFragment();
            mangaer.beginTransaction().add(R.id.frag_container,fragment).commit();
        }*/
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,loginFragment).commit();
    }

    public void LoadTabbedFragment()
    {
        tabbedFragment = new TabbedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,tabbedFragment,"TabbedFrag").addToBackStack(null).commit();
    }

    public void LoadMenuFragment()
    {
        MenuFragment menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment).commit();
    }

    public void LoadMenuFragment2()
    {
        MenuFragment menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,menuFragment).commit();
    }

    public void LoadCikklekerdezesFragment()
    {
        CikklekerdezesFragment cikklekerdezesFragment = new CikklekerdezesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cikklekerdezesFragment,"CikkFrag").addToBackStack(null).commit();
    }

    @Override
    public void tabChangeListener(int index) {
        tabbedFragment.updateTabView(index);
    }

    @Override
    public void loadForChange(String cikkszam, String megnevezes1, String megnevezes2, String mennyiseg) {
        tabbedFragment.setDataForChange(cikkszam,megnevezes1,megnevezes2,mennyiseg);
    }

  /* @Override
    public void onBackPressed() {

        backPressedTime = System.currentTimeMillis();

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("TabbedFrag");
        if(fragment!=null)
        {
            MenuFragment menuFragment = new MenuFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment).commit();
        }
    }*/

}