package net.ddns.smartfridge.smartfridgev2.modelo.escuchadores;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;

/**
 * Created by Alberto on 11/01/2018.
 */

public class CustomOnDragListener2 implements View.OnDragListener {
    private boolean dentro;
    private LinearLayout myLinearLayout;
    private View[] children;
    private ImageView draggedView;
    private Context context;

    public CustomOnDragListener2(Context context){
        this.context = context;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        myLinearLayout = (LinearLayout) v;
        ordenarPrimerPaso();
        int action = event.getAction();

        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                draggedView = (ImageView) event.getLocalState();
                dentro = false;

                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){

                    return true;
                }

                return false;
            case DragEvent.ACTION_DRAG_ENTERED:

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:

                return true;
            case DragEvent.ACTION_DRAG_EXITED:

                return true;
            case DragEvent.ACTION_DROP:

                dentro = true;

                ViewGroup owner = (ViewGroup) draggedView.getParent();
                owner.removeView(draggedView);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(38, 44);
                draggedView.setLayoutParams(layoutParams);
                myLinearLayout.addView(draggedView);

                draggedView.setVisibility(View.VISIBLE);
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData;

                dragData = String.valueOf(item.getText());
                Log.d("DragAndDrop", dragData);

                return true;
            case DragEvent.ACTION_DRAG_ENDED:
                if(this.children.length == 7){
                    ordenarSegundoPaso();
                }
                if (!dentro){
                    draggedView.setVisibility(View.VISIBLE);
                }
            default:
                break;

        }
        return false;
    }

    private void ordenarPrimerPaso(){
        int childcount = myLinearLayout.getChildCount();
        this.children = new View[childcount];
        Log.d("hijos", String.valueOf(childcount));
        // get children of linearlayout
        for (int i=0; i < childcount; i++){
            this.children[i] = myLinearLayout.getChildAt(i);
        }
    }

    private void ordenarSegundoPaso() {
        this.children = new View[7];
        Log.d("hijos", String.valueOf(7));
        // get children of linearlayout
        for (int i=0; i < 7; i++){
            this.children[i] = myLinearLayout.getChildAt(i);
        }

        myLinearLayout.removeAllViews();
        int x = 0;
        while(x != 7){
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 1){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 2){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 3){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 4){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 5){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 6){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 7){
                    myLinearLayout.addView(item);
                    x++;
                }
            }
        }
    }
}
