package net.ddns.smartfridge.smartfridgev2.vista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.ddns.smartfridge.smartfridgev2.R;

public class PlanificarMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planificar_menu);
        Intent intent = getIntent();
    }
}
