package com.example.loginusiel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    EditText namer, emailr, phoner, passwordr;
    Button registrar;
    TextView login;
    AutoCompleteTextView aupuestolu, auzonalu;
    boolean isnamevalid, isemailvalid, isphonevalid, ispassvalid;
    TextInputLayout nameerror, emailerror, phoneerror, passerror;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    //variables datos a registrar
    private String namev = "";
    private String emailv = "";
    private String phonv = "";
    private String passv = "";
    private String puesr = "";
    private String zonr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        namer = findViewById(R.id.namer);
        emailr = findViewById(R.id.emailr);
        phoner = findViewById(R.id.phoner);
        passwordr = findViewById(R.id.passwordr);
        aupuestolu = findViewById(R.id.aupuestolu);
        auzonalu = findViewById(R.id.auzonalu);
        login = findViewById(R.id.loginr);
        registrar = findViewById(R.id.registrarr);
        nameerror = findViewById(R.id.Nameerrorr);
        emailerror = findViewById(R.id.Emailerrorr);
        phoneerror = findViewById(R.id.Phoneerrorr);
        passerror = findViewById(R.id.Passerrorr);

        //llenado de zona
        String[] item1 = {"Huamantla", "Libres", "El Seco", "Guzman"};
        //Se agrupa el array adapter
        ArrayAdapter<String> item1L = new ArrayAdapter<>(Registro.this, R.layout.s_dropdown_item, item1);
        auzonalu.setAdapter(item1L);

        //llenado de puestos
        String[] item2 = {"Muestreador", "Guardia de seguridad", "Auxiliar", "Ingeniero"};
        //Se agrupa el array adapter
        ArrayAdapter<String> item2L = new ArrayAdapter<>(Registro.this, R.layout.s_dropdown_item, item2);
        aupuestolu.setAdapter(item2L);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                namev = namer.getText().toString();
                emailv = emailr.getText().toString();
                phonv = phoner.getText().toString();
                passv = passwordr.getText().toString();
                puesr = aupuestolu.getText().toString();
                zonr = auzonalu.getText().toString();

                validacion();
            }
        });

    }

    private void validacion() {
        // Check for a valid name.
        if (namer.getText().toString().isEmpty()) {
            nameerror.setError(getResources().getString(R.string.name_error));
            isnamevalid = false;
        } else {
            isnamevalid = true;
            nameerror.setErrorEnabled(false);
        }

        // Check for a valid email address.
        if (emailr.getText().toString().isEmpty()) {
            emailerror.setError(getResources().getString(R.string.email_error));
            isemailvalid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailr.getText().toString()).matches()) {
            emailerror.setError(getResources().getString(R.string.error_invalid_email));
            isemailvalid = false;
        } else {
            isemailvalid = true;
            emailerror.setErrorEnabled(false);
        }

        // Check for a valid phone number.
        if (phoner.getText().toString().isEmpty()) {
            phoneerror.setError(getResources().getString(R.string.phone_error));
            isphonevalid = false;
        } else {
            isphonevalid = true;
            phoneerror.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (passwordr.getText().toString().isEmpty()) {
            passerror.setError(getResources().getString(R.string.password_error));
            ispassvalid = false;
        } else if (passwordr.getText().length() < 6) {
            passerror.setError(getResources().getString(R.string.error_invalid_password));
            ispassvalid = false;
        } else {
            ispassvalid = true;
            passerror.setErrorEnabled(false);
        }

        if (isnamevalid && isemailvalid && isphonevalid && ispassvalid) {
            Toast.makeText(getApplicationContext(), "Verificando... Espere", Toast.LENGTH_SHORT).show();
            registraruser();
        }
    }

    private void registraruser() {
        mAuth.createUserWithEmailAndPassword(emailv, passv).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("Nombre", namev);
                    map.put("Email", emailv);
                    map.put("Telefono", phonv);
                    map.put("Contraseña", passv);
                    map.put("Puesto", puesr);
                    map.put("Zona", zonr);

                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(Registro.this, MainActivity.class));
                                Toast.makeText(Registro.this,"Bienvenido" + emailv, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(Registro.this,"No se pudieron agregar los datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Registro.this,"No se pudo completar la tarea", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

