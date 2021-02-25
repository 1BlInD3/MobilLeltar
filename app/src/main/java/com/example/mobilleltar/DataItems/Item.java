package com.example.mobilleltar.DataItems;

public class Item
{
    private String mCikkszam;
    private String mMegnevezes1;
    private String mMegnevezes2;
    private String mMennyiseg;
    private String mMegjegyzes;
    private String mBizsszam;

    public Item(String cikk, String megn1, String megn2,String mennyiseg,String megj,String bizszam)
    {
        mCikkszam = cikk;
        mMegnevezes1 = megn1;
        mMegnevezes2 = megn2;
        mMennyiseg = mennyiseg;
        mMegjegyzes = megj;
        mBizsszam = bizszam;
    }

    public String getmCikkszam() {
        return mCikkszam;
    }

    public String getmMegnevezes1() {
        return mMegnevezes1;
    }

    public String getmMegnevezes2() {
        return mMegnevezes2;
    }

    public String getmMennyiseg() {
        return mMennyiseg;
    }

    public String getmMegjegyzes() {
        return mMegjegyzes;
    }

    public String getmBizsszam() {
        return mBizsszam;
    }
}
