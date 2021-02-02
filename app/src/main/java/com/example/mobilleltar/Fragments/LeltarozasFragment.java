package com.example.mobilleltar.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilleltar.MainActivity;
import com.example.mobilleltar.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeltarozasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeltarozasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button rakhelyBtn;
    private Button kilepesBtn;
    private TextView rakhelyTxt;
    private TextView cikkszamTxt;
    private String ID;
    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private EditText mennyisegTxt;
    private TextView unitTxt;
    private TextView desc1Txt;
    private TextView desc2Txt;
    private TextView megjegyzesTxt;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LeltarozasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeltarozasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeltarozasFragment newInstance(String param1, String param2) {
        LeltarozasFragment fragment = new LeltarozasFragment();
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
        View view = inflater.inflate(R.layout.fragment_leltarozas, container, false);
        mainActivity = (MainActivity)getActivity();
        rakhelyTxt = (TextView)view.findViewById(R.id.rakhelyText);
        rakhelyTxt.setText("");
        unitTxt = (TextView)view.findViewById(R.id.unitLeltar);
        desc1Txt = (TextView)view.findViewById(R.id.desc1);
        desc2Txt = (TextView)view.findViewById(R.id.desc2);
        cikkszamTxt = (TextView)view.findViewById(R.id.cikkszamText);
        rakhelyBtn = (Button)view.findViewById(R.id.rakhelyButton);
        kilepesBtn = (Button)view.findViewById(R.id.kilepButton);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);
        mennyisegTxt = (EditText)view.findViewById(R.id.mennyisegText1);
        megjegyzesTxt = (TextView)view.findViewById(R.id.megjegyzesText);
        megjegyzesTxt.setEnabled(false);
        mennyisegTxt.setEnabled(false);
        mennyisegTxt.setFocusable(true);
        progressBar.setVisibility(View.GONE);


        kilepesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    mainActivity.LoadMenuFragment();
            }
        });

        rakhelyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Figyelem")
                        .setMessage("Lezárod?")
                        .setNegativeButton("Igen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),"Ide tenni a lezárós részt",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("Nem", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),"Ide tenni a nem lezárós részt",Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        megjegyzesTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LELTAR", "onClick: MEGNYOMTAM");
                megjegyzesTxt.setEnabled(false);
                mainActivity.ClearViews();
            }
        });

        mennyisegTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    megjegyzesTxt.setEnabled(true);
                    megjegyzesTxt.requestFocus();
            }
        });

        return view;
    }
    public void SetBinOrItem(String code)
    {

        if(rakhelyTxt.getText()=="")
        {
            rakhelyTxt.setText(code);
        }
        else if(rakhelyTxt.getText()=="Nem polc" || rakhelyTxt.getText()=="Nincs hálózat" || rakhelyTxt.getText()=="Nincs a rendszerben")
        {
            rakhelyTxt.setText(code);
        }
        else if(rakhelyTxt.getText()!="")
        {
            if(rakhelyTxt.getText() == code)
            {
                cikkszamTxt.setText("Ez polc, cikket vegyél fel");
            }
            cikkszamTxt.setText(code);
        }
    }
    public void SetID(String code)
    {
        ID = code;
    }

    public void StartProgress()
    {
        progressBar.setVisibility(View.VISIBLE);
    }
    public void StopProgress()
    {
        progressBar.setVisibility(View.GONE);
    }
    public void SetFocus()
    {
        mennyisegTxt.setEnabled(true);
        mennyisegTxt.requestFocus();
    }
    public void SetViews(String desc1, String desc2, String unit)
    {
        desc1Txt.setText(desc1);
        desc2Txt.setText(desc2);
        unitTxt.setText(unit);
    }
    public void ClearAllViews()
    {
        cikkszamTxt.setText("");
        desc1Txt.setText("");
        desc2Txt.setText("");
        unitTxt.setText("");
        mennyisegTxt.setText("");
        megjegyzesTxt.setText("");

    }
}