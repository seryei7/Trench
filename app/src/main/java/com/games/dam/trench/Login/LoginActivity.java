package com.games.dam.trench.Login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.games.dam.trench.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        //Quitar toolbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;
        switch (id){
            case R.id.signinEmail:
                intent = new Intent(LoginActivity.this, LoginWithEmail.class);
                startActivity(intent);
                break;
            case R.id.signinGoogle:
                Toast.makeText(this, "En pruebasssss", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.signinFacebook:
                Toast.makeText(this, "En pruebas", Toast.LENGTH_SHORT)
                        .show();
                break;
        }
    }
}
