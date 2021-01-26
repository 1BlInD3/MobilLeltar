package com.example.mobilleltar.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilleltar.Adapters.ViewPagerAdapter;
import com.example.mobilleltar.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabbedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabbedFragment extends Fragment{


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView cikkszam;
    private TextView megnevezes;
    private TextView megnevezes2;
    private EditText mennyiseg;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Fragment page;

    public TabbedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabbedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TabbedFragment newInstance(String param1, String param2) {
        TabbedFragment fragment = new TabbedFragment();
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

        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);
        cikkszam = (TextView)view.findViewById(R.id.cikkszamText);
        megnevezes = (TextView)view.findViewById(R.id.cikkszamNameText);
        mennyiseg = (EditText)view.findViewById(R.id.mennyisegText1);
        megnevezes2 = (TextView)view.findViewById(R.id.megjegyzesText);

      //  LeltarozasFragment leltarozasFragment = (LeltarozasFragment) getChildFragmentManager().beginTransaction().

       // cikkszam.isFocusable();
        FragmentManager manager = getChildFragmentManager();
        tabLayout = (TabLayout)view.findViewById(R.id.tablayout);
        tabLayout.bringToFront();
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.AddFragment(new LeltarozasFragment(),"Leltározás");
        adapter.AddFragment(new MainFragment(),"Felvitt tételek");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
       // Fragment page2 = getChildFragmentManager().findFragmentByTag("android:switcher:"+R.id.viewpager + ":" + 1);

     /*   viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0)
                {
                    Toast.makeText(getContext(),"ELSO",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Masodik",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {



            }
        });*/

        return view;
    }

    public void updateTabView(int a)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(a);
        tab.select();
    }
    public void setDataForChange(String a,String b, String c, String d)
    {
        cikkszam = (TextView)getActivity().findViewById(R.id.cikkszamText);
        megnevezes = (TextView)getActivity().findViewById(R.id.cikkszamNameText);
        mennyiseg = (EditText)getActivity().findViewById(R.id.mennyisegText1);
        megnevezes2 = (TextView)getActivity().findViewById(R.id.megjegyzesText);


        cikkszam.setText(a);
        megnevezes.setText(b);
        mennyiseg.setText(c);
        megnevezes2.setText(d);
        //Toast.makeText(getContext(),a+b+c+d,Toast.LENGTH_SHORT).show();
    }

  /*  public void GetFragmentAtPosition(String code)
    {
        page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + 0);
        if(page != null)
        {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    LeltarozasFragment leltarozasFragment = (LeltarozasFragment) page;
                    leltarozasFragment.SetBinOrItem(code);
                    break;
                case 1:
                    MainFragment mainFragment = (MainFragment)page;
                    break;
            }
        }
    }*/

    public void GetFragmentAtPosition(String code)
    {
       Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetBinOrItem(code);
        }
    }

}