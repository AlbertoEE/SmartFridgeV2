package net.ddns.smartfridge.smartfridgev2.modelo.escuchadores;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.CaducidadAlimento;

/**
 * Created by Alberto on 10/01/2018.
 */

public class CustomOnDragListener implements View.OnDragListener {
    private boolean dentro;
    private RelativeLayout zona;
    private LinearLayout myLinearLayout;
    private ImageView draggedView;
    private ImageView backGround;
    private View children[];
    private Context context;
    private CaducidadAlimento caducidadAlimento;
    private int w;
    private int h;

    public CustomOnDragListener(ImageView imageView, LinearLayout linearLayout, Context context, CaducidadAlimento caducidadAlimento){
        this.backGround = imageView;
        this.myLinearLayout = linearLayout;
        this.context = context;
        this.caducidadAlimento = caducidadAlimento;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        draggedView = (ImageView) event.getLocalState();
        zona = (RelativeLayout) v;
        int action = event.getAction();
        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
                dentro = false;

                if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
                    backGround.setColorFilter(Color.BLUE);
                    backGround.invalidate();
                    return true;
                }

                return false;

            case DragEvent.ACTION_DRAG_ENTERED:

                backGround.setColorFilter(Color.GREEN);
                backGround.invalidate();

                return true;

            case DragEvent.ACTION_DRAG_LOCATION:

                return true;

            case DragEvent.ACTION_DRAG_EXITED:

                backGround.setColorFilter(Color.BLUE);
                backGround.invalidate();

                return true;

            case DragEvent.ACTION_DROP:
                dentro = true;
                backGround.clearColorFilter();
                backGround.invalidate();
                Log.d("dafuq", String.valueOf(zona.getChildCount()));
                if(zona.getChildCount() == 0){
                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    owner.removeView(draggedView);
                    LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) draggedView.getLayoutParams();
                    w = linearLayoutParams.width;
                    h = linearLayoutParams.height;
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(w*2, h*2);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    draggedView.setLayoutParams(layoutParams);
                    zona.addView(draggedView);
                    draggedView.setVisibility(View.VISIBLE);
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData;
                    dragData = String.valueOf(item.getText());
                    caducidadAlimento.setTiempo_Caducidad(Integer.parseInt(dragData));
                }else{
                    View hijaDeRelative = zona.getChildAt(0);
                    zona.removeView(hijaDeRelative);
                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(w,h);
                    Log.d("width", "onDrag: " + w);
                    Log.d("height", "onDrag: " + h);
                    hijaDeRelative.setLayoutParams(ll);
                    myLinearLayout.addView(hijaDeRelative);
                    hijaDeRelative.setVisibility(View.VISIBLE);

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
