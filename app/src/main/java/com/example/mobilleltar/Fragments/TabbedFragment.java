package com.example.mobilleltar.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilleltar.Adapters.ViewPagerAdapter;
import com.example.mobilleltar.Activities.MainActivity;
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
    private TextView desc1;
    private TextView desc2;
    private EditText cikkszamHeader;
    private EditText megjegyzes;
    private TextView bizszam;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Fragment page;
    private MainActivity mainActivity;

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

        mainActivity = (MainActivity)getActivity();
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);

        FragmentManager manager = getChildFragmentManager();
        tabLayout = (TabLayout)view.findViewById(R.id.tablayout);
        tabLayout.bringToFront();
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(manager);
        adapter.AddFragment(new LeltarozasFragment(),"Leltározás");
        adapter.AddFragment(mainActivity.mainFragment,"Felvitt tételek");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setFocusable(false);
        viewPager.setFocusable(false);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
                if (viewPager.getCurrentItem() == 1 && page != null) {
                    mainActivity.mainFragment.ClearItems();
                    mainActivity.ReadNewItems();
                    Log.d("Tabbed", "onTabSelected: ");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    public void updateTabView(int a)
    {
        TabLayout.Tab tab = tabLayout.getTabAt(a);
        tab.select();
    }
    public void setDataForChange(String a,String b, String c, String d, String e)
    {
        cikkszam = (TextView)getActivity().findViewById(R.id.cikkszamText);
        cikkszamHeader = (EditText)getActivity().findViewById(R.id.cikkszamHeader);
        desc1 = (TextView)getActivity().findViewById(R.id.desc1);
        desc2 = (TextView)getActivity().findViewById(R.id.desc2);
        megjegyzes = (EditText) getActivity().findViewById(R.id.megjegyzesText);


        cikkszam.setText(a);
        cikkszamHeader.setText(b);
        desc1.setText(c);
        desc2.setText(d);
        megjegyzes.setText(e);
    }

    public void SetFocus1()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetFocus1();
        }
    }

    public void SetItem(String code)
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetItem(code);
        }
    }

    public void SetEnabledFalse()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetEnabledFalse();
        }
    }

    public void GetFragmentAtPosition(String code)
    {
       Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetBinOrItem(code);
        }
    }
    public void GetID(String code)
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetID(code);
        }
    }
    public void StartSpinning()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).StartProgress();
        }
    }
    public void StopSpinning()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).StopProgress();
        }
    }
    public void SetFocus()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetFocus();
        }
    }
    public void SetViews(String a, String b, String c)
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetViews(a,b,c);
        }
    }
    public void ClearAllViews()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).ClearAllViews();
        }
    }
    public void ClearAllViewsAndPolc()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).ClearPolc();
        }
    }
    public void PushData(String a,String b, String c, String d,String e,String biz)
    {
        mainActivity.mainFragment.AddDataToItems(a,b,c,d,e,biz);
    }
    public void UpdateTable(int pos)
    {
        mainActivity.mainFragment.UpdateList(pos);
    }
    public boolean IsMainFragment()
    {
         Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 1 && page != null) {
          return true;
        }
        return false;
    }
    public void IsUpdate(boolean update)
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).IsUpdate(update);
        }
    }
    public void EnableViews()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).EnableViews();
        }
    }
    public void SetInternalName(String internalName)
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).SetRaktar(internalName);
        }
    }
    public void SetFocusOff()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).OffFocus();
        }
    }
    public void SetCikkFocusOff()
    {
        Fragment page = getChildFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
        if (viewPager.getCurrentItem() == 0 && page != null) {
            ((LeltarozasFragment)page).OffFocusCikk();
        }
    }

}