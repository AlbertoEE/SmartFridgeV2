package net.ddns.smartfridge.smartfridgev2.modelo.personalizaciones;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.ddns.smartfridge.smartfridgev2.vista.actividades.ca.CaducidadAlimento;

/**
 * Created by Alberto on 10/01/2018.
 *
 * Esta clase maneja la zona de drop del Activity CaducidadAlimento
 */

public class CustomOnDragListener implements View.OnDragListener {
    private boolean dentro; //Booelean que declara si la imagen arrastrada está dentro o no
    private RelativeLayout zona; //Layout que contiene la zona de drop
    private LinearLayout myLinearLayout; //Layout que contiene todos las vistas que permiten el drag
    private ImageView draggedView; //Imagen que esta siendo arrastrada
    private ImageView backGround; //Imagen a la que se le va a dar filtros de color para guiar al usuario
    private View children[]; //Array que rellenaremos cual pavo con todos los hijos del myLinearLayout
    private Context context;
    private CaducidadAlimento caducidadAlimento;
    private int w; //Anchura de los dias
    private int h; //Altura de los dias

    public CustomOnDragListener(ImageView imageView, LinearLayout linearLayout, Context context, CaducidadAlimento caducidadAlimento){
        this.backGround = imageView;
        this.myLinearLayout = linearLayout;
        this.context = context;
        this.caducidadAlimento = caducidadAlimento;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        draggedView = (ImageView) event.getLocalState(); //Obtenemos la imagen que está siendo arrastrada
        zona = (RelativeLayout) v; //Obtenemos una referencia de la drop zone
        int action = event.getAction(); //Obtenemos la acción que se esta realizando en este momento
        switch (action){
            case DragEvent.ACTION_DRAG_STARTED: //Empiza el drag
                dentro = false; //Como acaba de empezar es imposible que esté dentro de la zona
                //Si la zona de drop puede recibir información del tipo texto entonces empieza el juego
                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    //Indicamos al usuario donde podemos hacer el drop a traves de un filtro de color azul
                    backGround.setColorFilter(Color.BLUE, PorterDuff.Mode.OVERLAY);
                    backGround.invalidate(); //Esto simplemente es para que se pinte otra vez la imagen con el filtro
                    return true; //Devolvemos true porque hemos comprobado que la drop zone puede recibir info tipo texto
                }

                return false; //Devolvemos falso si no la drop zone no puede recibir texto

            case DragEvent.ACTION_DRAG_ENTERED: //Acción que indica que ha la view arrastrada está entrando en la drop zone

                //Ponemos a verde la imagen de la drop zone para indicar al usuario que ya puede soltar
                backGround.setColorFilter(Color.GREEN, PorterDuff.Mode.OVERLAY);
                backGround.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                //Este evento sirve para recoger en que punto cardinal a soltado dicha vista que estaba siendo arrastrada

                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                //Aquí mostramos al usuario que ha saldio de la drop zone poniendo el fondo del mismo
                //color que cuando la drop zone estaba esperando a recibir una vista que estaba siendo
                //arrastrada

                backGround.setColorFilter(Color.BLUE, PorterDuff.Mode.OVERLAY);
                backGround.invalidate();

                return true;

            case DragEvent.ACTION_DROP:
                //Este evento surge cuando el drop se ha realizado de manera exitosa sobre la drop-zone
                dentro = true;
                //Limpiamos los filtros
                backGround.clearColorFilter();
                backGround.invalidate();

                //Si la zona de drop no tiene hijos tan solo asignamos la vista arrastrada a la zona
                if(zona.getChildCount() == 0){
                    //Obtenemos el layout padre de la vista que estaba siendo arrastrada
                    //Esto es lo mismo que coger el "myLinearLayout" que hemos pedido en el constructor
                    ViewGroup owner = (ViewGroup) draggedView.getParent();

                    //Quitamos la vista de su layout padre
                    owner.removeView(draggedView);

                    //Obtenemos las reglas que seguía en el layout padre, para obtener su altura y anchura
                    LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) draggedView.getLayoutParams();
                    w = linearLayoutParams.width;
                    h = linearLayoutParams.height;
                    //Lo multiplicamos por dos para que se vea mejor
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w*2, h*2);
                    //Añadimos la norma de que se centre en el padre
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    //hacemos el set de las normas
                    draggedView.setLayoutParams(layoutParams);

                    //Añadimos la vista con las nuevas reglas a la zona de drop
                    zona.addView(draggedView);
                    //Le damos visibilidad
                    draggedView.setVisibility(View.VISIBLE);
                    //Obtenemos la información que tre asignada la "dragedView"
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    //La asignamos al atributo de la clase de "caducidadAlimento"
                    String dragData;
                    dragData = String.valueOf(item.getText());
                    caducidadAlimento.setTiempo_Caducidad(Integer.parseInt(dragData));
                }else{ //En caso de que la drop zone tenga un hijo asignado

                    //Obtenemos la vista que está asignada a la drop-zone
                    View hijaDeRelative = zona.getChildAt(0);
                    zona.removeView(hijaDeRelative);
                    //Obtenemos las reglas del linear Layout a traves de cualquier vista hija que
                    //pertenezca al linear layout
                    LinearLayout.LayoutParams ll
                            = (LinearLayout.LayoutParams) myLinearLayout.getChildAt(0).getLayoutParams();
                    //A la vista que se encuentra en la drop-zone le damos reglas de linearLayout
                    hijaDeRelative.setLayoutParams(ll);
                    //Añadimos la vista que estaba en la drop zone a su lugar original
                    myLinearLayout.addView(hijaDeRelative);
                    //le damos visibilidad
                    hijaDeRelative.setVisibility(View.VISIBLE);

                    //A partir de aqui ya tenemos la drop zone vacia por lo tanto
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    owner.removeView(draggedView);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w*2, h*2);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    draggedView.setLayoutParams(layoutParams);
                    zona.addView(draggedView);
                    draggedView.setVisibility(View.VISIBLE);

                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData;
                    dragData = String.valueOf(item.getText());
                    caducidadAlimento.setTiempo_Caducidad(Integer.parseInt(dragData));
                }

                caducidadAlimento.setControlDragAndDrop(-1);

                return true;

            case DragEvent.ACTION_DRAG_ENDED:


                if (!dentro){
                    draggedView.setVisibility(View.VISIBLE);
                }else{
                    if(myLinearLayout.getChildCount() == 6){
                        ordenarSegundoPaso();
                    }
                }
                backGround.clearColorFilter();
                return true;

            default:
                break;
        }
        return false;
    }

    private void ordenarSegundoPaso() {
        this.children = new View[6];
        for (int i=0; i < 6; i++){
            this.children[i] = myLinearLayout.getChildAt(i);
        }
        int x = 0;
        while(x != 6){
            for (int i=1; i < 8; i++){
                for (View item : this.children) {
                    if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == i){
                        myLinearLayout.removeView(item);
                        myLinearLayout.addView(item);
                        x++;
                    }
                }
            }
        }
    }
}
