package com.example.mobilleltar;

import android.os.StrictMode;
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
import com.example.mobilleltar.Fragments.PolcResultFragment;
import com.example.mobilleltar.Fragments.TabbedFragment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange {

    private TabbedFragment tabbedFragment;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment loginFragment = new LoginFragment();
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,loginFragment,"LoginFrag").commit();
    }

    public void LoadTabbedFragment()
    {
        tabbedFragment = new TabbedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,tabbedFragment,"TabbedFrag").addToBackStack(null).commit();
    }

    public void LoadMenuFragment()
    {
        menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
    }

    public void LoadPolcResults()
    {
        PolcResultFragment polcResultFragment = new PolcResultFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,polcResultFragment,"LoadPolcFrag").commit();
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
  public boolean FragmentName()
  {
      FragmentManager manager = getSupportFragmentManager();
      MenuFragment  menuFragment = (MenuFragment)manager.findFragmentByTag("MenuFrag");
      TabbedFragment tabbedFragment = (TabbedFragment)manager.findFragmentByTag("TabbedFrag");
      if(menuFragment != null)
      {
          return  true;
      }
      else
      {
          return false;
      }
  }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

      if(FragmentName())
      {
          if(keyCode == 8)
          {
              //Toast.makeText(getApplicationContext(),"Ez az ",Toast.LENGTH_SHORT).show();
              LoadTabbedFragment();
          }
          else if (keyCode == 9)
          {
              Toast.makeText(getApplicationContext(),"Nem  az ",Toast.LENGTH_SHORT).show();
          }
          else if (keyCode == 10)
          {
              LoadCikklekerdezesFragment();
          }
          return super.onKeyDown(keyCode, event);
      }

     /*  if(FragmentName())
       {
         //  if(keyCode == KeyEvent.KEYCODE_BUTTON_8)
         //  {
               Toast.makeText(getApplicationContext(),"Ez az ",Toast.LENGTH_SHORT).show();
          // }
       }
       else
       {
           Toast.makeText(getApplicationContext(),"Nem  az ",Toast.LENGTH_SHORT).show();
       }*/
        return super.onKeyDown(keyCode, event);
    }
}