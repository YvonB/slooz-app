package com.benahita.slooz;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by yvon on 6/1/20.
 */
public class ChildFragment4 extends Fragment {

    public final static String MORA_500 = "#322*20#";
    public final static String MORA_1000 = "#322*21#";
    public final static String YELLOWW_100 = "#322*67#";
    public final static String NET_ONE_WEEK = "#322*70#";
    public final static String NET_ONE_MONTH = "#322*7#";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.view_forth_slide, container, false);

        Button button1InFragment4 = rootView.findViewById(R.id.view_forth_slide_a_btn);
        button1InFragment4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(MORA_500)));
                startActivity(intent);
            }
        });

        Button button2InFragment4 = rootView.findViewById(R.id.view_forth_slide_b_btn);
        button2InFragment4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(MORA_1000)));
                startActivity(intent);
            }
        });

        Button button3InFragment4 = rootView.findViewById(R.id.view_forth_slide_c_btn);
        button3InFragment4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(YELLOWW_100)));
                startActivity(intent);
            }
        });

        Button button4InFragment4 = rootView.findViewById(R.id.view_forth_slide_d_btn);
        button4InFragment4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(NET_ONE_WEEK)));
                startActivity(intent);
            }
        });

        Button button5InFragment4 = rootView.findViewById(R.id.view_forth_slide_e_btn);
        button5InFragment4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(NET_ONE_MONTH)));
                startActivity(intent);
            }
        });


        return rootView;
    }
}


