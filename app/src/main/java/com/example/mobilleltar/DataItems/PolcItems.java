package com.example.mobilleltar.DataItems;

import java.io.Serializable;

public class PolcItems implements Serializable
{
    private Double mMennyiseg;
    private String mEgyseg;
    private String mMegnevezes1;
    private String mMegnevezes2;
    private String mIntRem;
    private String mAllapot;

    public PolcItems (Double mennyiseg, String mertekegyseg, String megnevezes1, String megnevezes2, String intRem, String allapot)
    {
        mMennyiseg = mennyiseg;
        mEgyseg = mertekegyseg;
        mMegnevezes1 = megnevezes1;
        mMegnevezes2 = megnevezes2;
        mIntRem = intRem;
        mAllapot = allapot;
    }

    public Double getmMennyiseg() {
        return mMennyiseg;
    }

    public String getmEgyseg() {
        return mEgyseg;
    }

    public String getmMegnevezes1() {
        return mMegnevezes1;
    }

    public String getmMegnevezes2() {
        return mMegnevezes2;
    }

    public String getmIntRem() {
        return mIntRem;
    }

    public String getmAllapot() {
        return mAllapot;
    }
}
