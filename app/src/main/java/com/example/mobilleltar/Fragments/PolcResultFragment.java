package com.example.mobilleltar.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mobilleltar.Adapters.PolcItemAdapter;
import com.example.mobilleltar.DataItems.PolcItems;
import com.example.mobilleltar.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PolcResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PolcResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private String URL = "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=2";
    private Connection connection;
    private String sql ="SELECT SC33001 as [StockItem]," +
            "                           SUM(SC33005) as [BalanceQty],SUM(SC33008) as [ReceivedQty],MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory,MAX([SC01002]) as Description1," +
            "                         MAX([SC01003]) as Description2,MAX([SC01093]) as IntRem,MAX([SC01094]) as IntRem2,MAX(rtrim(Description)) as Unit FROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] " +
            "                               left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 " +
            "                            left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode " +
            "                            LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 where SC33005 > 0 and BinNumber='%s'group by SC33001, SC33010, SC33038 order by Description2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<PolcItems> myPolcItems = new ArrayList<>();

    public PolcResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CikkResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PolcResultFragment newInstance(String param1, String param2) {
        PolcResultFragment fragment = new PolcResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cikk_result, container, false);


         StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
         StrictMode.setThreadPolicy(policy);

         if(Connected(URL))
         {
             RunSql();
         }



        recyclerView = (RecyclerView)view.findViewById(R.id.polcRecycler);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(view.getContext());
        adapter = new PolcItemAdapter(myPolcItems);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        return view;
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
            return true;
        else
            return false;
    }
    private void RunSql()
    {
        try {
            Statement statement = connection.createStatement();
            sql = String.format(sql,"STD01");
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String a = resultSet.getString("Unit");
                Log.d("Mertekegyseg",a);
                myPolcItems.add(new PolcItems(resultSet.getDouble("BalanceQty"),a,resultSet.getString("Description1"),
                        resultSet.getString("Description2"),resultSet.getString("IntRem"),resultSet.getString("QcCategory")));
            }
        } catch (Exception e){

        }
    }
}