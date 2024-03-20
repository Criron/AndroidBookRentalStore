package com.example.bookflix;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPagerAdapter  extends FragmentStateAdapter {


    public MyPagerAdapter(FragmentActivity fa) {
        super(fa);
    }


    @Override
    public Fragment createFragment(int pos) {//analoga me to pos (tab) epilegxo to fragment pou 8a emfaniso
        switch (pos) {
            case 0: {
                return FirstFragment.newInstance();
            }
            case 1: {

                return SecondFragment.newInstance("fragment 2");
            }
            case 2: {
                return ThirdFragment.newInstance();
            }
            case 3: {
                return FourthFragment.newInstance();
            }
            default:
                return FirstFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}


