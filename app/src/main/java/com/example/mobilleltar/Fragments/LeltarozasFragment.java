package com.example.mobilleltar.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mobilleltar.Activities.MainActivity;
import com.example.mobilleltar.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeltarozasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeltarozasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button rakhelyBtn;
    private Button kilepesBtn;
    private TextView rakhelyTxt;
    private EditText cikkszamTxt;
    private String ID;
    private MainActivity mainActivity;
    private ProgressBar progressBar;
    private EditText mennyisegTxt;
    private TextView unitTxt;
    private TextView desc1Txt;
    private TextView desc2Txt;
    private EditText megjegyzesTxt;
    private TextView internalNameTxt;
    private TabbedFragment tabbedFragment;
    private SetTableView setTableView;
    private boolean cikkClick = false;
    // TODO: Rename and change types of parameters

    private String a,b,c,d, mDesc1,mDesc2,mUnit,mMegj;
    private boolean mUpdate;

    public interface SetTableView
    {
        void setDataToSend(String a, String b, String c, String d, String e);
        void setDataToSendAndRemove();
        void isEmpty (boolean a);
        void isContains (boolean a);
        void isClosed();
    }

    public LeltarozasFragment() {
        // Required empty public constructor
    }

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
           // mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
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
        cikkszamTxt = (EditText) view.findViewById(R.id.cikkszamText);
        rakhelyBtn = (Button)view.findViewById(R.id.rakhelyButton);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2);
        mennyisegTxt = (EditText)view.findViewById(R.id.cikkszamHeader);
        megjegyzesTxt = (EditText) view.findViewById(R.id.megjegyzesText);
        internalNameTxt = (TextView)view.findViewById(R.id.internalNameText);
        megjegyzesTxt.setEnabled(false);
        mennyisegTxt.setEnabled(false);
        mennyisegTxt.setFocusable(true);
        progressBar.setVisibility(View.GONE);
        tabbedFragment = new TabbedFragment();
        cikkszamTxt.setFocusable(false);

        mennyisegTxt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(9,2)});

        rakhelyBtn.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Figyelem")
                    .setMessage("Lezárod?")
                    .setNegativeButton("Igen", (dialog, which) -> {
                       // Toast.makeText(getContext(),"Ide tenni a lezárós részt",Toast.LENGTH_SHORT).show();
                        mainActivity.CloseRakhely("2");
                        ClearPolc();
                        ClearAllViews();
                        mainActivity.mainFragment.ClearItems();
                        mainActivity.isPolc=false;
                        cikkszamTxt.setFocusable(false);
                        mennyisegTxt.setFocusable(false);
                        megjegyzesTxt.setFocusable(false);
                    })
                    .setPositiveButton("Nem", (dialog, which) -> {
                        mainActivity.CloseRakhely("1");
                        mainActivity.isPolc=false;
                        ClearPolc();
                        ClearAllViews();
                        //setTableView.isClosed();
                        mainActivity.mainFragment.ClearItems();
                    });
            builder.create();
            builder.show();
        });

        megjegyzesTxt.setOnClickListener(v -> {
            Log.d("LELTAR", "onClick: MEGNYOMTAM");
            if(mUpdate)
            {
                //ide kell az update sql
                String uMenny = String.valueOf(mennyisegTxt.getText()).trim();
                String uMegj = String.valueOf(megjegyzesTxt.getText()).trim();
                String cikk = String.valueOf(cikkszamTxt.getText()).trim();
                String desc1 = String.valueOf(desc1Txt.getText()).trim();
                String desc2 = String.valueOf(desc2Txt.getText()).trim();
                mainActivity.UpdateItems(uMenny,uMegj,cikk,mainActivity.megjegyzes);
                setTableView.setDataToSend(cikk,desc1,desc2,uMenny,uMegj);
                setTableView.setDataToSendAndRemove();
                Log.d("LELTAR", "onClick: Ez már update-ra megy");
                mainActivity.ClearViews();
                mUpdate = false;
                mennyisegTxt.setEnabled(false);
                megjegyzesTxt.setEnabled(false);
                cikkszamTxt.setEnabled(true);
                cikkszamTxt.requestFocus();
            }
            else
            {
                b = String.valueOf(megjegyzesTxt.getText());
                d = String.valueOf(rakhelyTxt.getText());
                megjegyzesTxt.setEnabled(false);
                mainActivity.ClearViews();
                setTableView.setDataToSend(a,mDesc1.trim(),mDesc2.trim(),c.trim(),b.trim()); // frissítem a listát
                if(mainActivity.isEmpty)
                {
                    mainActivity.InsertNewRow(a, c, ID, mainActivity.mRakt, d, b, "n", "1", "0"); //feltöltöm a leltaradatot
                    mainActivity.InsertRakhEll();
                    setTableView.isEmpty(false);
                    setTableView.isContains(true);
                }
                else
                {
                    mainActivity.InsertNewRow(a, c, ID, mainActivity.mRakt, d, b, "n", "1", "0"); //feltöltöm a leltaradatot
                }
                Log.d("UPDATE", "onClick: Ez már simán ment");
                mennyisegTxt.setEnabled(false);
                megjegyzesTxt.setEnabled(false);
                cikkszamTxt.setEnabled(true);
                cikkszamTxt.requestFocus();
            }

        });

        mennyisegTxt.setOnClickListener(v -> {
                megjegyzesTxt.setEnabled(true);
                megjegyzesTxt.setSelection(megjegyzesTxt.getText().length());
                megjegyzesTxt.requestFocus();
                c = String.valueOf(mennyisegTxt.getText());
        });

        cikkszamTxt.setOnClickListener(v -> {
            String item = String.valueOf(cikkszamTxt.getText()).trim();
            mainActivity.WriteItem(String.valueOf(cikkszamTxt.getText()).trim());
            cikkszamTxt.setText(item);
            cikkClick = true;
            mennyisegTxt.requestFocus();
            a = item;
        });

        return view;
    }
    public void SetBinOrItem(String code)
    {

        if(rakhelyTxt.getText()=="")
        {
            rakhelyTxt.setText(code);
            cikkszamTxt.setEnabled(true);
            cikkszamTxt.setFocusable(true);
            cikkszamTxt.requestFocus();
        }
        else if(rakhelyTxt.getText()=="Nem polc" || rakhelyTxt.getText()=="Nincs hálózat" || rakhelyTxt.getText()=="Nincs a rendszerben"||rakhelyTxt.getText()=="A polc üres" || rakhelyTxt.getText() == "A polc nem elérhető")
        {
            rakhelyTxt.setText(code);
            //cikkszamTxt.setEnabled(false);
        }
        else if(rakhelyTxt.getText()!="")
        {
                SetFocus1();
                if (rakhelyTxt.getText() == code) {
                    cikkszamTxt.setText("Ez polc, cikket vegyél fel");
                }
                cikkszamTxt.setText(code);
                a = code;
                mennyisegTxt.setEnabled(true);
                mennyisegTxt.requestFocus();

        }
    }
    public void SetItem(String code)
    {
        cikkszamTxt.setText(code);
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
        mDesc1 = desc1;
        mDesc2 = desc2;
        mUnit = unit;

        desc1Txt.setText(mDesc1);
        desc2Txt.setText(mDesc2);
        unitTxt.setText(mUnit);
    }
    public void ClearAllViews()
    {
        cikkszamTxt.setText("");
        desc1Txt.setText("");
        desc2Txt.setText("");
        unitTxt.setText("");
        mennyisegTxt.setText("");
        megjegyzesTxt.setText("");
        cikkszamTxt.setEnabled(true);
        cikkszamTxt.requestFocus();

    }
    public void ClearPolc()
    {
        rakhelyTxt.setText("");
        cikkszamTxt.setText("");
        desc1Txt.setText("");
        desc2Txt.setText("");
        unitTxt.setText("");
        mennyisegTxt.setText("");
        megjegyzesTxt.setText("");
        internalNameTxt.setText("");
        mennyisegTxt.setEnabled(false);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SetTableView)
        {
            setTableView = (SetTableView) context;
        }
        else
            {
                throw new RuntimeException(context.toString() + "must implement tabchange");
            }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setTableView = null;
    }
    public void IsUpdate(boolean update)
    {
        mUpdate = update;
    }
    public void EnableViews()
    {
        mennyisegTxt.setEnabled(true);
        //mennyisegTxt.requestFocus();
        mennyisegTxt.setSelection(mennyisegTxt.getText().length());
        mennyisegTxt.requestFocus();
    }
    public void SetRaktar(String raktar)
    {
        internalNameTxt.setText(raktar);
    }
    public void SetEnabledFalse()
    {
        mennyisegTxt.setEnabled(false);
    }
    public void SetFocus1()
    {
        cikkszamTxt.setFocusable(true);
        //cikkszamTxt.setEnabled(true);
        cikkszamTxt.requestFocus();
    }
    public class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

        public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
            mPattern=Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            Matcher matcher=mPattern.matcher(dest);
            if(!matcher.matches())
                return "";
            return null;
        }

    }
    public void OffFocus()
    {
        cikkszamTxt.setEnabled(false);
    }
}
