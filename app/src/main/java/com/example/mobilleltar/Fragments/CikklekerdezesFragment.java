package com.example.mobilleltar.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
    private EditText editText;
    private String sql ="";
    private MainActivity mainActivity;
    private SetItemOrBinManually setItemOrBinManually;

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

    public interface SetItemOrBinManually
    {
        void setValue (String value);
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
        editText = (EditText)view.findViewById(R.id.binOrItemText);
        editText.setSelection(editText.getText().length());
        editText.requestFocus();
        editText.setOnClickListener(v -> {
            //editText.setSelection(editText.getText().length());
           // editText.requestFocus();
            setItemOrBinManually.setValue(String.valueOf(editText.getText()).trim());
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
        });
       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);
        //lekerdezesTxt = (TextView)view.findViewById(R.id.lekerdezesText);
       // mainActivity = (MainActivity)getActivity();
       // mainActivity.LoadPolcResults();

        return view;
    }
    public void SetBinOrItem(String code)
    {
        editText.setText(code);
        editText.setSelection(editText.getText().length());
        editText.requestFocus();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SetItemOrBinManually)
        {
            setItemOrBinManually = (SetItemOrBinManually) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + "must implement");
        }
    }
}