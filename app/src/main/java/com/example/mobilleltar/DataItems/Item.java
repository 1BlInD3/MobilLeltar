package com.example.mobilleltar.DataItems;

public class Item
{
    private String mMertErtek;
    private String mDatum;
    private String mRajzszam;
    private String mValami;
    private int mCount;

    public Item(String ertek, String datum, String rajsszam,String valami,int count)
    {
        mMertErtek = ertek;
        mDatum = datum;
        mRajzszam = rajsszam;
        mValami = valami;
        mCount = count;
    }

    public String getmMertErtek() {
        return mMertErtek;
    }

    public String getmDatum() {
        return mDatum;
    }

    public String getmRajzszam() {
        return mRajzszam;
    }

    public String getmValami() {
        return mValami;
    }

    public int getmCount() {
        return mCount;
    }
}
