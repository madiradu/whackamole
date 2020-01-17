package com.mnm.wackamole;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.GridLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;

import static android.graphics.Bitmap.createScaledBitmap;

public class LevelDialog extends DialogFragment{
    final Context context = this.getActivity();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.cdt.cancel();
        Builder builder = new Builder(getActivity());

        final Context context = this.getActivity();
        GridLayout la = new GridLayout(context);
        la.setColumnCount(1);la.setRowCount(2);
        LinearLayout lb = new LinearLayout(context);

        FrameLayout ll = new FrameLayout(context);

        InputStream fis = null;
        try {
            fis = this.getActivity().getAssets().open("mole"+new Integer((int) (System.currentTimeMillis()%15)).toString()+".jpg", AssetManager.ACCESS_STREAMING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bt = BitmapFactory.decodeStream(fis);
        Bitmap bMapScaled = createScaledBitmap(
                bt, 200, 300, true);
        ll.setBackground(new BitmapDrawable(bMapScaled));
        FrameLayout.LayoutParams layoutParam = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        @SuppressLint({"ResourceType", "NewApi"}) TextView tv = (TextView) new TextView(context);

        tv.setTextAppearance(context, R.style.FntBW3);
        tv.setText("You scored " + ((int)((double)MainActivity.score/(double)MainActivity.tries*10000))/100+"%.");

        tv.setMinimumWidth(MainActivity.displayMetrics.widthPixels);
        tv.setMinWidth(MainActivity.displayMetrics.widthPixels);
        tv.setPadding(5, 5, 0, 0);
        la.addView(ll, layoutParam);
        lb.addView(tv);
        la.addView(lb);
        builder.setView(la);

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.N = 0;

                MainActivity.score=0;
                Intent intent = new Intent(getActivity(), MainActivity.class);
        
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                (getActivity()).startActivity(intent);
            }
        });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        ;}
    @Override
    public void onCancel(DialogInterface dialog) {;}

}
