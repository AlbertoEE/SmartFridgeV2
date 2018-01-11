package net.ddns.smartfridge.smartfridgev2.modelo.escuchadores;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Alberto on 11/01/2018.
 */

public class CustomOnDragListener2 implements View.OnDragListener {
    private boolean dentro;
    private LinearLayout myLinearLayout;
    private View[] children;
    private ImageView draggedView;
    private ImageView backGround;

    @Override
    public boolean onDrag(View v, DragEvent event) {
        // v será el elemento el cual hace referencia a la zona de drop, es decir, el elemento que
        // tiene asignado el dragListener
        myLinearLayout = (LinearLayout) v;
        //ordenarPrimerPaso();

        int action = event.getAction();

        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                draggedView = (ImageView) event.getLocalState();
                // Este evento se activa cuando se ha iniciado el DRAG, aquí debemos indicar la zona
                // dónde permitimos al usuario hacer el DROP, verbigracia, poniendo un filtro azul
                //  a la zona de DROP

                // v.setColorFilter(Color.BLUE);
                // Ahora invalidamos el view para forzar el recolor
                // v.invalidate();

                // Debemos comprobar si nuestra zona de drop puede recibir un ClipData del tipo que
                // nos aportará el elemento que está siendo arrastrado
                dentro = false;

                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    // Aquí hacemos los cambios de color etc
                    // Devolvemos true porque se supone que el drop puede recibir ese tipo
                    // de ClipData
                    //backGround.setColorFilter(Color.BLUE);
                    //backGround.invalidate();
                    return true;
                }
                //devolvemos false si no pasa por la condicional
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                // Este evento surge cuando el elemento que está siendo arrastrado se encuentra
                // sobre la zona de DROP

                // Podemos aprovechar para cambiar el color de la zona de DROP para indicar al
                // usuario que puede realizar el DROP

                //backGround.setColorFilter(Color.GREEN);
                //backGround.invalidate();

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                // Aquí obtenemos en que parte de la pantalla se encuentra el elemento

                event.getX();
                event.getY();

                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                // Este evento surge cuando el elemento que está siendo arrastrado sale de la zona de
                // DROP, lo suyo sería poner el color de la zona de DROP igual que cuando comenzó
                // a arrastrarse el elemento (ACTION_DRAG_STARTED)

                //backGround.setColorFilter(Color.BLUE);
                //backGround.invalidate();

                return true;
            case DragEvent.ACTION_DROP:
                // Este evento surge cuando el elemento que se está arrastrando se suelta en la zona
                // de DROP, es decir, el DROP ha sido exitoso.

                dentro = true;
                //backGround.clearColorFilter();
                //backGround.invalidate();
                // El método getLocalState te devuelve el objeto que ha iniciado el método
                // startDragAndDrop, es decir, te devuelve una referencia del objeto que esta siendo
                // arrastrado actualmente.
                // En este caso como el elemento que arrastramos pertenece a un gridLayout-->LinearLaout
                // y debemos obtener desde donde viene, es decir, su padre para eliminarlo de ahí ya
                // que su nueva posición puede no corresponderse
                ViewGroup owner = (ViewGroup) draggedView.getParent();
                owner.removeView(draggedView);
                // Ahora obtenemos el contenedor donde se va a alojar el objeto ARRASTRADO y lo
                // añadimos a la zona de DROP
                myLinearLayout.addView(draggedView);

                // Importante dale a la zona de la que proviene el objeto visibilidad (CREO QUE FUNCIONA SIN ESTE PASO)
                // ESTE PASO ES MUY IMPORTANTE TENEMOS QUE DAR VISIBILIDAD ACABADA AL OBJETO ARRASTRADO
                // YA QUE HEMOS ACABADO DE MOVERLO Y LO HEMOS SOLTADO
                //ordenarSegundoPaso();
                draggedView.setVisibility(View.VISIBLE);
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData;
                // Gets the text data from the item.
                dragData = String.valueOf(item.getText());
                Log.d("DragAndDrop", dragData);

                // El ejemplo donde lo he visto lo usa en un Thread aparte pero funciona igual si está fuera del thread
                /*draggedView.post(new Runnable() {
                    @Override
                    public void run() {
                        draggedView.setVisibility(View.GONE);
                    }
                });*/
                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                // Este evento se activa SIEMPRE, independientemente si está o no en la zona de DROP
                // en cuanto se suelta el objeto se lanza el evento.

                // Comprobamos si está en la zona de drop el obejto arrastrado, de no ser el caso
                // le damos visibilidad para que vuelva a verse en su sitio
                if (!dentro){
                    draggedView.setVisibility(View.VISIBLE);
                }
                //backGround.clearColorFilter();

                /*draggedView.post(new Runnable() {
                    @Override
                    public void run() {
                        draggedView.setVisibility(View.VISIBLE);
                    }
                });*/
            default:
                break;

        }
        return false;
    }

    private void ordenarPrimerPaso(){
        int childcount = myLinearLayout.getChildCount();
        View[] children = new View[childcount];

        // get children of linearlayout
        for (int i=0; i < childcount; i++){
            children[i] = myLinearLayout.getChildAt(i);
        }
        this.children = children;

        myLinearLayout.removeAllViews();

        //and resort, first position
        myLinearLayout.addView(this.children[0]);
        myLinearLayout.addView(this.children[1]);
        myLinearLayout.addView(this.children[2]);
        myLinearLayout.addView(this.children[3]);
        myLinearLayout.addView(this.children[4]);
        myLinearLayout.addView(this.children[5]);
        myLinearLayout.addView(this.children[6]);
    }

    private void ordenarSegundoPaso() {
        int childcount = myLinearLayout.getChildCount();
        View[] children = new View[childcount];

        // get children of linearlayout
        for (int i=0; i < childcount; i++){
            children[i] = myLinearLayout.getChildAt(i);
        }
        this.children = children;
        myLinearLayout.removeAllViews();

        //and resort, first position
        myLinearLayout.addView(this.children[0]);
        myLinearLayout.addView(this.children[1]);
        myLinearLayout.addView(this.children[2]);
        myLinearLayout.addView(this.children[3]);
        myLinearLayout.addView(this.children[4]);
        myLinearLayout.addView(this.children[5]);
        myLinearLayout.addView(this.children[6]);
    }
}
