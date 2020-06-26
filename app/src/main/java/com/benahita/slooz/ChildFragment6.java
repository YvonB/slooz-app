package com.benahita.slooz;

import android.Manifest;
import android.content.DialogInterface;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * Created by yvon on 6/26/20.
 */
public class ChildFragment6 extends Fragment {
    public final static String STAY_IN = "222*33";
    public final static String FACEBOOK_MIX = "*114*500#";
    public final static String FACEBOOBAKA = "#322*65#";
    public final static String EXTRA_SMS = "*340*750#";
    public final static String MON_TAXI = "0347221413";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.view_seventh_slide, container, false);

        Button buttonInFragment6 = rootView.findViewById(R.id.view_seventh_slide_a_btn);
        buttonInFragment6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(STAY_IN)));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buttonInFragment6.setTooltipText("Prix : 500 Ar, 1Go, Validité : 3 jours\n" +
                    "Instagram, Facebook, Messanger\n" +
                    "Appel 30min vers 3 numéro famille.");
        }else{
            buttonInFragment6.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TooltipCompat.setTooltipText(v, "Prix : 500 Ar, 1Go, Validité : 3 jours\n" +
                            "Instagram, Facebook, Messanger\n" +
                            "Appel 30min vers 3 numéro famille.");

                    return false;
                }
            });

        }

        Button button2InFragment6 = rootView.findViewById(R.id.view_seventh_slide_b_btn);
        button2InFragment6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(FACEBOOK_MIX)));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button2InFragment6.setTooltipText("Prix :500 Ar, 150Mo, Validité:7 jours\n" +
                    "Facebook, Messanger\n" +
                    "Appel, SMS, vers Airtel ou autres.");
        }else{
            button2InFragment6.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TooltipCompat.setTooltipText(v, "Prix :500 Ar, 150Mo, Validité:7 jours\n" +
                            "Facebook, Messanger\n" +
                            "Appel, SMS, vers Airtel ou autres.");

                    return false;
                }
            });

        }

        Button button3InFragment6 = rootView.findViewById(R.id.view_seventh_slide_c_btn);
        button3InFragment6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+Uri.encode(FACEBOOBAKA)));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button3InFragment6.setTooltipText("Prix :500 Ar, 1Go, Validité:7 jours\n" +
                    "Facebook, Messanger.");
        }else{
            button3InFragment6.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TooltipCompat.setTooltipText(v, "Prix :500 Ar, 1Go, Validité:7 jours\n" +
                            "Facebook, Messanger.");

                    return false;
                }
            });

        }

        Button button4InFragment6 = rootView.findViewById(R.id.view_seventh_slide_d_btn);
        button4InFragment6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:"+Uri.encode(EXTRA_SMS)));
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            button4InFragment6.setTooltipText("Prix :750 Ar, Validité:48 heures\n" +
                    "25 SMS, Vers tous les opérateurs.");
        }else{
            button4InFragment6.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TooltipCompat.setTooltipText(v, "Prix :750 Ar, Validité:48 heures\n" +
                            "25 SMS, Vers tous les opérateurs.");

                    return false;
                }
            });

        }

        Button button5InFragment6 = rootView.findViewById(R.id.view_seventh_slide_e_btn);
        button5InFragment6.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                if(isPermissionGranted()){
                    intent.setData(Uri.parse("tel:"+Uri.encode(MON_TAXI)));
                    startActivity(intent);
                }
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
