package net.ddns.smartfridge.smartfridgev2.modelo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.ConfirmarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.ControlAlimentosActivity;
import net.ddns.smartfridge.smartfridgev2.vista.IdentificarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.InitialActivity;

import java.io.IOException;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private Context contexto;//El contexto del dialog
    private Intent intent;//Para llamar a otras Activitys
    private Alimento acod;//Para crear un objeto que sea un alimento
    //private AlertDialog.Builder builder;//El builder para crear los dialogs
    private Activity clase;//Para el constructor necesitamos indicar el activity donde se va a ejecutar el dialog
    private static final String DIA = " día";//Cte para el mensaje del dialog
    private static final String DIAS = " días";//Cte para el mensaje del dialog

    public Dialogos(Context context, Activity clase){
        this.contexto=context;
        //builder = new AlertDialog.Builder(contexto);
        this.clase = clase;
    }
    //Se mostrará el dialog cuando el alimento encontrado en la bbdd no sea el que tiene el cliente
    public void dialogAlimentoNoEncontrado(){
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("¡Vaya! El producto no es correcto.\nPor favor, vuelve a intentar localizar tu producto.");
        //Título
        builder.setTitle("Ups...");
        //Añadimos los botones
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(contexto, IdentificarAlimentoActivity.class);
                contexto.startActivity(intent);
                ConfirmarAlimentoActivity ca = (ConfirmarAlimentoActivity) clase;
                //Finalizamos el activity
                ca.finishAffinity();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();*/
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("Ups...")
                //Ponemos el mensaje
                .setMessage("¡Vaya! El producto no es correcto.\\nPor favor, vuelve a intentar localizar tu producto.")
                //Asignamos el botón de negativo
                .setNegativeBtnText("Cancelar")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.gif1)
                .isCancellable(true)
                //Añadimos los listener
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        intent = new Intent(contexto, IdentificarAlimentoActivity.class);
                        contexto.startActivity(intent);
                        ConfirmarAlimentoActivity ca = (ConfirmarAlimentoActivity) clase;
                        //Finalizamos el activity
                        ca.finishAffinity();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //No hacemos nada
                    }
                })
                .build();
    }
    //Se mostrará el dialog cuando el alimento encontrado en la bbdd sí sea el que tiene el cliente
    public void dialogAlimentoEncontrado(){
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("A cotinuación pasarás a la pantalla para añadir la caducidad del producto");
        //Título
        builder.setTitle("¡¡¡¡Bieeeeeeeeeeen, somos los mejores!!!!");
        //Añadimos los botones
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent = new Intent(contexto, CaducidadAlimento.class);
                contexto.startActivity(intent);
                ConfirmarAlimentoActivity ca = (ConfirmarAlimentoActivity) clase;
                //Finalizamos el activity
                ca.finishAffinity();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();*/
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("¡¡¡¡Bieeeeeeeeeeen, somos los mejores!!!!")
                //Ponemos el mensaje
                .setMessage("A cotinuación pasarás a la pantalla para añadir la caducidad del producto.")
                //Asignamos el botón de negativo
                .setNegativeBtnText("Cancelar")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.gif2)
                .isCancellable(true)
                //Añadimos los listener
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        intent = new Intent(contexto, CaducidadAlimento.class);
                        contexto.startActivity(intent);
                        ConfirmarAlimentoActivity ca = (ConfirmarAlimentoActivity) clase;
                        //Finalizamos el activity
                        ca.finishAffinity();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //No hacemos nada
                    }
                })
                .build();
    }

    //Se mostrará el dialog cuando haya seleccionado la caducidad y las uds para confirmar los datos
    public void dialogCaducidad(int udsSeleccionadas, int caducidad){
        String day=null;//Para poner el mensaje del dialog
        if (caducidad==1){
            day = DIA;
        } else {
            day = DIAS;
        }
        /*
        //Título
        builder.setTitle("CONFIRMAR CADUCIDAD Y CANTIDAD");
        //Mensaje del Alert
        builder.setMessage("El alimento caducará en: " + caducidad + day + ".\nLas unidades seleccionadas son: " + udsSeleccionadas + ", ¿Es correcto?");
        //Añadimos los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Metodo para almacenar los datos en la nevera local
                Toast.makeText(contexto, "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                intent = new Intent(contexto, InitialActivity.class);
                contexto.startActivity(intent);
                CaducidadAlimento ia = (CaducidadAlimento) clase;
                //Finalizamos el activity
                ia.finish();
                ia.finishAffinity();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No hacemos nada
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();*/
        new FancyGifDialog.Builder(clase)
                .setTitle("CONFIRMAR CADUCIDAD Y CANTIDAD")
                .setMessage("El alimento caducará en: " + caducidad + day + ".\nLas unidades seleccionadas son: " + udsSeleccionadas + ", ¿Es correcto?")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif3)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Metodo para almacenar los datos en la nevera local
                        Toast.makeText(contexto, "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                        intent = new Intent(contexto, InitialActivity.class);
                        contexto.startActivity(intent);
                        CaducidadAlimento ia = (CaducidadAlimento) clase;
                        //Finalizamos el activity
                        ia.finish();
                        ia.finishAffinity();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //No hacemos nada
                    }
                })
                .build();
    }
}
