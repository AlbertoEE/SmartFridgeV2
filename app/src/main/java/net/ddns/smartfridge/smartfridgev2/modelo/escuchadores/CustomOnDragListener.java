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

    public CustomOnDragListener(ImageView imageView, LinearLayout linearLayout, Context context){
        this.backGround = imageView;
        this.myLinearLayout = linearLayout;
        this.context = context;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        draggedView = (ImageView) event.getLocalState();
        zona = (RelativeLayout) v;
        ordenarPrimerPaso();
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
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(32, 44);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    draggedView.setLayoutParams(layoutParams);
                    zona.addView(draggedView);
                    draggedView.setVisibility(View.VISIBLE);
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    String dragData;
                    dragData = String.valueOf(item.getText());
                }else{
                    View hijaDeRelative = zona.getChildAt(0);
                    zona.removeView(hijaDeRelative);
                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(32,44);
                    hijaDeRelative.setLayoutParams(ll);
                    myLinearLayout.addView(hijaDeRelative);
                    hijaDeRelative.setVisibility(View.VISIBLE);

                    ViewGroup owner = (ViewGroup) draggedView.getParent();
                    owner.removeView(draggedView);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(32, 44);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                    draggedView.setLayoutParams(layoutParams);
                    zona.addView(draggedView);
                    draggedView.setVisibility(View.VISIBLE);

                }

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
    private void ordenarPrimerPaso(){
        this.children = new View[6];
        for (int i=0; i < 6; i++){
            this.children[i] = myLinearLayout.getChildAt(i);
        }
    }

    private void ordenarSegundoPaso() {
        this.children = new View[6];
        for (int i=0; i < 6; i++){
            this.children[i] = myLinearLayout.getChildAt(i);
        }
        Log.d("HijosEnLinearLayout", String.valueOf(children.length));
        Log.d("HijosEnLinearLayout", String.valueOf(myLinearLayout.getChildCount()));
        Log.d("HijosEnLinearLayout", String.valueOf(children.length));
        Log.d("HijosEnLinearLayout", String.valueOf(myLinearLayout.getChildCount()));
        int x = 0;
        while(x != 6){
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 1){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 2){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 3){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 4){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 5){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 6){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
            for (View item : this.children) {
                if(Integer.parseInt(context.getResources().getResourceEntryName(item.getId()).substring(5)) == 7){
                    myLinearLayout.removeView(item);
                    myLinearLayout.addView(item);
                    x++;
                }
            }
        }
    }
}
