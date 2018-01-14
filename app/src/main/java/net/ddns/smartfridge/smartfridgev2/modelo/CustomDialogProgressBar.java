package net.ddns.smartfridge.smartfridgev2.modelo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingPlane;

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
        dialog.setContentView(R.layout.dialog_progressbar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void endDialog(){
        dialog.dismiss();
    }
}
