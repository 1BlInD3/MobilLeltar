package com.example.mobilleltar.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
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

    private EditText editText;
    private SetItemOrBinManually setItemOrBinManually;

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
        //args.putString(ARG_PARAM1, param1);
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
            //mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cikklekerdezes, container, false);
        editText = (EditText)view.findViewById(R.id.binOrItemText);
        editText.setSelection(editText.getText().length());
        editText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        editText.requestFocus();
        editText.setOnClickListener(v -> {
            setItemOrBinManually.setValue(String.valueOf(editText.getText()).trim());
            editText.setSelection(editText.getText().length());
            editText.requestFocus();
        });

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