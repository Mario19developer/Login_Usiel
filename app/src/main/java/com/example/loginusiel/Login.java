package com.example.loginusiel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    //Agregacion de elementos de layout
    EditText email, password;
    Button login;
    TextView registrar;

    //Parametros de error
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;

    //Se pasa el usuario y la contraseña como variables para proceder a generar el login
    private String emaill = "";
    private String passwordl = "";

    //Se trae la autentificacion de Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Se genera la intancia de la base de datos Firebase
        mAuth = FirebaseAuth.getInstance();

        //Seteo de layout hacia la clase
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        login = findViewById(R.id.Login);
        registrar = findViewById(R.id.Regitrar);
        emailError = (TextInputLayout) findViewById(R.id.EmailError);
        passError =  (TextInputLayout) findViewById(R.id.PassError);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emaill = email.getText().toString();
                passwordl = password.getText().toString();

                validacion();
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);
            }
        });
    }

    private void validacion() {

        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isEmailValid && isPasswordValid) {
            Toast.makeText(getApplicationContext(), "Por favor Espere...", Toast.LENGTH_SHORT).show();
            LoginUser();
        }
    }

    private void LoginUser() {
        mAuth.signInWithEmailAndPassword(emaill, passwordl).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(Login.this, MainActivity.class));
                    Toast.makeText(Login.this, "Bienvenido " + emaill, Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(Login.this, "No se pudo iniciar sesion comprueba los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}