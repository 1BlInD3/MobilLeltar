package com.example.mobilleltar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.mobilleltar.DataItems.CikkItems;
import com.example.mobilleltar.DataItems.PolcItems;
import com.example.mobilleltar.Fragments.CikkResultFragment;
import com.example.mobilleltar.Fragments.CikklekerdezesFragment;
import com.example.mobilleltar.Fragments.EmptyFragment;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange, BarcodeReader.BarcodeListener {

    private TabbedFragment tabbedFragment;
    private MenuFragment menuFragment;

    private BarcodeReader barcodeReader;
    private AidcManager manager;

    private String barcodeData;
    private String decodedData;

    private String URL = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=10";
    private Connection connection;

    private String sql="";
    private ArrayList<PolcItems> pi = new ArrayList<>();
    private ArrayList<CikkItems> ci = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginFragment loginFragment = new LoginFragment();
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,loginFragment,"LoginFrag").commit();

        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        filter.addAction(getResources().getString(R.string.dw_action));
        registerReceiver(myBroadcastReceiver, filter);

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

    /*public void LoadPolcResults()
    {
        PolcResultFragment polcResultFragment = /*PolcResultFragment.newInstance(decodedData);      new PolcResultFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,polcResultFragment,"LoadPolcFrag").commit();
    }*/

    public void LoadCikklekerdezesFragment()
    {
        CikklekerdezesFragment cikklekerdezesFragment = new CikklekerdezesFragment();//CikklekerdezesFragment.newInstance(barcodeData);//
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cikklekerdezesFragment,"CikkFrag").addToBackStack(null).commit();
    }

   /* public void LoadCikkResult()
    {
        CikkResultFragment cikkResultFragment = CikkResultFragment.newInstance(decodedData);
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,cikkResultFragment).commit();
    }*/
    public void LoadEmptyFragment()
    {
        EmptyFragment emptyFragment = new EmptyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
    }

    @Override
    public void tabChangeListener(int index) {
        tabbedFragment.updateTabView(index);
    }

    @Override
    public void loadForChange(String cikkszam, String megnevezes1, String megnevezes2, String mennyiseg) {
        tabbedFragment.setDataForChange(cikkszam,megnevezes1,megnevezes2,mennyiseg);
    }

  public boolean FragmentName()
  {
      FragmentManager manager = getSupportFragmentManager();
      MenuFragment  menuFragment = (MenuFragment)manager.findFragmentByTag("MenuFrag");
      //TabbedFragment tabbedFragment = (TabbedFragment)manager.findFragmentByTag("TabbedFrag");
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
                    tabbedFragment.GetFragmentAtPosition(barcodeData);
                    tabbedFragment.onDestroy();
                }
                else if (cikklekerdezesFragment != null)
                {
                    LoadEmptyFragment();
                    pi.clear();
                    ci.clear();
                    cikklekerdezesFragment.SetBinOrItem(barcodeData);
                    SQL();
                    cikklekerdezesFragment.onDestroy();
                }
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {

        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Nem sikerült leolvasni",
                Toast.LENGTH_SHORT).show());

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

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           // Bundle b = intent.getExtras();

            if (action.equals(getResources().getString(R.string.dw_action))) {

                try {
                    displayScanResult(intent, "via Broadcast");
                } catch (Exception e) {

                }
            }
        }
    };

    private void displayScanResult(Intent initiatingIntent, String howDataReceived)
    {
       // String decodedSource = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_source));
        decodedData = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data));
        //String decodedLabelType = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_label_type));


        FragmentManager manager = getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment)manager.findFragmentByTag("LoginFrag");
        TabbedFragment tabbedFragment = (TabbedFragment)manager.findFragmentByTag("TabbedFrag");
        CikklekerdezesFragment cikklekerdezesFragment = (CikklekerdezesFragment)manager.findFragmentByTag("CikkFrag");
        if(loginFragment != null)
        {
            loginFragment.SetId(decodedData);
            loginFragment.onDestroy();
        }
        else if(tabbedFragment != null)
        {
            tabbedFragment.GetFragmentAtPosition(decodedData);
            tabbedFragment.onDestroy();
        }
        else if (cikklekerdezesFragment != null)
        {
            barcodeData = decodedData;
            LoadEmptyFragment();
            pi.clear();
            ci.clear();
            cikklekerdezesFragment.SetBinOrItem(decodedData);
            SQL();
            cikklekerdezesFragment.onDestroy();
        }
    }

    class SqlRunnable implements Runnable
    {
        @Override
        public void run() {
            try {
                Connect(barcodeData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private void Connect(String code) throws SQLException {
        PolcResultFragment polcResultFragment = new PolcResultFragment();
        CikkResultFragment cikkResultFragment = new CikkResultFragment();
        Bundle bundle = new Bundle();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if(connection != null)
        {
            try {
                Statement statement = connection.createStatement();
                statement.setQueryTimeout(10);
                sql = String.format(getResources().getString(R.string.polcSql),code);
                ResultSet resultSet = statement.executeQuery(sql);
                if(!resultSet.next())
                {
                    Log.d("HONEY", "DISConnect: ");
                    Statement statement2 = connection.createStatement();
                    statement2.setQueryTimeout(10);
                    sql = String.format(getResources().getString(R.string.cikkSql),code);
                    ResultSet resultSet1 = statement2.executeQuery(sql);
                    if(!resultSet1.next())
                    {
                        Log.d("HONEY", "DUPLA NULLA: ");
                        EmptyFragment emptyFragment = EmptyFragment.newInstance("Nincs találat","");
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
                    }else
                    {
                        String megjegyzes1,megjegyzes2,unit;
                        megjegyzes1 = resultSet1.getString("Description1");
                        megjegyzes2 = resultSet1.getString("Description2");
                        unit = resultSet1.getString("Unit");
                        Log.d("HONEY", "Connect1: ");
                        do {

                            ci.add(new CikkItems(resultSet1.getDouble("BalanceQty"),resultSet1.getString("BinNumber"), resultSet1.getString("Warehouse"), resultSet1.getString("QcCategory")));

                        }
                        while (resultSet1.next());
                        bundle.putSerializable("cikk",ci);
                        bundle.putString("megjegyzes",megjegyzes1);
                        bundle.putString("megjegyzes2",megjegyzes2);
                        bundle.putString("unit",unit);
                        cikkResultFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,cikkResultFragment).commit();
                    }
                }
                else
                {
                    Log.d("HONEY", "Connect: ");
                    do {

                        pi.add(new PolcItems(resultSet.getDouble("BalanceQty"), resultSet.getString("Unit"), resultSet.getString("Description1"), resultSet.getString("Description2"), resultSet.getString("IntRem"), resultSet.getString("QcCategory")));

                    }
                    while (resultSet.next());
                    bundle.putSerializable("polc",pi);
                    polcResultFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,polcResultFragment,"PolcResultFrag").commit();
                }
            }
            catch (Exception e)
            {
                EmptyFragment emptyFragment = EmptyFragment.newInstance("A feldolgozás során hiba lépett fel","");
                getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
            }
        }
        else
        {
            Log.d("HONEY", "XConnect: ");
            EmptyFragment emptyFragment = EmptyFragment.newInstance("Nincs hálózat","");
            getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
        }
    }
    public void SQL()
    {
        SqlRunnable sqlRunnable = new SqlRunnable();
        new Thread(sqlRunnable).start();
    }
}