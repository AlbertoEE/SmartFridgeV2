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
import net.ddns.smartfridge.smartfridgev2.persistencia.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.vista.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.ConfirmarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.IdentificarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.InitialActivity;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private static Context contexto;//El contexto del dialog
    private static Intent intent;//Para llamar a otras Activitys
    private Alimento acod;//Para crear un objeto que sea un alimento
    //private AlertDialog.Builder builder;//El builder para crear los dialogs
    private static Activity clase;//Para el constructor necesitamos indicar el activity donde se va a ejecutar el dialog
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
                        intent.putExtra("ClasePadre", "ConfirmarAlmientoActivity");
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
    public void dialogCaducidad(int udsSeleccionadas, int caducidad, final Alimento alimento, final boolean manual){
        String day=null;//Para poner el mensaje del dialog

        if (caducidad==1){
            day = DIA;
        } else {
            day = DIAS;
        }

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
                        AlimentoDB adb = new AlimentoDB(contexto);
                        adb.guardarAlimento(alimento);
                        Toast.makeText(contexto, "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                        adb.cerrarConexion();
                        if (manual){
                            dialogNotificarSF(alimento);
                        } else {
                            intent = new Intent(contexto, InitialActivity.class);
                            contexto.startActivity(intent);
                            CaducidadAlimento ia = (CaducidadAlimento) clase;
                            //Finalizamos el activity
                            ia.finish();
                            ia.finishAffinity();
                        }
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
    //Dialog para cuando no se ha seleccionado una caducidad
    public void dialogNoCaducidad(){
        new FancyGifDialog.Builder(clase)
                .setTitle("¡NO SE HA SELECCIONADO LA CADUCIDAD!")
                .setMessage("Por favor, selecciona una fecha de caducidad para este alimento.")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif4)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Volvemos a la pantalla anterior
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Volvemos a la pantalla anterior
                    }
                })
                .build();
    }

    //Dialog para notificar a SF un alimento nuevo
    public static void dialogNotificarSF(Alimento alimento){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("¿Desea informar a Smart Fridge sobre el nuevo producto?");
        //Título
        builder.setTitle("¡¡¡Elemento Nuevo!!!");
        //Añadimos los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Enviamos un mensaje a SFç
                Toast.makeText(contexto, "Mensaje enviado, gracias por su colaboración.", Toast.LENGTH_SHORT).show();
                Log.d("sf", "Mensaje a SF");
                intent = new Intent(contexto, InitialActivity.class);
                contexto.startActivity(intent);
                CaducidadAlimento ia = (CaducidadAlimento) clase;
                //Finalizamos el activity
                ia.finish();
                ia.finishAffinity();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CaducidadAlimento ia = (CaducidadAlimento) clase;
                //Finalizamos el activity
                ia.finish();
                ia.finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
