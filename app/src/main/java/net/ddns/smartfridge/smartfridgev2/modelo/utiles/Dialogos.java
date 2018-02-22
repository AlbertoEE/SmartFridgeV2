package net.ddns.smartfridge.smartfridgev2.modelo.utiles;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomArrayAdapterNuevaLista;
import net.ddns.smartfridge.smartfridgev2.modelo.adaptadores.CustomPageAdapter;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Alimento_Nuevo;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.ComponenteListaCompra;
import net.ddns.smartfridge.smartfridgev2.modelo.basico.Tipo;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.AlimentoDB;
import net.ddns.smartfridge.smartfridgev2.persistencia.gestores.Alimento_NuevoDB;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.DialogActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.CaducidadAlimento;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.ConfirmarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.DetallesActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.IdentificarAlimentoActivity;
import net.ddns.smartfridge.smartfridgev2.vista.actividades.InitialActivity;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.TabTipo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Clase para mostrar los diferentes tipos de dialogs en la app
 */

public class Dialogos {
    private Context contexto;//El contexto del dialog
    private static Intent intent;//Para llamar a otras Activitys
    private Alimento acod;//Para crear un objeto que sea un alimento
    //private AlertDialog.Builder builder;//El builder para crear los dialogs
    private static Activity clase;//Para el constructor necesitamos indicar el activity donde se va a ejecutar el dialog
    private static FragmentActivity fragment;//Para el constructor necesitamos indicar el fragment donde se va a ejecutar el dialog
    private static final String DIA = " día";//Cte para el mensaje del dialog
    private static final String DIAS = " días";//Cte para el mensaje del dialog
    private static AlimentoDB alimentoDB;//Para usar los métodos de la bbdd de los alimentos de Mi Nevera
    private static Bitmap imagenDetalles;//Para recoger el bitmap de la bbdd
    private ArrayList<String>listadoAlimentosEscasez = new ArrayList<String>();//Para almacenar todos los alimentos que tienen escasez
    private int idAlimento;//Para guardar el id recogido de la bbdd
    private Alimento_Nuevo aliNuevo;//Para crear un objeto alimento nuevo y guardarlo en la bbdd
    private ComponenteListaCompra componente;//Para crear un componente nuevo de la lista para añadirlo a esta

    public Dialogos(Context context, Activity activity){
        this.contexto=context;
        //builder = new AlertDialog.Builder(contexto);
        this.clase = activity;
    }

    public Dialogos(Context context, FragmentActivity fragment){
        this.contexto=context;
        this.fragment = fragment;
    }

    public Dialogos(Context context){
        this.contexto=context;
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
    public void dialogCaducidad(int udsSeleccionadas, int caducidad, final Alimento alimento, final boolean manualCod, final String cod_barras){
        String day;//Para poner el mensaje del dialog
        Log.d("manual", "manual: " + manualCod);
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
                        idAlimento = adb.getIdAlimento(alimento);
                        Log.d("ref", "dialog id: " + idAlimento);
                        aliNuevo = new Alimento_Nuevo(alimento.getNombreAlimento(), alimento.getFecha_registro(), idAlimento);
                        andb.guardarAlimento(aliNuevo);
                        Toast.makeText(contexto, "Elemento guardado correctamente en Tu Nevera", Toast.LENGTH_SHORT).show();
                        adb.cerrarConexion();

                        if (manualCod){
                            Log.d("manual", "manual: " + manualCod);
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
    public void dialogCeroUnidades(final View vista, final int id, final Context contexto, final Bitmap foto, final String nombre, final CustomPageAdapter adapter, final int posicion, final WheelPicker wheelPicker, final int uds){
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
                        mostrarSnack(vista, id, contexto, foto, nombre, adapter, posicion ,wheelPicker, uds);
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //No hacemos nada
                        wheelPicker.setSelectedItemPosition(uds);
                    }
                })
                .build();
    }
    //SnackBar para deshacer la eliminación del elimento
    public static void mostrarSnack(View vista, final int id, final Context contexto, final Bitmap foto, final String nombre, final CustomPageAdapter adapter, final int posicion, final WheelPicker wheelPicker, final int uds){
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
                        wheelPicker.setSelectedItemPosition(uds);
                        break;
                    default:
                        try {
                            //Si no se pulsa deshacer, eliminamos el alimento y refrescamos la lista
                            alimentoDB = new AlimentoDB(contexto);
                            alimentoDB.borrarAlimento(id);
                            Toast.makeText(contexto, "Elemento eliminado", Toast.LENGTH_SHORT).show();
                            dialogAnadirLista(contexto, foto, nombre);
                            adapter.removePage(posicion);
                            alimentoDB.cerrarConexion();
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

    //Método para enviar la notificación
    public void enviarNotificacionCaducado(Alimento alimento, Context contexto, int posicion){
        intent = new Intent(contexto, DetallesActivity.class);
        intent.putExtra("Alimento", alimento);
        intent.putExtra("posicion", posicion);
        /*int id = alimento.getId();
        String nombreAlimento = alimento.getNombreAlimento();
        int cantidad = alimento.getCantidad();
        String fecha_registro = alimento.getFecha_registro();
        String fecha_caducidad = alimento.getFecha_caducidad();
        int dias_caducidad = alimento.getDias_caducidad();
        imagen = alimento.getImagen();
        i.putExtra("id", id);
        i.putExtra("nombre", nombreAlimento);
        i.putExtra("cantidad", cantidad);
        i.putExtra("fecha_registro", fecha_registro);
        i.putExtra("fecha_caducidad", fecha_caducidad);
        i.putExtra("dias_caducidad", dias_caducidad);
        i.putExtra("imagen", imagen);
        i.putExtra("alimento", alimento);*/
        intent.putExtra("ClasePadre", "Dialogos");
        Notification.Builder nb = new Notification.Builder(contexto);
        nb.setSmallIcon(R.mipmap.ic_launcher_f);
        nb.setContentTitle("¡¡¡Alimento Caducado!!!");
        nb.setContentText("¡Vaya! " + alimento.getNombreAlimento() + " ha caducado." +
                " Pulsa para ver los detalles.");
        nb.setContentIntent(PendingIntent.getActivity(contexto, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT));
        nb.setAutoCancel(true);
        //Permitimos que se pueda expandir la notificación
        Notification notificacion = new Notification.BigTextStyle(nb).bigText("¡Vaya! " + alimento.getNombreAlimento() +
                " ha caducado. Pulsa para ver los detalles.").build();
        NotificationManager nm =(NotificationManager)contexto.getSystemService(NOTIFICATION_SERVICE);
        //Emisión de la notificación. Le damos - el id del alimento
        nm.notify(alimento.getId()*-1, notificacion);

    }

    //Método para enviar la notificación cuando falten menos de dos días para la caducidad
    public void enviarNotificacionProximaCaducidad(Alimento alimento, Context contexto, int posicion){
        //Completar cuando esté creado el BuscarReceta por alimento

        //intent = new Intent (contexto, BuscarRecetaActivity.class);
        //intent.putExtra("Alimento", alimento);
        //intent.putExtra("posicion", posicion);
        //intent.putExtra("ClasePadre", "Dialogos");
        Notification.Builder nb = new Notification.Builder(contexto);
        nb.setSmallIcon(R.mipmap.ic_launcher_f);
        nb.setContentTitle("Va a caducar...");
        nb.setContentText("Faltan menos de 2 días para que " + alimento.getNombreAlimento() + " caduque." +
                " Ver los detalles.");
       // nb.setContentIntent(PendingIntent.getActivity(contexto, 0,
                //intent, PendingIntent.FLAG_UPDATE_CURRENT));
        nb.setAutoCancel(true);
        //Permitimos que se pueda expandir la notificación
        Notification notificacion = new Notification.BigTextStyle(nb).bigText("Faltan menos de 2 días para que " + alimento.getNombreAlimento() + " caduque." +
                " Pulsa para buscar recetas que contengan este alimento.").build();
        NotificationManager nm =(NotificationManager)contexto.getSystemService(NOTIFICATION_SERVICE);

        //Emisión de la notificación. Le damos el id del alimento
        nm.notify(alimento.getId(), notificacion);
    }

    //Método para enviar la notificación cuando haya menos de dos unidades de alimento
    public void enviarNotificacionProximaEscasez(Alimento alimento, Context contexto, int posicion){
        //Creamos el objeto componente con todos los datos
        componente = new ComponenteListaCompra(alimento.getId(), alimento.getNombreAlimento(),ComponenteListaCompra.TIPOS[0]);

        //intent = new Intent (contexto, AñadirAListaActivity.class);
        //intent.putExtra("Alimento", alimento);
        //intent.putExtra("posicion", posicion);
        //intent.putExtra("ClasePadre", "Dialogos");

        //listadoAlimentosEscasez.add(alimento.getNombreAlimento());
        intent = new Intent(contexto, DialogActivity.class);
        //intent.putExtra("Alimento", alimento.getNombreAlimento());
        intent.putExtra("Alimento", componente);
        Log.d("Alimento", componente.getNombreElemento());
        Notification.Builder nb = new Notification.Builder(contexto);
        nb.setSmallIcon(R.mipmap.ic_launcher_f);
        nb.setContentTitle("Escasez de alimento");
        nb.setContentText("Tiene menos de 2 unidades de " + componente.getNombreElemento() + "." +
                " Pulsa para recordártelo cuando hagas la lista de la compra.");
        nb.setContentIntent(PendingIntent.getActivity(contexto, alimento.getId(),
        intent, PendingIntent.FLAG_UPDATE_CURRENT));
        nb.setAutoCancel(true);

        //Permitimos que se pueda expandir la notificación
        Notification notificacion = new Notification.BigTextStyle(nb).bigText("Tiene menos de 2 unidades de " + componente.getNombreElemento() + "." +
                " Pulsa para recordártelo cuando hagas la lista de la compra.").build();
        NotificationManager nm =(NotificationManager)contexto.getSystemService(NOTIFICATION_SERVICE);

        //Emisión de la notificación. Le damos el id del alimento
        nm.notify(componente.getId(), notificacion);
        Log.d("Alimento", "id: " + componente.getId());
    }

    //Dialog para cuando se añadan productos a la lista de la compra
    public void dialogListaCompra(){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("Te recordaremos el producto cuando hagas la lista de la compra");
        //Título
        builder.setTitle("Producto almacenado");
        //Añadimos los botones
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //intent = new Intent(contexto, IdentificarAlimentoActivity.class);
                //contexto.startActivity(intent);
                DialogActivity ca = (DialogActivity) clase;
                //Finalizamos el activity
                ca.finishAffinity();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void dialogoModificarBorrar(String texto, final CustomArrayAdapterNuevaLista adapter, final int position){
        final String[] componenteReturn = new String[1];

        final Dialog dialog = new Dialog(clase);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogo_editar);

        dialog.show();

        final EditText editText = dialog.findViewById(R.id.etDialogEditar);
        Button btn = dialog.findViewById(R.id.btnCancelDialogEditar);
        Button btn2 = dialog.findViewById(R.id.btnOkDialogEditar);
        Button btn3 = dialog.findViewById(R.id.btnDeleteDialogoEditar);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                componenteReturn[0] = editText.getText().toString();
                adapter.modificar(position, componenteReturn[0]);
                dialog.dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                componenteReturn[0] = null;
                adapter.modificar(position, componenteReturn[0]);
                dialog.dismiss();
            }
        });

        editText.setText(texto);

        componenteReturn[0] =  editText.getText().toString();

        Log.d("tengen", "dialogoModificarBorrar: " + componenteReturn[0]);
    }

    //Dialog que se mostrará cuando no se haya encontrado ninguna receta con los criterios de búsqueda
    public void dialogoNoReceta(){
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("¡Vaya, qué pena!")
                //Ponemos el mensaje
                .setMessage("No hemos encontrado ninguna receta con esos criterios de búsqueda. Por favor, vuelve a intentarlo")
                //Asignamos el botón de negativo
                .setNegativeBtnText("Cancelar")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#1CACCC")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.gif_dk)
                .isCancellable(true)
                .build();
    }
    //Dialog que se mostrará cuando se vaya a borrar una lista de la compra de las que hay guardadas
    public void dialogoBorrarLista(){
        new FancyGifDialog.Builder(clase)
                //Ponemos el título
                .setTitle("¡Cuidado!")
                //Ponemos el mensaje
                .setMessage("¿Está seguro de que quiere borrar la lista?")
                //Asignamos el botón de negativo
                .setNegativeBtnText("Cancelar")
                //Asignamos el color de fondo del boton positivo
                .setPositiveBtnBackground("#1CACCC")
                .setPositiveBtnText("Aceptar")
                .setNegativeBtnBackground("#FFA9A7A8")
                //Asignamos el gif
                .setGifResource(R.drawable.gif_dixiek)
                .isCancellable(true)
                //Añadimos los listener
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        //Borramos la lista
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
    //Dialog que se mostrará cuando se vaya a filtrar por algún tipo de receta
    public void dialogoFiltroTipo(final Tipo tipo){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        //Mensaje del Alert
        builder.setMessage("Va a filtrar por " + tipo.getDescripcion() + ", ¿es correcto?");
        //Título
        builder.setTitle("Filtrando receta");
        //Añadimos los botones
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Hacemos la consulta
                new TabTipo.FiltrarPorTipo().execute(tipo.getId());
                //fragment.finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Volvemos a la pantalla para que el usuario pueda seleccionar otro tipo
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
