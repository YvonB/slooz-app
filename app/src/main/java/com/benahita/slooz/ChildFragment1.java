package com.benahita.slooz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * Created by yvon on 5/31/20.
 */
public class ChildFragment1 extends Fragment {

    public final static String AKAMA_1 = "222*1";
    public final static String AKAMA_3 = "222*3";
    public final static String AKAMA_7 = "222*7";
    public final static String BE_500 = "224*1*1";
    public final static String BE_1000 = "224*1*2";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.view_first_slide, container, false);

        Button buttonInFragment1 = rootView.findViewById(R.id.view_first_slide_a_btn);
        buttonInFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(AKAMA_1)));
                startActivity(intent);
            }
        });

        Button button2InFragment1 = rootView.findViewById(R.id.view_first_slide_b_btn);
        button2InFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(AKAMA_3)));
                startActivity(intent);
            }
        });

        Button button3InFragment1 = rootView.findViewById(R.id.view_first_slide_c_btn);
        button3InFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(AKAMA_7)));
                startActivity(intent);
            }
        });

        Button button4InFragment1 = rootView.findViewById(R.id.view_first_slide_d_btn);
        button4InFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                if(isPermissionGranted()){
                    intent.setData(Uri.parse("tel:"+Uri.encode(BE_500)));
                    startActivity(intent);
                }
            }
        });

        Button button5InFragment1 = rootView.findViewById(R.id.view_first_slide_e_btn);
        button5InFragment1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(BE_1000)));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public boolean isPermissionGranted(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }else{
            return true;
        }
    }
}
