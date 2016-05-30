package com.games.dam.trench;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.games.dam.trench.Login.LoginActivity;
import com.games.dam.trench.MultiPlayer.MPMain;
import com.games.dam.trench.SoloPlayer.SPMain;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.home_fragment);

        mAuth = FirebaseAuth.getInstance();
    }


    public void iniciarAjustes(View view) {
        final String[] choices = getResources().getStringArray(R.array.settings);
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("AJUSTES")
                .setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (choices[which]){
                            case "Cerrar sesión":
                                mAuth.signOut();

                                Toast.makeText(HomeActivity.this, "Cerrando sesión...", Toast.LENGTH_SHORT)
                                    .show();
                                startActivity(new Intent().setClass(HomeActivity.this, LoginActivity.class));
                                finish();
                                break;
                            case "Información":
                                startActivity(new Intent().setClass(HomeActivity.this, InfoActivity.class));
                                break;
                        }
                    }
                })
                .show();
    }

    public void iniciarDosJugadores(View view) {
        Intent intentDosJugadores = new Intent(this, MPMain.class);
        startActivity(intentDosJugadores);
    }

    public void iniciarUnJugador(View view) {
        Intent intentUnJugador = new Intent(this, SPMain.class);
        startActivity(intentUnJugador);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialogExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void dialogExit(){
        new AlertDialog.Builder(this)
                .setTitle("Salir de Trench")
                .setMessage("¿Seguro que quieres salir de Trench?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        finishAffinity();
                    }
                })
                .show();
    }

}
