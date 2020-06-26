package com.benahita.slooz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import static java.lang.System.exit;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ChildFragment1(); //ChildFragment1 at position 0
            case 1:
                return new ChildFragment2(); //ChildFragment2 at position 1
            case 2:
                return new ChildFragment3(); //ChildFragment3 at position 2
            case 3:
                return new ChildFragment4(); //ChildFragment4 at position 3
            case 4:
                return new ChildFragment5(); //ChildFragment4 at position 4
            case 5:
                return new ChildFragment6(); //ChildFragment4 at position 5
        }
        return null; //does not happen
    }

    @Override
    public int getCount() {
        return 6; //three fragments
    }
}