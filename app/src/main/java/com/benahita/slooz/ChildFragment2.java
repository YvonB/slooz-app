package com.benahita.slooz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by yvon on 5/31/20.
 */
public class ChildFragment2 extends Fragment {

    public final static String TSIK_JIABY = "*301*1#";
    public final static String TSIK_RAY = "*320*1000#";
    public final static String TSIK_ROA = "*320*2000#";
    public final static String TSIK_DIMY = "*320*5000#";
    public final static String TSIK_MAX = "*320*20000#";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.view_second_slide, container, false);

        Button button1InFragment2 = rootView.findViewById(R.id.view_second_slide_a_btn);
        button1InFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(TSIK_JIABY)));
                startActivity(intent);
            }
        });

        Button button2InFragment2 = rootView.findViewById(R.id.view_second_slide_b_btn);
        button2InFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(TSIK_RAY)));
                startActivity(intent);
            }
        });

        Button button3InFragment2 = rootView.findViewById(R.id.view_second_slide_c_btn);
        button3InFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(TSIK_ROA)));
                startActivity(intent);
            }
        });

        Button button4InFragment2 = rootView.findViewById(R.id.view_second_slide_d_btn);
        button4InFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(TSIK_DIMY)));
                startActivity(intent);
            }
        });

        Button button5InFragment2 = rootView.findViewById(R.id.view_second_slide_e_btn);
        button5InFragment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(TSIK_MAX)));
                startActivity(intent);
            }
        });


        return rootView;
    }
}
