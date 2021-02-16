package com.example.mobilleltar.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
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
import com.example.mobilleltar.Fragments.LeltarozasFragment;
import com.example.mobilleltar.Fragments.LoginFragment;
import com.example.mobilleltar.Fragments.MainFragment;
import com.example.mobilleltar.Fragments.MenuFragment;
import com.example.mobilleltar.Fragments.PolcResultFragment;
import com.example.mobilleltar.Fragments.TabbedFragment;
import com.example.mobilleltar.R;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//jogosultsagot elmenteni és a nyomógombokat letiltani ahhoz képest

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange, BarcodeReader.BarcodeListener, LeltarozasFragment.SetTableView {

    private static final String TAG = "MainActivity";
    private TabbedFragment tabbedFragment;
    public MainFragment mainFragment = new MainFragment();
    
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
    private Handler handler6 = new Handler();

    public String DolgKod;
    private boolean hasRight;

    public boolean isPolc = false;
    private String mdesc1,mdesc2,munit;
    public String mRakt;
    public boolean isEmpty = false;
    public boolean isContains = false;
    private String polc;
    public String megjegyzes;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFragment = new LoginFragment();
        getSupportActionBar().hide();
       // FullScreencall();
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

   /* public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }*/

    public void LoadTabbedFragment()
    {
        tabbedFragment = new TabbedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,tabbedFragment,"TabbedFrag").addToBackStack(null).commit();
    }
   /* public void LoadBlank()
    {
        BlankFragment blankFragment = new BlankFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,blankFragment).commit();
    }*/

   /* public void LoadMenuFragment()
    {
        menuFragment = MenuFragment.newInstance(hasRight);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
        isPolc = false;
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
    public void loadForChange(String cikkszam, String megnevezes1, String megnevezes2, String mennyiseg, String megjegyzes) {
        tabbedFragment.setDataForChange(cikkszam,megnevezes1,megnevezes2,mennyiseg,megjegyzes);
    }

    @Override
    public void isUpdate(boolean update) {
        tabbedFragment.IsUpdate(update);
        tabbedFragment.EnableViews();
    }

    @Override
    public void oldMegjegyz(String megjegyzes) {
        this.megjegyzes = megjegyzes;
    }

    @Override
    public void getPos(int pos) {
        position = pos;
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
            try {
               CloseOccupied();
               mainFragment.ClearItems();
            }
            catch (Exception e)
            {
                ShowDialog(String.valueOf(e));
            }
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
              }
              else if (keyCode == 10)
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
             isContains = false;
             if(!polc.isEmpty())
             {
                CloseOccupied();
             }
          }

      }
      return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBarcodeEvent(
            BarcodeReadEvent barcodeReadEvent) {
        runOnUiThread(() -> {
            barcodeData = barcodeReadEvent.getBarcodeData();
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
            }
            else if(tabbedFragment != null && tabbedFragment.isVisible())
            {
                if(!tabbedFragment.IsMainFragment()) {
                    PolcThread();
                    tabbedFragment.GetID(DolgKod);
                }
            }
            else if (cikklekerdezesFragment != null && cikklekerdezesFragment.isVisible())
            {
                LoadEmptyFragment();
                pi.clear();
                ci.clear();
                cikklekerdezesFragment.SetBinOrItem(barcodeData);
                SQL();
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
        if(tabbedFragment != null && tabbedFragment.isVisible())
        {
            tabbedFragment.ClearAllViewsAndPolc();
            ShowDialog("Újra be kell olvasnod a leltározandó polcot");
        }
        isPolc = false;

        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            barcodeReader.release();
        }
        try
        {
            CloseOccupied();
            mainFragment.ClearItems();
        }catch (Exception e)
        {
            Log.d(TAG, "onPause: nem írta fölül");
        }
        Log.d(TAG, "onPause: ");
    }
    @Override
    public void onDestroy() {
      /*  try {
            if (!polc.isEmpty()) {
                UpdateLocked();
            }
        }
        catch (Exception e)
        {
            //ShowDialog(String.valueOf(e));
        }*/
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
        decodedData = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data));

        FragmentManager manager = getSupportFragmentManager();
        LoginFragment loginFragment = (LoginFragment)manager.findFragmentByTag("LoginFrag");
        TabbedFragment tabbedFragment = (TabbedFragment)manager.findFragmentByTag("TabbedFrag");
        CikklekerdezesFragment cikklekerdezesFragment = (CikklekerdezesFragment)manager.findFragmentByTag("CikkFrag");
        if(loginFragment != null)
        {
            barcodeData = decodedData;
            loginFragment.SetId(decodedData);
            CheckRights();

        }
        else if(tabbedFragment != null && tabbedFragment.isVisible())
        {
            barcodeData = decodedData;
            if(!tabbedFragment.IsMainFragment()) {
                PolcThread();
                tabbedFragment.GetID(DolgKod);
            }
            //tabbedFragment.onDestroy();
        }
       /* else if(tabbedFragment != null)
        {
            tabbedFragment.GetFragmentAtPosition(decodedData);
            tabbedFragment.onDestroy();
        }*/
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

    @Override
    public void setDataToSend(String a, String b, String c, String d, String e) {
        tabbedFragment.PushData(a,b,c,d,e);
    }

    @Override
    public void isEmpty(boolean a) {
        isEmpty = a;
    }

    @Override
    public void isContains(boolean a) {
        isContains = a;
    }

    @Override
    public void isClosed() {
        polc = "";
    }

    class SqlRunnable implements Runnable
    {
        @Override
        public void run() {
                Connect(barcodeData);
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
            handler.post(() -> {
                loginFragment.SetId(mText);
                loginFragment.StopSpinning();
            });
        }
    }

    Runnable checkPolc = new Runnable() {
        @Override
        public void run() {
            try {
                PolcCheck(barcodeData);
            } catch (ClassNotFoundException | SQLException e) {
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
            handler1.post(() -> tabbedFragment.GetFragmentAtPosition(mCode));
        }
    }
    class Animation implements Runnable
    {
        @Override
        public void run() {
            handler2.post(() -> tabbedFragment.StartSpinning());
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
            handler5.post(() -> tabbedFragment.SetViews(mdesc1,mdesc2,munit));
        }
    }

    Runnable Animation2 = new Runnable() {
        @Override
        public void run() {
            handler3.post(() -> tabbedFragment.StopSpinning());
        }
    };

   class ListCucc implements Runnable
   {
        String ma,mb,mc,md,me;
        ListCucc (String a,String b,String c,String d,String e)
       {
           ma = a;
           mb = b;
           mc = c;
           md = d;
           me = e;
       }

       @Override
       public void run() {
           handler6.post(() -> tabbedFragment.PushData(ma,mb,mc,md,me));
       }
   }

   class RaktarName implements Runnable
   {
       String raktarName;
       RaktarName(String raktar)
       {
           raktarName = raktar;
       }

       @Override
       public void run() {
           handler.post(() -> tabbedFragment.SetInternalName(raktarName));
       }
   }

    Runnable focus = new Runnable() {
        @Override
        public void run() {
               handler4.post(() -> tabbedFragment.SetFocus());
        }
    };

   class InsertRows implements Runnable
   {
       String a,b,c,d,e,f,g,h,i;
       InsertRows(String cikk, String mennyiseg, String dolgozo, String raktar, String rakhely, String megjegyzes, String nyomtatva, String status, String ellStatus)
       {
           a = cikk;
           b = mennyiseg;
           c = dolgozo;
           d = raktar;
           e = rakhely;
           f = megjegyzes;
           g = nyomtatva;
           h = status;
           i = ellStatus;
       }

       @Override
       public void run() {
           try {
               InsertRow(a,b,c,d,e,f,g,h,i);
           } catch (ClassNotFoundException | SQLException ex) {
               ex.printStackTrace();
           }
       }
   }

   Runnable rakhelyEll = () -> {
       try {
           InsertRakhelyEll();
       } catch (ClassNotFoundException | SQLException e) {
           e.printStackTrace();
       }
   };
   Runnable polcClear = new Runnable() {
       @Override
       public void run() {
           handler.post(() -> tabbedFragment.ClearAllViewsAndPolc());

       }
   };
   class Dialog implements Runnable
   {
       String mText;

       Dialog(String text)
       {
           mText = text;
       }

       @Override
       public void run() {
           handler.post(() -> Dialog(mText));
       }
   }

   class CloseRakh implements Runnable
   {
       String code;
        CloseRakh(String a)
        {
            code = a;
        }
       @Override
       public void run() {
           try {
               CloseRakh(code);
           } catch (ClassNotFoundException | SQLException e) {
               e.printStackTrace();
           }
       }
   }
   class ResultView implements Runnable
   {
       int value;
       ResultView(int a)
       {
           value = a;
       }

       @Override
       public void run() {
           handler.post(() -> tabbedFragment.updateTabView(value));
       }
   }
   class UpdateItem implements Runnable
   {
        String a,b,c,d;
        UpdateItem(String x, String y,String z,String w)
        {
            a = x;
            b = y;
            c = z;
            d = w;
        }
       @Override
       public void run() {
           try {
               UpdateItem(a,b,c,d);
           } catch (ClassNotFoundException | SQLException e) {
               e.printStackTrace();
           }
       }
   }
   class UpdateListItems implements Runnable
    {
        int position;
        UpdateListItems(int a)
        {
            position = a;
        }
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.UpdateTable(position));
        }
    }
    Runnable updateLocked = () -> {
        try {
            CloseRakh("1");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    };
   Runnable setLocked = () -> {
       try {
           CloseVacant("1");
       } catch (ClassNotFoundException | SQLException e) {
           e.printStackTrace();
       }
   };

    private void PolcCheck(String code) throws ClassNotFoundException, SQLException {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(URL);
        if(connection!=null)
        {
            Statement statement = connection.createStatement();
            sql = String.format(getResources().getString(R.string.isPolc), code);
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next())
            {             //Megnézem hogy polc-e
                Statement statement1 = connection.createStatement();
                sql = String.format(getResources().getString(R.string.cikkSql),code);
                ResultSet resultSet1 = statement1.executeQuery(sql);
                if(code.equals("EMPTY")) //Megnézem hogy cikk-e
                {
                    if(!isContains) {
                        //ide ha nem lett felvéve
                        //InsertRakhelyEll();
                        //IDE KELL A LEZÁRÁS
                        CloseRakh("0");
                        StopAnimation();
                        //GetPolc("A polc üres");
                        ClearPolc();
                        isPolc = false;
                        isContains = false;
                        polc = "";
                    }
                    else 
                    {
                        Log.d(TAG, "PolcCheck: NEM LEHET ÜRES VAN RAJTA CUCC");
                        //IDE EGY DIALOGOT
                        ShowDialog("A polcra már vételeztek, nem lehet üres");
                        StopAnimation();
                    }
                }
                else if (!resultSet1.next())
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
            }//HA POLC
            else
                {   //HA MÁR VETTEM FEL POLCOT
                    isEmpty = false;
                    String raktar = resultSet.getString("InternalName");
                    mRakt = resultSet.getString("WarehouseID");
                    if(isPolc)
                    {
                        StopAnimation();
                        GetPolc("Cikket vigyél fel");
                    }
                    else
                    {
                        // HA POLC, MEGNÉZEM A STÁTUSZÁT
                        isPolc = true;
                        GetPolc(barcodeData);
                        polc = barcodeData;
                        SetRaktar(raktar);
                        Class.forName("net.sourceforge.jtds.jdbc.Driver");
                        connection = DriverManager.getConnection(connectionString);
                        Statement polcState = connection.createStatement();
                        String a;
                        a = String.format(getResources().getString(R.string.polcStatus),code);
                        ResultSet polcResult = polcState.executeQuery(a);
                        if(!polcResult.next())
                        {
                            //megnézem hogy üres -e a polc
                            InsertLocked();
                            isEmpty = true;
                            StopAnimation();
                        }
                        else if(polcResult.getInt("Statusz")==1)
                        {
                            //isPolc = true;
                            CloseRakh("3");
                            //Ide ha már vettem fel rá valamit
                            do {
                                SendList(polcResult.getString("Cikkszam"),polcResult.getString("Description1"),polcResult.getString("Description2"),
                                        polcResult.getString("Mennyiseg"),polcResult.getString("Megjegyzes"));
                            }while(polcResult.next());
                            StopAnimation();
                            if(!isEmpty) {
                                isContains = true;
                                String x = String.format("A(z) %s polcon cikkek vannak", polc);
                                ShowDialog(x);
                                Log.d(TAG, "PolcCheck: van rajta valami");
                                ChangeView(1);
                            }
                        }
                        else if(polcResult.getInt("Statusz")==2)
                        {
                            //Ide ha fullosan zárolt
                            Log.d(TAG, "PolcCheck: fullosan zárolt");
                            StopAnimation();
                            ClearPolc();
                            String x = String.format("A(z) %s polc zárolva van",polc);
                            ShowDialog(x);
                            isPolc = false;
                            polc = "";
                        }
                        else if(polcResult.getInt("Statusz")==0)
                        {
                            //Ide ha üres a polc
                            Log.d(TAG, "PolcCheck: a polc üres");
                            StopAnimation();
                            ClearPolc();
                            GetPolc("A polc üres");
                            String x = String.format("A(z) %s polc üres",polc);
                            ShowDialog(x);
                            isPolc = false;
                        }
                        else if(polcResult.getInt("Statusz")==3)
                        {
                            Log.d(TAG, "PolcCheck: a polc leltár alatt van");
                            StopAnimation();
                            ClearPolc();
                            GetPolc("A polc nem elérhető");
                            isPolc = false;
                            String x = String.format("A(z) %s polcon jelenleg leltároznak",polc);
                            ShowDialog(x);
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

    private void Connect(String code) {
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
                String a = e.getMessage();
                EmptyFragment emptyFragment = EmptyFragment.newInstance(a,"");
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
                    menuFragment = MenuFragment.newInstance(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
                }
                else
                {
                    hasRight = false;
                    menuFragment = MenuFragment.newInstance(false);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
                }
            }
        }
        else
        {
           SetText("Hálózati probléma");
        }

    }
   
    public void InsertRow(String cikk, String mennyiseg, String dolgozo, String raktar, String rakhely, String megjegyzes, String nyomtatva, String status, String ellStatus) throws ClassNotFoundException, SQLException {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null) {
            Statement statement = connection.createStatement();
           // String a = "INSERT INTO [leltar].[dbo].[Leltaradat] (Cikkszam,Mennyiseg,Dolgozo,Raktar,RaktHely,Megjegyzes,Nyomtatva,Status,EllStatus) VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')";
            String sql;
            sql = String.format(getResources().getString(R.string.insertRow),cikk,mennyiseg,dolgozo,raktar,rakhely,megjegyzes,nyomtatva,status,ellStatus);
            Log.d(TAG, "InsertRow: "+sql);
            statement.executeUpdate(sql);
            StopAnimation();
        }
        else
        {
            //ide ha nincs connection
            Log.d(TAG, "InsertRow: NO CONNECTION");
            StopAnimation();
        }
    }

    public void InsertLocked()throws ClassNotFoundException, SQLException
    {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null) {
            // String s = "INSERT INTO [leltar].[dbo].[LeltarRakhEll] (RaktHely,DolgozoKezd,Statusz,KezdDatum) VALUES('%s','%s','%s','%s')";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String datetime = simpleDateFormat.format(new Date());
            String sql;
            try {
                sql = String.format(getResources().getString(R.string.insertRakh), polc, DolgKod, "3", datetime);
                Statement rakhEll = connection.createStatement();
                rakhEll.executeUpdate(sql);
                StopAnimation();
            }
            catch (Exception e)
            {
                ShowDialog(String.valueOf(e));
            }
        }
    }

    private void InsertRakhelyEll() throws ClassNotFoundException, SQLException {
          Class.forName("net.sourceforge.jtds.jdbc.Driver");
          connection = DriverManager.getConnection(connectionString);
          if(connection!=null) {
             // String s = "INSERT INTO [leltar].[dbo].[LeltarRakhEll] (RaktHely,DolgozoKezd,Statusz,KezdDatum) VALUES('%s','%s','%s','%s')";
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
              String datetime = simpleDateFormat.format(new Date());
              String sql;
              if(isContains)
              {
                  sql = String.format(getResources().getString(R.string.insertRakh), polc, DolgKod, "1", datetime);
              }
              else {
                  sql = String.format(getResources().getString(R.string.insertRakh), polc, DolgKod, "0", datetime);
              }
              Statement rakhEll = connection.createStatement();
              rakhEll.executeUpdate(sql);
              StopAnimation();
          }
    }

    private void CloseRakh(String code)throws ClassNotFoundException, SQLException
    {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null) {
            // String s = "INSERT INTO [leltar].[dbo].[LeltarRakhEll] (RaktHely,DolgozoKezd,Statusz,KezdDatum) VALUES('%s','%s','%s','%s')";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String datetime = simpleDateFormat.format(new Date());
            String sql;
            sql = String.format(getResources().getString(R.string.closeRakh),DolgKod,code,datetime,polc);
            Statement closeState = connection.createStatement();
            try
            {
                closeState.executeUpdate(sql);
                StopAnimation();
               // isPolc = false;
                //tabbedFragment.ClearAllViewsAndPolc();
            }
            catch (Exception e)
            {
                StopAnimation();
                ShowDialog(String.valueOf(e));
            }
        }
        else
        {
            StopAnimation();
            ShowDialog("Hálózati probléma");
        }
    }
    private void CloseVacant(String code) throws ClassNotFoundException, SQLException {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null) {
            // String s = "INSERT INTO [leltar].[dbo].[LeltarRakhEll] (RaktHely,DolgozoKezd,Statusz,KezdDatum) VALUES('%s','%s','%s','%s')";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String datetime = simpleDateFormat.format(new Date());
            String sql;
            sql = String.format(getResources().getString(R.string.closeRakh),DolgKod,code,datetime,polc);
            Statement closeState = connection.createStatement();
            try {
                closeState.executeUpdate(sql);
            }catch (Exception e)
            {
                Log.d(TAG, "CloseVacant: ");
            }
        }
        else
            Log.d(TAG, "CloseVacant: Nincs hálózat");
    }

    private void UpdateItem(String mennyisegUj, String megjegyzesUj, String cikkszam, String mennyisegRegi) throws ClassNotFoundException, SQLException {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null)
        {
            String sql;
            sql = String.format(getResources().getString(R.string.updateItem),mennyisegUj,megjegyzesUj,mennyisegRegi,cikkszam);
            Statement updateStatement = connection.createStatement();
            try
            {
                updateStatement.executeUpdate(sql);
               // tabbedFragment.UpdateTable();
                UpdateList(position);
                StopAnimation();
            }
            catch (Exception e)
            {
                ShowDialog(String.valueOf(e));
                StopAnimation();
            }
        }
        else
        {
            ShowDialog("Hálózati probléma");
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
    public void ClearViews()
    {
        tabbedFragment.ClearAllViews();
    }
    private void ClearPolc()
    {
       // tabbedFragment.ClearAllViewsAndPolc();
        new Thread(polcClear).start();
    }
    public void SendList(String a,String b,String c,String d,String e)
    {
        ListCucc listCucc = new ListCucc(a,b,c,d,e);
        new Thread(listCucc).start();
    }
    public void SetRaktar(String raktar)
    {
        RaktarName raktarName = new RaktarName(raktar);
        new Thread(raktarName).start();
    }
    public void InsertNewRow(String cikk, String mennyiseg, String dolgozo, String raktar, String rakhely, String megjegyzes, String nyomtatva, String status, String ellStatus)
    {
        InsertRows insertRows = new InsertRows(cikk,mennyiseg,dolgozo,raktar,rakhely,megjegyzes,nyomtatva,status,ellStatus);
        new Thread(insertRows).start();
    }
    public void InsertRakhEll()
    {
        new Thread(rakhelyEll).start();
    }
    private void Dialog(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Figyelem")
                .setMessage(text);
        builder.create();
        builder.show();
    }
    private void ShowDialog(String text)
    {
        Dialog dialog = new Dialog(text);
        new Thread(dialog).start();
    }
    public void CloseRakhely(String a)
    {
        CloseRakh closeRakh = new CloseRakh(a);
        new Thread(closeRakh).start();
    }
    private void ChangeView(int viewNum)
    {
        ResultView resultView = new ResultView(viewNum);
        new Thread(resultView).start();
    }
    public void UpdateItems(String a, String b, String c, String d)
    {
        UpdateItem updateItem = new UpdateItem(a,b,c,d);
        new Thread(updateItem).start();
    }
    public void UpdateList(int pos)
    {
        UpdateListItems updateListItems = new UpdateListItems(pos);
        new Thread(updateListItems).start();
    }
    public void UpdateLocked()
    {
        new Thread(updateLocked).start();
    }
    public void CloseOccupied()
    {
        new Thread(setLocked).start();
    }

}