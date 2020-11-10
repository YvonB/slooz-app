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
public class ChildFragment3 extends Fragment {

    public final static String MLAY_200 = "*100*200#";
    public final static String MLAY_500 = "*100*500#";
    public final static String MLAY_1000 = "*100*1000#";
    public final static String BOOST = "*100*100#";
    public final static String SMS_MLAY = "*100*2*4#";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.view_third_slide, container, false);

        Button button1InFragment3 = rootView.findViewById(R.id.view_third_slide_a_btn);
        button1InFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(MLAY_200)));
                startActivity(intent);
            }
        });

        Button button2InFragment3 = rootView.findViewById(R.id.view_third_slide_b_btn);
        button2InFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(MLAY_500)));
                startActivity(intent);
            }
        });

        Button button3InFragment3 = rootView.findViewById(R.id.view_third_slide_c_btn);
        button3InFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(MLAY_1000)));
                startActivity(intent);
            }
        });

        Button button4InFragment3 = rootView.findViewById(R.id.view_third_slide_d_btn);
        button4InFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(BOOST)));
                startActivity(intent);
            }
        });

        Button button5InFragment3 = rootView.findViewById(R.id.view_third_slide_e_btn);
        button5InFragment3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(SMS_MLAY)));
                startActivity(intent);
            }
        });


        return rootView;
    }
}

