package net.ddns.smartfridge.smartfridgev2.modelo;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Created by Alberto on 14/01/2018.
 */

public class CustomDialogProgressBar {
    private Dialog dialog;

    public CustomDialogProgressBar(Activity activity){
        dialog = new Dialog(activity);
    }
    public void showDialog(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progressbar_wave);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void showDialogCuadrado(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progressbar_wave);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void endDialog(){
        dialog.dismiss();
    }
}
