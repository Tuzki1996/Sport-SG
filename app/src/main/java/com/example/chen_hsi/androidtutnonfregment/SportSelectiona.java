package com.example.chen_hsi.androidtutnonfregment;

/**
 * Created by GMD on 27/10/16.
 */
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.ArrayList;

public class SportSelectiona extends DialogFragment{

    @NonNull

    ArrayList<Integer> list=new ArrayList<Integer>();
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       final String[] items=Sport.SPORT_TYPE.names();
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Sport");
        builder.setMultiChoiceItems(Sport.SPORT_TYPE.names(), null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)
                {
                    list.add(which);
                }
                else if(list.contains(which)){
                    list.remove(which);
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selections="";
                for(Integer ms:list)
                {
                    selections+=ms.toString()+"  ";
                }
                Toast.makeText(getActivity(),"Select"+selections,Toast.LENGTH_SHORT).show();
            }
        });
        return builder.create();
    }
}
