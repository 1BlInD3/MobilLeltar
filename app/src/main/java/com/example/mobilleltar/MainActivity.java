package com.example.mobilleltar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.mobilleltar.DataItems.CikkItems;
import com.example.mobilleltar.DataItems.PolcItems;
import com.example.mobilleltar.Fragments.BlankFragment;
import com.example.mobilleltar.Fragments.CikkResultFragment;
import com.example.mobilleltar.Fragments.CikklekerdezesFragment;
import com.example.mobilleltar.Fragments.EmptyFragment;
import com.example.mobilleltar.Fragments.LeltarozasFragment;
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

//jogosultsagot elmenteni és a nyomógombokat letiltani ahhoz képest

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange, BarcodeReader.BarcodeListener {

    private static final String TAG = "QUERY";
    private TabbedFragment tabbedFragment;
    
    private MenuFragment menuFragment;
    private LoginFragment loginFragment;

    private BarcodeReader barcodeReader;
    private AidcManager manager;

    private String barcodeData;
    private String decodedData;

    private String URL = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=10";
    private String connectionString = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=leltar;user=Raktarrendszer;password=PaNNoN0132;loginTimeout=10";
    private Connection connection;

    private String sql="";
    private ArrayList<PolcItems> pi = new ArrayList<>();

    private ArrayList<CikkItems> ci = new ArrayList<>();
    private Handler handler = new Handler();
    private Handler handler1 = new Handler();
    private Handler handler2 = new Handler();
    private Handler handler3 = new Handler();
    private Handler handler4 = new Handler();
    private Handler handler5 = new Handler();

    public String DolgKod;
    private boolean hasRight;

    public boolean isPolc = false;
    private String mdesc1,mdesc2,munit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFragment = new LoginFragment();
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
    public void LoadBlank()
    {
        BlankFragment blankFragment = new BlankFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,blankFragment).commit();
    }

   /* public void LoadMenuFragment()
    {
        menuFragment = new MenuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
    }*/

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
      menuFragment = (MenuFragment)manager.findFragmentByTag("MenuFrag");
      if(menuFragment != null && menuFragment.isVisible())
      {
          return  true;
      }
      else
      {
          return false;
      }
  }

    @Override
    public void onBackPressed() {
        //
        if(tabbedFragment != null && tabbedFragment.isVisible())
        {
            isPolc = false;
        }
        super.onBackPressed();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      if(FragmentName())
      {
              if (hasRight && keyCode == 8)
              {
                  LoadTabbedFragment();
              }
              else if (keyCode == 9)
              {
                  Toast.makeText(getApplicationContext(), "Nincs jogosultságod belépni ", Toast.LENGTH_SHORT).show();
              } else if (keyCode == 10)
              {
                  LoadCikklekerdezesFragment();
              }
              else if(keyCode == 11)
              {
                  finishAndRemoveTask();
              }
              return super.onKeyDown(keyCode, event);
      }
      if(tabbedFragment != null && tabbedFragment.isVisible())
      {
          if(keyCode == 111)
          {
             isPolc = false;
          }
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
                if(loginFragment != null && loginFragment.isVisible())
                {
                    loginFragment.SetId(barcodeData);
                    DolgKod = barcodeData;
                    loginFragment.StartSpinning();
                    CheckRights();
                    //loginFragment.onDestroy();
                }
                else if(tabbedFragment != null && tabbedFragment.isVisible())
                {
                    PolcThread();
                    tabbedFragment.GetID(DolgKod);
                    //tabbedFragment.onDestroy();
                }
                else if (cikklekerdezesFragment != null && cikklekerdezesFragment.isVisible())
                {
                    LoadEmptyFragment();
                    pi.clear();
                    ci.clear();
                    cikklekerdezesFragment.SetBinOrItem(barcodeData);
                    SQL();
                    //cikklekerdezesFragment.onDestroy();
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
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
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
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            barcodeReader.release();
        }
        Log.d(TAG, "onPause: ");
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
        Log.d(TAG, "onDestroy: ");
        unregisterReceiver(myBroadcastReceiver);
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
           // loginFragment.SetId(barcodeData);
            CheckRights();
            //loginFragment.onDestroy();
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
    class SQLCheckrights implements Runnable
    {
        @Override
        public void run() {
            try {
                RightCheck(barcodeData);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    class TextChange implements Runnable
    {
        String mText;

        TextChange(String text)
        {
            mText = text;
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loginFragment.SetId(mText);
                    loginFragment.StopSpinning();
                }
            });
        }
    }

    Runnable checkPolc = new Runnable() {
        @Override
        public void run() {
            try {
                PolcCheck(barcodeData);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    class IsPolc implements Runnable
    {
        String mCode;
        IsPolc(String code)
        {
            mCode = code;
        }
        @Override
        public void run() {
            handler1.post(new Runnable() {
                @Override
                public void run() {
                    tabbedFragment.GetFragmentAtPosition(mCode);
                }
            });
        }
    }
    class Animation implements Runnable
    {
        @Override
        public void run() {
            handler2.post(new Runnable() {
                @Override
                public void run() {
                    tabbedFragment.StartSpinning();
                }
            });
        }
    }

    class SetViews implements Runnable
    {
        SetViews(String desc1, String desc2, String unit)
        {
            mdesc1 = desc1;
            mdesc2 = desc2;
            munit = unit;
        }
        @Override
        public void run() {
            handler5.post(new Runnable() {
                @Override
                public void run() {
                    tabbedFragment.SetViews(mdesc1,mdesc2,munit);
                }
            });
        }
    }

    Runnable Animation2 = new Runnable() {
        @Override
        public void run() {
            handler3.post(new Runnable() {
                @Override
                public void run() {
                    tabbedFragment.StopSpinning();
                }
            });
        }
    };

    Runnable focus = new Runnable() {
        @Override
        public void run() {
               handler4.post(new Runnable() {
                   @Override
                   public void run() {
                        tabbedFragment.SetFocus();
                   }
               });
        }
    };
    private void PolcCheck(String code) throws ClassNotFoundException, SQLException {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(URL);
        if(connection!=null)
        {
            Statement statement = connection.createStatement();
            sql = String.format(getResources().getString(R.string.polcSql), code);
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next())
            {             //Megnézem hogy polc-e
                Statement statement1 = connection.createStatement();
                sql = String.format(getResources().getString(R.string.cikkSql),code);
                ResultSet resultSet1 = statement1.executeQuery(sql);
                if(!resultSet1.next()) //Megnézem hogy cikk-e
                {
                    StopAnimation();
                    GetPolc("Nincs a rendszerben");
                }
                else
                {
                    if(!isPolc)
                    {
                        StopAnimation();
                        GetPolc("Nem polc");
                    }
                    else
                    {
                        mdesc1 = resultSet1.getString("Description1");
                        mdesc2 = resultSet1.getString("Description2");
                        munit = resultSet1.getString("Unit");
                        SetViews(mdesc1,mdesc2,munit);
                        StopAnimation();
                        GetFocus();
                        GetPolc(barcodeData);
                    }
                }
            }
            else
                {
                    if(isPolc)
                    {
                        StopAnimation();
                        GetPolc("Cikket vigyél fel");
                    }
                    else
                    {
                        StopAnimation();
                        isPolc = true;
                        GetPolc(barcodeData);
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        connection = DriverManager.getConnection(connectionString);
                        Statement polcState = connection.createStatement();
                        String a;
                        a = String.format(getResources().getString(R.string.polcStatus),code);
                        ResultSet polcResult = polcState.executeQuery(a);
                        if(!polcResult.next())
                        {
                            //ide ha még nem volt ilyen polc
                            Log.d(TAG, "PolcCheck: ilyen még nem volt");
                        }
                        else if(polcResult.getInt("Statusz")==1)
                        {
                            //Ide ha már vettem fel rá valamit
                            Log.d(TAG, "PolcCheck: van rajta valami");
                        }
                        else if(polcResult.getInt("Statusz")==2)
                        {
                            //Ide ha fullosan zárolt
                            Log.d(TAG, "PolcCheck: fullosan zárolt");
                        }
                        else if(polcResult.getInt("Statusz")==0)
                        {
                            //Ide ha üres a polc
                        }

                    }
                }
        }
        else
        {
            StopAnimation();
           GetPolc("Nincs hálózat");
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
                        String megjegyzes1,megjegyzes2,unit,intrem;
                        megjegyzes1 = resultSet1.getString("Description1");
                        megjegyzes2 = resultSet1.getString("Description2");
                        unit = resultSet1.getString("Unit");
                        intrem = resultSet1.getString("IntRem");
                        Log.d("HONEY", "Connect1: ");
                        do {

                            ci.add(new CikkItems(resultSet1.getDouble("BalanceQty"),resultSet1.getString("BinNumber"), resultSet1.getString("Warehouse"), resultSet1.getString("QcCategory")));

                        }
                        while (resultSet1.next());
                        bundle.putSerializable("cikk",ci);
                        bundle.putString("megjegyzes",megjegyzes1);
                        bundle.putString("megjegyzes2",megjegyzes2);
                        bundle.putString("unit",unit);
                        bundle.putString("intrem",intrem);
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

    private void RightCheck(String barcodeData) throws SQLException {
        MenuFragment menuFragment;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if(connection != null)
        {
            Statement statement = connection.createStatement();
            String rightSql = String.format(getResources().getString(R.string.jog),barcodeData);
            ResultSet resultSet = statement.executeQuery(rightSql);
            if(!resultSet.next())
            {
               // loginFragment.SetId("Nincs jogosultságod belépni");
                SetText("Nincs jogosultságod belépni");
            }
            else
            {
                if(resultSet.getInt("Jog") == 1)
                {
                    hasRight = true;
                    menuFragment = MenuFragment.newInstance(true,"");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
                }
                else
                {
                    hasRight = false;
                    menuFragment = MenuFragment.newInstance(false,"");
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
                }
            }
        }
        else
        {
           SetText("Hálózati probléma");
        }

    }

    public void SQL()
    {
        SqlRunnable sqlRunnable = new SqlRunnable();
        new Thread(sqlRunnable).start();
    }
    private void CheckRights()
    {
        SQLCheckrights sqlCheckrights = new SQLCheckrights();
        new Thread(sqlCheckrights).start();
    }
    private void SetText(String text)
    {
        TextChange textChange = new TextChange(text);
        new Thread(textChange).start();
    }
    public void PolcThread()
    {
        new Thread(checkPolc).start();
        //return false;
    }
    public void GetPolc(String code)
    {
        IsPolc isPolc = new IsPolc(code);
        new Thread(isPolc).start();
    }
    public void StartAnimation()
    {
        Animation animation = new Animation();
        new Thread(animation).start();
    }
    public void StopAnimation()
    {
        new Thread(Animation2).start();
    }
    public void GetFocus()
    {
        new Thread(focus).start();
    }
    public void SetViews(String desc1, String desc2, String unit)
    {
        SetViews setViews = new SetViews(desc1,desc2,unit);
        new Thread(setViews).start();
    }
}