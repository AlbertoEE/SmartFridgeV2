package net.ddns.smartfridge.smartfridgev2.modelo.utiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.Alimento_NuevoDB;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.ConfirmarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.IdentificarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.InitialActivity;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private Context contexto;//El contexto del dialog
    private static Intent intent;//Para llamar a otras Activitys
    private Alimento acod;//Para crear un objeto que sea un alimento
    //private AlertDialog.Builder builder;//El builder para crear los dialogs
    private static Activity clase;//Para el constructor necesitamos indicar el activity donde se va a ejecutar el dialog
    private static final String DIA = " día";//Cte para el mensaje del dialog
    private static final String DIAS = " días";//Cte para el mensaje del dialog
    private static AlimentoDB alimentoDB;//Para usar los métodos de la bbdd de los alimentos de Mi Nevera

    public Dialogos(Context context, Activity activity){
        this.contexto=context;
        //builder = new AlertDialog.Builder(contexto);
        this.clase = activity;
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
                .setMessage("¡Vaya! El producto no es correcto.\nPor favor, vuelve a intentar localizar tu producto.")
                //Asignamos el botón de negativo
                .setNegativeBtnText("Cancelar")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#1CACCC")
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
                .setPositiveBtnBackground("#1CACCC")
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
    public void dialogCaducidad(int udsSeleccionadas, int caducidad, final Alimento alimento, final boolean manual, final String cod_barras){
        String day;//Para poner el mensaje del dialog
        Log.d("manual", "manual: " + manual);
        if (caducidad==1){
            day = DIA;
        } else {
            day = DIAS;
        }

        new FancyGifDialog.Builder(clase)
                .setTitle("CONFIRMAR CADUCIDAD Y CANTIDAD")
                .setMessage("El alimento caducará en: " + caducidad + day + ".\nLas unidades seleccionadas son: " + udsSeleccionadas + ", ¿Es correcto?")
                .setNegativeBtnText("Cancelar")
                .setPositiveBtnBackground("#1CACCC")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif3)
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Metodo para almacenar los datos en la nevera local
                        AlimentoDB adb = new AlimentoDB(contexto);
                        Alimento_NuevoDB andb = new Alimento_NuevoDB(contexto);
                        adb.guardarAlimento(alimento);
                        andb.guardarAlimento(alimento);
                        Toast.makeText(contexto, "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                        adb.cerrarConexion();
                        if (manual){
                            Log.d("manual", "manual: " + manual);
                            dialogNotificarSF(alimento, cod_barras);
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
                .setPositiveBtnBackground("#1CACCC")
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
    public void dialogNotificarSF(final Alimento alimento, final String cod_barras){
        Log.d("cod", "codigo 11: " + cod_barras);
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("¿Desea informar a Smart Fridge sobre el nuevo producto?");
        //Título
        builder.setTitle("¡¡¡Elemento Nuevo!!!");
        //Añadimos los botones
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Enviamos un mensaje a SF. Creamos un Intent implícito
                Intent compartir = new Intent();
                compartir.setAction(Intent.ACTION_SEND);
                compartir.putExtra(Intent.EXTRA_EMAIL, new String[]{"raquel.menciac@gmail.com", "albertolopezdam@gmail.com"});
                Log.d("cod", "codigo 12: " + cod_barras);
                compartir.putExtra(Intent.EXTRA_SUBJECT, "Alimento no reconocido: " + cod_barras);
                Log.d("cod", "codigo 12: " + cod_barras);
                compartir.putExtra(Intent.EXTRA_TEXT, "Hola,\nEl siguiente código de barras no ha sido reconido por el Smart Fridge: " + cod_barras +
                ".\nEl nombre del producto es: " + alimento.getNombreAlimento() + "\nGracias.");
                compartir.setType("*/*");
                contexto.startActivity(Intent.createChooser(compartir, "Enviar Email a Smart Fridge"));
                Toast.makeText(contexto, "Mensaje enviado, gracias por su colaboración.", Toast.LENGTH_SHORT).show();
                Log.d("sf", "Mensaje a SF");
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

    //Dialog para cuando no se encuentra el producto escaneado en la bbdd de códigos de barras
    public void dialogNoCodBarras(){
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("¡Vaya, que pena!")
                //Ponemos el mensaje
                .setMessage("No hemos encontrado tu producto en nuestra base de datos.\nPor favor, inténtalo a través de una fotografía o introduce los datos manualmente.")
                //Asignamos el botón de negativo
                .setNegativeBtnText("Cancelar")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#1CACCC")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.desepsio)
                .isCancellable(true)
                //Añadimos los listener
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                       //No codificamos
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

    //Dialog para cuando se van a eliminar todas las uds de un alimento
    public void dialogCeroUnidades(final View vista, final int id, final Context contexto, final Bitmap foto, final String nombre){
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("¡Cuidado!")
                //Ponemos el mensaje
                .setMessage("¿Estás segur@ de que quieres eliminar este producto?")
                //Asignamos el botón de negativo
                .setNegativeBtnText("No, me he equivocado")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#1CACCC")
                .setPositiveBtnText("Dale, sin miedo")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.gif5)
                .isCancellable(true)
                //Añadimos los listener
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Mostramos el SnackBar
                        mostrarSnack(vista, id, contexto, foto, nombre);
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
    //SnackBar para deshacer la eliminación del elimento
    public static void mostrarSnack(View vista, final int id, final Context contexto, final Bitmap foto, final String nombre){
        //Creamos el SnackBar con el texto que indiquemos
        Snackbar sb = Snackbar.make(vista, "Eliminando alimento", Snackbar.LENGTH_SHORT);
        //La opción que va a tener es la de deshacer. Programamos el listener
        sb.setAction("Deshacer", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //No hacemos nada
            }
        });
        sb.show();
        //Programamos el método callback para cuando desaparezca
        sb.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                switch (event) {
                    //Si pulsamos el botón de deshacer
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        Toast.makeText(contexto, "Se ha cancelado la eliminación del alimento", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        try {
                            //Si no se pulsa deshacer, eliminamos el alimento y refrescamos la lista
                            alimentoDB = new AlimentoDB(contexto);
                            alimentoDB.borrarAlimento(id);
                            Toast.makeText(contexto, "Elemento eliminado", Toast.LENGTH_SHORT).show();
                            //Aquí tienes que actualizar el recyclerview
                         /*   cursor = alimentoDB.getBocatas();
                            ba.setCursor(cursor);
                            ba.notifyItemRemoved(position);*/
                            dialogAnadirLista(contexto, foto, nombre);
                            clase.finish();
                            break;
                        }catch (SQLException e){
                            Toast.makeText(contexto, "Error al eliminar el elemento. Por favor, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });
    }

    //Dialog para cuando se van a eliminar todas las uds de un alimento
    public static void dialogAnadirLista(final Context contexto, Bitmap foto, String nombre){
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("Agregar a MiLista")
                //Ponemos el mensaje
                .setMessage("¿Quieres aprovechar y agregar este producto a MiLista?")
                //Asignamos el botón de negativo
                .setNegativeBtnText("No, gracias")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#1CACCC")
                .setPositiveBtnText("Sí, por favor")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.gif6)
                .isCancellable(true)
                //Añadimos los listener
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Llamamos al método que agrega el elemento en la lista de la compra
                        //agregarMiLista(foto, nombre);
                        Toast.makeText(contexto, "Se ha agregado el producto a MiLista", Toast.LENGTH_SHORT).show();
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
