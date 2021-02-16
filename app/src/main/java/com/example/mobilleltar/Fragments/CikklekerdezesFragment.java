package com.example.mobilleltar.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobilleltar.Activities.MainActivity;
import com.example.mobilleltar.R;

import java.sql.Connection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CikklekerdezesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CikklekerdezesFragment extends Fragment {

    private String URL = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=10";
    private Connection connection;
    private TextView lekerdezesTxt;
    private String sql ="";
    private MainActivity mainActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
  //  private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
   // private String mParam2;

    public CikklekerdezesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CikklekerdezesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CikklekerdezesFragment newInstance(String param1) {
        CikklekerdezesFragment fragment = new CikklekerdezesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cikklekerdezes, container, false);

       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);


        lekerdezesTxt = (TextView)view.findViewById(R.id.lekerdezesText);
       // mainActivity = (MainActivity)getActivity();
       // mainActivity.LoadPolcResults();

        return view;
    }
    public void SetBinOrItem(String code)
    {
        lekerdezesTxt.setText(code);
    }

    /*
    private boolean LoadPolc(String code)
    {
        try {
            Statement statement = connection.createStatement();
            sql = String.format(getResources().getString(R.string.polcSql),code);
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next() == false)
            {
                return false;
            }
            else
            {
                return true;
            }

        } catch (Exception e){
            Toast.makeText(getContext(),"",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean LoadCikk(String code)
    {
        try {
            Statement statement = connection.createStatement();
            sql = String.format(getResources().getString(R.string.cikkSql),code);
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next() == false)
            {
                return false;
            }
            else
            {
                return true;
            }

        } catch (Exception e){
            Toast.makeText(getContext(),"Nem bírja az STD01-et",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private boolean Connected(String url)
    {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if(connection != null)
        {
           // Toast.makeText(getContext(),"10mp alatt megvolt",Toast.LENGTH_LONG).show();
            return true;
        }
        else
        {
            Toast.makeText(getContext(),"10mp alatt nem volt meg ",Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private void RunSql(String code) throws SQLException {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection(URL);
    }*/
}