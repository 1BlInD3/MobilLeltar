package com.example.mobilleltar.DataItems;

public class CikkItems
{
    private Double mMennyiseg;
    private String mPolc;
    private String mRaktar;
    private String mAllapot;

    public CikkItems (Double mennyiseg, String polc, String raktar, String allapot)
    {
        mMennyiseg = mennyiseg;
        mPolc = polc;
        mRaktar = raktar;
        mAllapot = allapot;
    }

    public String getmAllapot() {
        return mAllapot;
    }

    public Double getmMennyiseg() {
        return mMennyiseg;
    }

    public String getmPolc() {
        return mPolc;
    }

    public String getmRaktar() {
        return mRaktar;
    }
}
