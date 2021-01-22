package com.example.mobilleltar;

import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.mobilleltar.Fragments.CikkResultFragment;
import com.example.mobilleltar.Fragments.CikklekerdezesFragment;
import com.example.mobilleltar.Fragments.LoginFragment;
import com.example.mobilleltar.Fragments.MainFragment;
import com.example.mobilleltar.Fragments.MenuFragment;
import com.example.mobilleltar.Fragments.PolcResultFragment;
import com.example.mobilleltar.Fragments.TabbedFragment;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.InvalidScannerNameException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange, BarcodeReader.BarcodeListener {

    private TabbedFragment tabbedFragment;
    private MenuFragment menuFragment;

    private BarcodeReader barcodeReader;
    private AidcManager manager;

    private String barcodeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment loginFragment = new LoginFragment();
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,loginFragment,"LoginFrag").commit();

        AidcManager.create(this, new AidcManager.CreatedCallback() {
            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                try {
                    barcodeReader = manager.createBarcodeReader();
                    barcodeReader.claim();
                } catch (ScannerUnavailableException | InvalidScannerNameException e) {
                    e.printStackTrace();
                }
                try
                {
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_39_ENABLED,true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                            BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                } catch (UnsupportedPropertyException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed to apply properties", Toast.LENGTH_SHORT).show();
                }
                barcodeReader.addBarcodeListener(MainActivity.this);
            }
        });

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
        PolcResultFragment polcResultFragment = PolcResultFragment.newInstance(barcodeData);               //new PolcResultFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,polcResultFragment,"LoadPolcFrag").commit();
    }

    public void LoadCikklekerdezesFragment()
    {
        CikklekerdezesFragment cikklekerdezesFragment = new CikklekerdezesFragment();//CikklekerdezesFragment.newInstance(barcodeData);//
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cikklekerdezesFragment,"CikkFrag").addToBackStack(null).commit();
    }

    public void LoadCikkResult()
    {
        CikkResultFragment cikkResultFragment = CikkResultFragment.newInstance(barcodeData);
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,cikkResultFragment).commit();
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
      return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBarcodeEvent(
            BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barcodeData = barcodeReadEvent.getBarcodeData();
                //String timestamp = barcodeReadEvent.getTimestamp();
                FragmentManager manager = getSupportFragmentManager();
                LoginFragment loginFragment = (LoginFragment)manager.findFragmentByTag("LoginFrag");
                TabbedFragment tabbedFragment = (TabbedFragment)manager.findFragmentByTag("TabbedFrag");
                CikklekerdezesFragment cikklekerdezesFragment = (CikklekerdezesFragment)manager.findFragmentByTag("CikkFrag");
                if(loginFragment != null)
                {
                    loginFragment.SetId(barcodeData);
                    loginFragment.onDestroy();
                }
                else if(tabbedFragment != null)
                {
                    //Toast.makeText(getApplicationContext(),"Tabbed",Toast.LENGTH_SHORT).show();
                    tabbedFragment.GetFragmentAtPosition(barcodeData);
                    tabbedFragment.onDestroy();
                }
                else if (cikklekerdezesFragment != null)
                {
                    cikklekerdezesFragment.SetBinOrItem(barcodeData);
                    cikklekerdezesFragment.onDestroy();
                }
               // Toast.makeText(getApplicationContext(),barcodeData,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Barcode read failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            barcodeReader.release();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (barcodeReader != null) {
            barcodeReader.removeBarcodeListener(this);
            barcodeReader.close();
        }
        if (manager != null) {
            manager.close();
        }
    }
}