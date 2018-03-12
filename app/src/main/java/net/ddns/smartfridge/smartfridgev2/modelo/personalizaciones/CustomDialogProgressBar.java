package net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Clase que prepresenta un DialogProgressBar personalizado
 */
public class CustomDialogProgressBar {
    private Dialog dialog;

    /**
     * Constructor
     *
     * @param activity La Activity sobre la que se ejecuta
     */
    public CustomDialogProgressBar(Activity activity){
        dialog = new Dialog(activity);
    }

    /**
     * Muestra el diálogo personalizado con ondas
     */
    public void showDialogOndas(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progressbar_wave);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * Muestra el diálogo personalizado con un plano rotando
     */
    public void showDialogCuadrado(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progressbar_cuadrado);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /**
     * Finaliza el diálogo
     */
    public void endDialog(){
        dialog.dismiss();
    }
}
