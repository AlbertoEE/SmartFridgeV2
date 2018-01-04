package net.ddns.smartfridge.smartfridgev2.modelo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import net.ddns.smartfridge.smartfridgev2.vista.IdentificarAlimentoActivity;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private Context contexto;//El contexto del dialog
    public Dialogos(Context context){
        this.contexto=context;
    }

    public void dialogAlimentoNoEncontrado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("¡Vaya! El producto no es correcto.\nPor favor, vuelve a intentar localizar tu producto.");
        //Título
        builder.setTitle("Ups...");
        //Añadimos los botones
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(contexto, IdentificarAlimentoActivity.class);
                contexto.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
