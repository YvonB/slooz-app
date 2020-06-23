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
 * Created by yvon on 6/23/20.
 */
public class ChildFragment5 extends Fragment {

    public final static String EXTRA_DATA_10GO = "*340*109000#"; // Bip Mensuel
    public final static String FIRST_PREMIUM = "#322*92#"; // Telma Mensuel
    public final static String DEPANNAGE = "**436*2*4*SON NUMÉRO#"; // Airtel Mensuel
    public final static String BE_CONNECT_10GO = "http://123.orange.mg/"; // Orange Mensuel
    public final static String AUTRES = "https://www.algo-fy.com"; // Autre

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.view_sixth_slide, container, false);

        Button buttonInFragment5 = rootView.findViewById(R.id.view_sixth_slide_a_btn);
        buttonInFragment5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(EXTRA_DATA_10GO)));
                startActivity(intent);
            }
        });

        Button button2InFragment5 = rootView.findViewById(R.id.view_sixth_slide_b_btn);
        button2InFragment5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(FIRST_PREMIUM)));
                startActivity(intent);
            }
        });

        Button button3InFragment5 = rootView.findViewById(R.id.view_sixth_slide_c_btn);
        button3InFragment5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(DEPANNAGE)));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button3InFragment5.setTooltipText("Besoin d’un dépannage ? \n" +
                    "Sollicite\n" +
                    "GRATUITEMENT ton proche grâce à Airtel Money\n");
        }else{
            button3InFragment5.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TooltipCompat.setTooltipText(v, "Besoin d’un dépannage ?\n" +
                            "Sollicite\n" +
                            "GRATUITEMENT ton proche grâce à Airtel Money\n");

                    return false;
                }
            });

        }

        Button button4InFragment5 = rootView.findViewById(R.id.view_sixth_slide_d_btn);
        button4InFragment5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:"+Uri.encode(BE_CONNECT_10GO)));
                startActivity(intent);
            }
        });

        Button button5InFragment5 = rootView.findViewById(R.id.view_sixth_slide_e_btn);
        button5InFragment5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:"+Uri.encode(AUTRES)));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button5InFragment5.setTooltipText("Pompier \n" +
                    "Police  \n" +
                    "Jirama\n" +
                    "Pharmacie de garde");
        }else{
            button5InFragment5.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TooltipCompat.setTooltipText(v, "Pompier \n" +
                            "Police \n" +
                            "Jirama\n" +
                            "Pharmacie de garde");

                    return false;
                }
            });

        }

        return rootView;
    }
}
