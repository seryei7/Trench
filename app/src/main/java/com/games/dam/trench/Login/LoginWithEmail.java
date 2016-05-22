package com.games.dam.trench.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.games.dam.trench.HomeActivity;
import com.games.dam.trench.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginWithEmail extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "EmailPassword";
    private FirebaseAuth fba;
    FirebaseUser user;
    //private FirebaseAuth.AuthStateListener fbalistener;
    private EditText mEmailField, mPasswordField;
    private Button signup, signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Quitar toolbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginwithemail_fragment);

        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);

        signin = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.singup);

        signin.setOnClickListener(this);
        signup.setOnClickListener(this);

        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();

        if(user == null){
            Toast.makeText(getApplicationContext(),"Registrate", Toast.LENGTH_SHORT).show();

        }else{
            /*fbalistener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                    Intent intent = new Intent(LoginWithEmail.this, HomeActivity.class);
                    startActivity(intent);
                }
            };*/
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //fba.addAuthStateListener(fbalistener);
    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (fbalistener != null) {
            fba.removeAuthStateListener(fbalistener);
        }*/
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        fba.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginWithEmail.this, "Fallo en la autentificación",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginWithEmail.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        hideProgressDialog();
                    }
                });
    }

    /*public void signOut() {
        fba.signOut();
    }*/

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        fba.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginWithEmail.this, "Fallo en la autentificación",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Intent intent = new Intent(LoginWithEmail.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        hideProgressDialog();
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singup:
                createAccount(mEmailField.getText().toString().trim(),
                        mPasswordField.getText().toString().trim());
                break;
            case R.id.login:
                signIn(mEmailField.getText().toString().trim(),
                        mPasswordField.getText().toString().trim());
        }
    }
}
