package net.ddns.smartfridge.smartfridgev2.vista;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import net.ddns.smartfridge.smartfridgev2.R;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainCa;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainClc;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainPm;
import net.ddns.smartfridge.smartfridgev2.vista.fragmentos.MainSr;

public class InitialActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.ca:
                    MainCa ca = new MainCa();
                    setTitle("Control de Alimentos");
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, ca, "CA");
                    fragmentTransaction.commit();
                    return true;
                case R.id.pm:
                    MainPm pm = new MainPm();
                    setTitle("Programar Menu");
                    FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.content, pm, "PM");
                    fragmentTransaction2.commit();
                    return true;
                case R.id.clc:
                    MainClc clc = new MainClc();
                    setTitle("Crear lista de la Compra");
                    FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction3.replace(R.id.content, clc, "CLC");
                    fragmentTransaction3.commit();
                    return true;
                case R.id.sr:
                    MainSr sr = new MainSr();
                    setTitle("Sugerencias de recetas");
                    FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction4.replace(R.id.content, sr, "SR");
                    fragmentTransaction4.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        MainCa ca = new MainCa();
        setTitle("Control de Alimentos");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, ca, "CA");
        fragmentTransaction.commit();
    }

}
