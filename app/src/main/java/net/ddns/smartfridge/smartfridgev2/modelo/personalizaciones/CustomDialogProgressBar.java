package net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import net.ddns.smartfridge.smartfridgev2.R;

/**
 * Created by Alberto on 14/01/2018.
 * <p>
 * Clase creada con el fin de invocar un dialago que contenga el progressDialog indeterminado
 */
public class CustomDialogProgressBar {
    private Dialog dialog;

    /**
     * Instantiates a new Custom dialog progress bar.
     *
     * @param activity the activity
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
     * Finaliza el diálogo, lo mata
     */
    public void endDialog(){
        dialog.dismiss();
    }
}
