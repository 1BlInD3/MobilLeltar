package com.example.mobilleltar.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.mobilleltar.DataItems.CikkItems;
import com.example.mobilleltar.DataItems.PolcItems;
import com.example.mobilleltar.Fragments.CikkResultFragment;
import com.example.mobilleltar.Fragments.CikklekerdezesFragment;
import com.example.mobilleltar.Fragments.EmptyFragment;
import com.example.mobilleltar.Fragments.PolcResultFragment;
import com.example.mobilleltar.R;
import com.example.mobilleltar.Utils.CaptureAct;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CikklekerdezesFragment.SetItemOrBinManually {


    private static final String TAG = "MainActivity";
   // private TabbedFragment tabbedFragment;
  //  public MainFragment mainFragment = new MainFragment();
    
  //  private MenuFragment menuFragment;
  //  private LoginFragment loginFragment;

  //  private BarcodeReader barcodeReader;
 //   private AidcManager manager;

    private String barcodeData;
  //  private String decodedData;

    private String URL = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=10";
    private String connectionString = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=leltar;user=Raktarrendszer;password=PaNNoN0132;loginTimeout=10";
    private Connection connection;

    private String sql="";
    private ArrayList<PolcItems> pi = new ArrayList<>();

    private ArrayList<CikkItems> ci = new ArrayList<>();
    private Handler handler = new Handler();
    private  CikklekerdezesFragment cikklekerdezesFragment;
    private EmptyFragment emptyFragment = new EmptyFragment();
    // public String DolgKod;
 //   private boolean hasRight;

  //  public boolean isPolc = false;
  //  private String mdesc1,mdesc2,munit;
  //  public String mRakt;
 //   public boolean isEmpty = false;
 //   public boolean isContains = false;
    private String polc ="";
 //   public String megjegyzes;
 //   private int position;
 //   private boolean onResume = false;
 //   private boolean isClosed = false;
 //   private String mBiz = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        LoadCikklekerdezesFragment();

    }

    //OVERRIDE METHODOK

    //SYSTEM OVERRIDE

    @Override
    public void onBackPressed() {
        //
        super.onBackPressed();
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
        Log.d(TAG, "onResume: ");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void setValue(String value) {
        LoadEmptyFragment();
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
   //SQL KEZELÉS


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
                        EmptyFragment emptyFragment = EmptyFragment.newInstance("Nincs találat","");
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
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
                        EmptyFragment emptyFragment = EmptyFragment.newInstance("A polc üres","");
                        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
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
                connection.close();
            }
            catch (Exception e)
            {
                EmptyFragment emptyFragment = EmptyFragment.newInstance(String.valueOf(e),"");
                getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
            }
        }
        else
        {
            EmptyFragment emptyFragment = EmptyFragment.newInstance("A feldolgozás során hiba történt","");
            getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
        }
    }

    // MEZEI FÜGGVÉNYEK


    public void LoadCikklekerdezesFragment(){
        cikklekerdezesFragment = new CikklekerdezesFragment();//CikklekerdezesFragment.newInstance(barcodeData);//
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,cikklekerdezesFragment,"CikkFrag").addToBackStack(null).commit();
    }

    public void LoadEmptyFragment() {
        EmptyFragment emptyFragment = new EmptyFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.cikk_container,emptyFragment).commit();
    }
    public void RemoveFragment()
    {
        if(emptyFragment.isVisible())
        getSupportFragmentManager().beginTransaction().remove(emptyFragment).commit();
    }

    // THREAD FÜGGVÉNYEK
    public void SQL(String code)
    {
        SqlRunnable sqlRunnable = new SqlRunnable(code);
        new Thread(sqlRunnable).start();
    }
    //Kamera kezelés

    public void ScanCode()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Beolvasás folyamatban...");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null)
        {
           if(result.getContents() != null)
           {
               SQL(result.getContents());
               cikklekerdezesFragment.SetBinOrItem(result.getContents());
               cikklekerdezesFragment.SetBinOrItem(result.getContents());
           }
           else
           {
               Log.d(TAG, "onActivityResult: no result");
           }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}