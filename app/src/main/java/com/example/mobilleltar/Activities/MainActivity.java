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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MainFragment.TabChange, BarcodeReader.BarcodeListener, LeltarozasFragment.SetTableView, CikklekerdezesFragment.SetItemOrBinManually {


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

    public String DolgKod;
    private boolean hasRight;

    public boolean isPolc = false;
    private String mdesc1,mdesc2,munit;
    public String mRakt;
    public boolean isEmpty = false;
    public boolean isContains = false;
    private String polc ="";
    public String megjegyzes;
    private int position;
    private boolean onResume = false;
    private boolean isClosed = false;
    private String mBiz = "";

 //2021.03.18 zebra elküldés
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

    //OVERRIDE METHODOK

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // Bundle b = intent.getExtras();

            if (action.equals(getResources().getString(R.string.dw_action))) {

                try {
                    displayScanResult(intent);
                } catch (Exception ignored) {

                }
            }
        }
    };

    //SYSTEM OVERRIDE

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
             /* else if (keyCode == 9)
              {
                  Toast.makeText(getApplicationContext(), "Nincs jogosultságod belépni ", Toast.LENGTH_SHORT).show();
              }*/
              else if (keyCode == 9)
              {
                  LoadCikklekerdezesFragment();
              }
              else if(keyCode == 10)
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
          if(keyCode == 21 && onResume)
          {
              try {
                  tabbedFragment.SetFocus1();
              }catch (Exception e)
              {
                  Log.d(TAG, "onKeyDown: ");
              }
          }
      }
      return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
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
                    PolcThread(barcodeData);
                    tabbedFragment.GetID(DolgKod);
                    onResume = false;
                }
            }
            else if (cikklekerdezesFragment != null && cikklekerdezesFragment.isVisible())
            {
                LoadEmptyFragment("Várom az eredményt");
                pi.clear();
                ci.clear();
                cikklekerdezesFragment.SetBinOrItem(barcodeData);
                SQL(barcodeData);
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
            tabbedFragment.SetEnabledFalse();
            ShowDialog("Újra be kell olvasnod a leltározandó polcot");
            SetCikkFocus();//ide egy másikat ami letiltja
            isClosed = false;

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
            if(!isClosed) {
                CloseOccupied();
                tabbedFragment.updateTabView(0);
                mainFragment.ClearItems();
                tabbedFragment.ClearAllViewsAndPolc();
                onResume = true;
            }

            //polc = "";
        }catch (Exception e)
        {
            Log.d(TAG, "onPause: nem írta fölül");
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
        try {
            connection.close();
        }catch (Exception e)
        {
            Log.d(TAG, "onDestroy: Connection close failed");
        }
    }

    // INTERFACEK
    @Override
    public void tabChangeListener(int index) {
        tabbedFragment.updateTabView(index);
    }
    @Override
    public void loadForChange(String cikkszam, String megnevezes1, String megnevezes2, String mennyiseg, String megjegyzes,String biz) {
        tabbedFragment.setDataForChange(cikkszam,megnevezes1,megnevezes2,mennyiseg,megjegyzes);
        mBiz = biz;
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
    @Override
    public void setDataToSend(String a, String b, String c, String d, String e,String biz) {
        tabbedFragment.PushData(a,b,c,d,e,biz);
    }
    @Override
    public void setDataToSendAndRemove() {
        tabbedFragment.UpdateTable(position);
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
        isClosed = true;
    }
    @Override
    public void setValue(String value) {
        LoadEmptyFragment("Várom az eredményt");
        pi.clear();
        ci.clear();
        SQL(value);
    }

    // OSZTÁLYOK/RUNNABLE

   class SqlRunnable implements Runnable
   {
        String barcode;
        SqlRunnable (String code)
        {
            barcode = code;
        }
        @Override
        public void run() {
                ConnectSql(barcode);
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
   class CheckPolc implements Runnable
   {
        String itemCode;
        CheckPolc(String code)
        {
            itemCode = code;
        }
        @Override
        public void run() {
            try {
                PolcCheck(itemCode);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                ShowDialog("Hálózati probléma");
                StopAnimation();
            }
        }
    }
   class IsPolc implements Runnable
   {
        String mCode;
        IsPolc(String code)
        {
            mCode = code;
        }
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.GetFragmentAtPosition(mCode));
        }
    }
   class Animation implements Runnable
   {
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.StartSpinning());
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
            handler.post(() -> tabbedFragment.SetViews(mdesc1,mdesc2,munit));
        }
    }
   class ListCucc implements Runnable
   {
        String ma,mb,mc,md,me,mBiz;
        ListCucc (String a,String b,String c,String d,String e,String biz)
       {
           ma = a;
           mb = b;
           mc = c;
           md = d;
           me = e;
           mBiz = biz;
       }
       @Override
       public void run() {
           handler.post(() -> tabbedFragment.PushData(ma,mb,mc,md,me,mBiz));
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
   class OnlyItemClass implements Runnable
   {
        String itemCode;
        OnlyItemClass(String item)
        {
            itemCode = item;
        }
        @Override
        public void run() {
            try {
                OnlyItem(itemCode);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
   class SetItem implements Runnable
   {
        String itemCode;
        SetItem(String code)
        {
            itemCode = code;
        }
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.SetItem(itemCode));
        }
    }

   Runnable setLocked = () -> {
        try {
            CloseVacant("1");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    };
   Runnable focus = new Runnable() {
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.SetFocus());
        }
    };
   Runnable Animation2 = new Runnable() {
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.StopSpinning());
        }
    };
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
   Runnable offFocus = new Runnable() {
        @Override
        public void run() {
            handler.post(() -> tabbedFragment.SetCikkFocusOff());
        }
    };
   Runnable focusOn = new Runnable() {
       @Override
       public void run() {
            handler.post(() -> tabbedFragment.SetFocus1());
       }
   };
   Runnable focusOff = new Runnable() {
       @Override
       public void run() {
        handler.post(() -> tabbedFragment.SetFocusOff());
       }
   };
   Runnable readItems = () -> {
       try {
           ReadItems(polc);
       } catch (ClassNotFoundException | SQLException e) {
           e.printStackTrace();
       }
   };

   //SQL KEZELÉS

    private void OnlyItem (String code) throws ClassNotFoundException, SQLException {
       StartAnimation();
       Class.forName("net.sourceforge.jtds.jdbc.Driver");
       connection = DriverManager.getConnection(URL);
       if(connection != null) {
           PreparedStatement statement1 = connection.prepareStatement(getResources().getString(R.string.cikkSql));
           statement1.setString(1,code);
           //Statement statement1 = connection.createStatement();
           //sql = String.format(getResources().getString(R.string.cikkSql), code);
           ResultSet resultSet1 = statement1.executeQuery();
           if (code.equals("EMPTY")) //Megnézem hogy cikk-e
           {
               if (!isContains) {
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
               } else {
                   Log.d(TAG, "PolcCheck: NEM LEHET ÜRES VAN RAJTA CUCC");
                   //IDE EGY DIALOGOT
                   ShowDialog("A polcra már vételeztek, nem lehet üres");
                   StopAnimation();
               }
           } else if (!resultSet1.next()) {
               StopAnimation();
               FocusOn();
               SetMennyFocusOff();
               //mennyiség fókuszt
               ShowDialog("Biztos nincs a rendszerben");

           } else {
               if (!isPolc) {
                   StopAnimation();
                   SetMennyFocusOff();
                   FocusOn();
                   //mennyiség fókuszt
                   ShowDialog("Biztos nincs a rendszerben");
               } else {
                   mdesc1 = resultSet1.getString("Description1");
                   mdesc2 = resultSet1.getString("Description2");
                   munit = resultSet1.getString("Unit");
                   SetViews(mdesc1, mdesc2, munit);
                   StopAnimation();
                   GetFocus();
                   SetItem(code);
                   StopAnimation();
               }
           }
         //  connection.close();
       }
       else
       {
           ShowDialog("Hálózati probléma");
       }
   }

    private void PolcCheck(String code) throws ClassNotFoundException, SQLException {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(URL);
        if(connection!=null)
        {
            PreparedStatement statement = connection.prepareStatement(getResources().getString(R.string.isPolc));
            statement.setString(1,code);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next())
            {//Megnézem hogy polc-e
                PreparedStatement statement1 = connection.prepareStatement(getResources().getString(R.string.cikkSql));
                statement1.setString(1,code);
                ResultSet resultSet1 = statement1.executeQuery();
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
                    SetMennyFocusOff();
                    if(tabbedFragment.GetPolcText()) {
                        SetCikkFocus();
                    } else {
                        FocusOn();
                    }
                    ShowDialog("Nincs a rendszerben");
                }
                else
                {
                    if(!isPolc)
                    {
                        StopAnimation();
                        SetMennyFocusOff();
                        SetCikkFocus();
                        ShowDialog("Nem polc");
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
                        ShowDialog("Cikket vigyél fel");
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
                        Statement polcState = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                        String a;
                        a = String.format(getResources().getString(R.string.polcStatus),code);
                        ResultSet polcResult = polcState.executeQuery(a);
                        if(!polcResult.next())
                        {
                            //megnézem hogy üres -e a polc
                                InsertLocked();
                                isEmpty = true;
                                StopAnimation();
                                FocusOn();
                        }
                        else if(polcResult.getInt("Statusz")==1)
                        {
                            polcResult.beforeFirst();
                            CloseRakh("3");
                            //Ide ha már vettem fel rá valamit
                                try {
                                    if(!polcResult.next())
                                    {
                                        FocusOn();
                                    }
                                    else {
                                        FocusOn();
                                        isEmpty = false;
                                        polcResult.beforeFirst();
                                        while(polcResult.next()){
                                            if(!polcResult.getString("Cikkszam").equals("")) {
                                                SendList(polcResult.getString("Cikkszam"), polcResult.getString("Description1"), polcResult.getString("Description2"), polcResult.getString("Mennyiseg"), polcResult.getString("Megjegyzes"),polcResult.getString("Bizszam"));
                                            }
                                            else {
                                                isEmpty = true;
                                            }
                                        }
                                        if (!isEmpty) {
                                            isContains = true;
                                            String x = String.format("A(z) %s polcon cikkek vannak", polc);
                                            ShowDialog(x);
                                            Log.d(TAG, "PolcCheck: van rajta valami");
                                            ChangeView(1);
                                        }
                                    }
                                    StopAnimation();
                                } catch (Exception e) {
                                    Log.d(TAG, "PolcCheck: üres a polc");
                                    //Log.d(TAG, "PolcCheck: "+ String.valueOf(e));
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
                            SetCikkFocus();
                            //tabbedFragment.SetFocusOff();
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
                            SetCikkFocus();
                        }
                        else if(polcResult.getInt("Statusz")==3)
                        {
                            Log.d(TAG, "PolcCheck: a polc leltár alatt van");
                            StopAnimation();
                            ClearPolc();
                            //GetPolc("A polc nem elérhető");
                            GetPolc("");
                            isPolc = false;
                            polc = "";
                            SetCikkFocus();
                            String x = String.format("A(z) %s polcon jelenleg leltároznak",polc);
                            ShowDialog(x);
                        }
                    }
                }
           // connection.close();
        }
        else
        {
            StopAnimation();
           GetPolc("Hálózati probléma");
        }
    }

    private void ReadItems (String code) throws ClassNotFoundException, SQLException {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(URL);
        if(connection != null) {
            Statement polcState = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String a;
            a = String.format(getResources().getString(R.string.polcStatus), code);
            try {
                ResultSet res = polcState.executeQuery(a);
            if (!res.next()) {
                //megnézem hogy üres -e a polc
                Log.d(TAG, "ReadItems: ");

            } else if (res.getInt("Statusz") == 3) {
                res.beforeFirst();
                //Ide ha már vettem fel rá valamit
                try {
                    if (!res.next()) {
                        Log.d(TAG, "ReadItems: ");
                    } else {
                        res.beforeFirst();
                        while (res.next()) {
                            if (!res.getString("Cikkszam").equals("")) {
                                SendList(res.getString("Cikkszam"), res.getString("Description1"), res.getString("Description2"), res.getString("Mennyiseg"), res.getString("Megjegyzes"), res.getString("Bizszam"));
                            } else {
                                Log.d(TAG, "ReadItems: ");
                            }
                        }

                    }
                } catch (Exception e) {
                    Log.d(TAG, "PolcCheck: üres a polc");
                    //Log.d(TAG, "PolcCheck: "+ String.valueOf(e));
                }
            }
            }catch (Exception ad)
            {
                Log.d(TAG, String.valueOf(ad));
            }
           // connection.close();
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
            PreparedStatement statement = connection.prepareStatement(getResources().getString(R.string.jog));
            statement.setString(1,barcodeData);
            //Statement statement = connection.createStatement();
            //String rightSql = String.format(getResources().getString(R.string.jog),barcodeData);
            ResultSet resultSet = statement.executeQuery();
            if(!resultSet.next())
            {
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
          //  connection.close();
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
            PreparedStatement statement = connection.prepareStatement(getResources().getString(R.string.insertRow));
            statement.setString(1,cikk);
            statement.setString(2,mennyiseg);
            statement.setString(3,dolgozo);
            statement.setString(4,raktar);
            statement.setString(5,rakhely);
            statement.setString(6,megjegyzes);
            statement.setString(7,nyomtatva);
            statement.setString(8,status);
            statement.setString(9,ellStatus);
            //Statement statement = connection.createStatement();
           // String sql;
            //sql = String.format(getResources().getString(R.string.insertRow),cikk,mennyiseg,dolgozo,raktar,rakhely,megjegyzes,nyomtatva,status,ellStatus);
            Log.d(TAG, "InsertRow: "+sql);
            statement.executeUpdate();
            StopAnimation();
          //  connection.close();
        }
        else
        {
            //ide ha nincs connection
            Log.d(TAG, "InsertRow: NO CONNECTION");
            StopAnimation();
        }
    }

    public void InsertLocked()throws ClassNotFoundException, SQLException {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null) {
            // String s = "INSERT INTO [leltar].[dbo].[LeltarRakhEll] (RaktHely,DolgozoKezd,Statusz,KezdDatum) VALUES('%s','%s','%s','%s')";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String datetime = simpleDateFormat.format(new Date());
            String sql;
            try {
                PreparedStatement rakhEll = connection.prepareStatement(getResources().getString(R.string.insertRakh));
                rakhEll.setString(1,polc);
                rakhEll.setString(2,DolgKod);
                rakhEll.setString(3,"3");
                rakhEll.setString(4,datetime);
                rakhEll.executeUpdate();
                StopAnimation();
            }
            catch (Exception e)
            {
                ShowDialog(String.valueOf(e));
            }
          //  connection.close();
        }
    }

    private void InsertRakhelyEll() throws ClassNotFoundException, SQLException {
          Class.forName("net.sourceforge.jtds.jdbc.Driver");
          connection = DriverManager.getConnection(connectionString);
          if(connection!=null) {
              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
              String datetime = simpleDateFormat.format(new Date());
              PreparedStatement rakhEll = connection.prepareStatement(getResources().getString(R.string.insertRakh));
              if(isContains)
              {
                  rakhEll.setString(1,polc);
                  rakhEll.setString(2,DolgKod);
                  rakhEll.setString(3,"1");
                  rakhEll.setString(4,datetime);
              }
              else {
                  rakhEll.setString(1,polc);
                  rakhEll.setString(2,DolgKod);
                  rakhEll.setString(3,"0");
                  rakhEll.setString(4,datetime);
              }
              rakhEll.executeUpdate();
              StopAnimation();
             // connection.close();
          }
    }

    private void CloseRakh(String code)throws ClassNotFoundException, SQLException    {
        StartAnimation();
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(connectionString);
        if(connection!=null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String datetime = simpleDateFormat.format(new Date());
            PreparedStatement closeState = connection.prepareStatement(getResources().getString(R.string.closeRakh));
            closeState.setString(1,DolgKod);
            closeState.setString(2,code);
            closeState.setString(3,datetime);
            closeState.setString(4,polc);
            try
            {
                closeState.executeUpdate();
                StopAnimation();
            }
            catch (Exception e)
            {
                StopAnimation();
                ShowDialog(String.valueOf(e));
            }
           // connection.close();
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String datetime = simpleDateFormat.format(new Date());
            String sql;
            PreparedStatement closeState = connection.prepareStatement(getResources().getString(R.string.closeRakh));
            closeState.setString(1,DolgKod);
            closeState.setString(2,code);
            closeState.setString(3,datetime);
            closeState.setString(4,polc);
            try {
                closeState.executeUpdate();
            }catch (Exception e)
            {
                Log.d(TAG, "CloseVacant: ");
            }
          //  connection.close();
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
            PreparedStatement updateStatement = connection.prepareStatement(getResources().getString(R.string.updateItem));
            updateStatement.setString(1,mennyisegUj);
            updateStatement.setString(2,megjegyzesUj);
            updateStatement.setString(3,mBiz);
           /* String sql;
            sql = String.format(getResources().getString(R.string.updateItem),mennyisegUj,megjegyzesUj,mennyisegRegi,cikkszam);
            Statement updateStatement = connection.createStatement();*/
            try
            {
                updateStatement.executeUpdate();
                StopAnimation();
            }
            catch (Exception e)
            {
                ShowDialog(String.valueOf(e));
                StopAnimation();
            }
          //  connection.close();
        }
        else
        {
            ShowDialog("Hálózati probléma");
        }
    }

    private void ConnectSql(String code) {
        PolcResultFragment polcResultFragment = new PolcResultFragment();
        CikkResultFragment cikkResultFragment = new CikkResultFragment();
        Bundle bundle = new Bundle();
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(URL);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if(connection != null){
            try {
                PreparedStatement statement = connection.prepareStatement(getResources().getString(R.string.isPolc));
                statement.setString(1,code);
                ResultSet resultSet = statement.executeQuery();
                if(!resultSet.next())
                {
                    PreparedStatement statement1 = connection.prepareStatement(getResources().getString(R.string.cikkSql));
                    statement1.setString(1,code);
                    ResultSet resultSet1 = statement1.executeQuery();
                    if(!resultSet1.next())
                    {
                       /* EmptyFragment emptyFragment = EmptyFragment.newInstance("Nincs találat","");
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();*/
                        LoadEmptyFragment("");
                    }
                    else
                    {
                        //itt kell a cikkeket feltölteni
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
                    PreparedStatement statement2 = connection.prepareStatement(getResources().getString(R.string.polcSql));
                    statement2.setString(1,code);
                    ResultSet resultSet2 = statement2.executeQuery();
                    if(!resultSet2.next())
                    {
                        /*EmptyFragment emptyFragment = EmptyFragment.newInstance("A polc üres","");
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();*/
                        LoadEmptyFragment("A poc üres");
                    }
                    else
                    {
                        //itt kell a polcot feltölteni
                        Log.d("HONEY", "Connect: ");
                        do {
                            pi.add(new PolcItems(resultSet2.getDouble("BalanceQty"), resultSet2.getString("Unit"), resultSet2.getString("Description1"), resultSet2.getString("Description2"), resultSet2.getString("IntRem"), resultSet2.getString("QcCategory")));
                        }
                        while (resultSet2.next());
                        bundle.putSerializable("polc",pi);
                        polcResultFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,polcResultFragment,"PolcResultFrag").commit();
                    }
                }
            }
            catch (Exception e)
            {
                /*EmptyFragment emptyFragment = EmptyFragment.newInstance(String.valueOf(e),"");
                getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();*/
                LoadEmptyFragment(String.valueOf(e));
            }
        }
        else
        {
           /* EmptyFragment emptyFragment = EmptyFragment.newInstance("A feldolgozás során hiba történt","");
            getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();*/
           LoadEmptyFragment("A feldolgozás során hiba történt");
        }
    }

    // MEZEI FÜGGVÉNYEK

    public void LoadTabbedFragment(){
        tabbedFragment = new TabbedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,tabbedFragment,"TabbedFrag").addToBackStack(null).commit();
    }

    public void LoadCikklekerdezesFragment(){
        CikklekerdezesFragment cikklekerdezesFragment = new CikklekerdezesFragment();//CikklekerdezesFragment.newInstance(barcodeData);//
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cikklekerdezesFragment,"CikkFrag").addToBackStack(null).commit();
    }

    public void LoadEmptyFragment(String value) {
        EmptyFragment emptyFragment = EmptyFragment.newInstance(value);
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
    }
    public void MenuFragment() {
        MenuFragment menuFragment = MenuFragment.newInstance(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,menuFragment,"MenuFrag").commit();
    }

    public boolean FragmentName() {
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

    private void displayScanResult(Intent initiatingIntent){
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
                PolcThread(barcodeData);
                tabbedFragment.GetID(DolgKod);
            }
        }
        else if (cikklekerdezesFragment != null)
        {
            barcodeData = decodedData;
            LoadEmptyFragment("Várom az eredményt");
            pi.clear();
            ci.clear();
            cikklekerdezesFragment.SetBinOrItem(decodedData);
            SQL(barcodeData);
            cikklekerdezesFragment.onDestroy();
        }
    }

    // THREAD FÜGGVÉNYEK

    public void SQL(String code)
    {
        SqlRunnable sqlRunnable = new SqlRunnable(code);
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
    public void PolcThread(String code)
    {
        CheckPolc checkPolc2 = new CheckPolc(code);
        new Thread(checkPolc2).start();
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
        new Thread(polcClear).start();
    }
    public void SendList(String a,String b,String c,String d,String e,String biz)
    {
        ListCucc listCucc = new ListCucc(a,b,c,d,e,biz);
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
    public void CloseOccupied()
    {
        new Thread(setLocked).start();
    }
    public void WriteItem(String item)
    {
        OnlyItemClass onlyItemClass = new OnlyItemClass(item);
        new Thread(onlyItemClass).start();
    }
    public void SetItem(String code)
    {
        SetItem setItem = new SetItem(code);
        new Thread(setItem).start();
    }
    public void SetCikkFocus()
    {
        new Thread(offFocus).start();
    }
    public  void FocusOn()
    {
        new Thread(focusOn).start();
    }
    public void SetMennyFocusOff()
    {
        new Thread(focusOff).start();
    }
    public void ReadNewItems()
    {
        new Thread(readItems).start();
    }
}