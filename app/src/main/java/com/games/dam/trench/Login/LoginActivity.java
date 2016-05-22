package com.games.dam.trench.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.games.dam.trench.R;

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button email,google,facebook;
    protected void onCreate(Bundle savedInstanceState) {
        //Quitar toolbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);

        email = (Button)findViewById(R.id.signinEmail);
        google = (Button)findViewById(R.id.signinGoogle);
        facebook = (Button)findViewById(R.id.signinFacebook);

        email.setOnClickListener(this);
        google.setOnClickListener(this);
        facebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.signinEmail:
                Intent intent = new Intent().setClass(LoginActivity.this, LoginWithEmail.class);
                startActivity(intent);
                finish();
                break;
            case R.id.signinGoogle:
                Toast.makeText(this, "En pruebas", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.signinFacebook:
                Toast.makeText(this, "En pruebas", Toast.LENGTH_SHORT)
                        .show();
                break;
        }
    }
}
