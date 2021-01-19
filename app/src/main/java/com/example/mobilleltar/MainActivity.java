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
    private String URL = "jdbc:jtds:sqlserver://10.0.0.18;databaseName=EllenallasMeres;user=MvWrite;password=Mv2019;loginTimeout=2";
    private Connection connection;
    //private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);
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

      /*  try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(connection != null)
        {
            Toast.makeText(this,"Van hálózat",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Nem tud csatlakozni",Toast.LENGTH_SHORT).show();
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

    public void LoadPolcResults()
    {
        PolcResultFragment polcResultFragment = new PolcResultFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,polcResultFragment).commit();
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