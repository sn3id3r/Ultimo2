package e.lil.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText TxtEmail;
    private EditText TxtPassword;

    private FirebaseAuth mAuth;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mensajeRef = reference.child("Mensaje").child("Ultimo");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TxtEmail = (EditText)findViewById(R.id.TextEmail);
        TxtPassword = (EditText) findViewById(R.id.TextPassword);

    }

    public void Registrar(View view) {

        registrarUsuario();
    }

    public void Ingresar(View view) {
        ingresarUsuario();
    }

    private void ingresarUsuario() {

        final String email = TxtEmail.getText().toString().trim();
        String password = TxtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Ingrese Correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese Contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Bienvenido: "+TxtEmail.getText(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                            intent.putExtra(MapsActivity.Email, email);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo ingresar", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void registrarUsuario() {

        String email = TxtEmail.getText().toString().trim();
        String password = TxtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Ingrese Correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese Contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Se ha registrado el usuario: "+TxtEmail.getText(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "No se pudo registrar el usuario", Toast.LENGTH_LONG).show();
                        }
                    }
                });

       /* Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);*/
    }


    public void Conductores(View view) {
        Intent intent = new Intent(this, Conductores.class);
        startActivity(intent);
    }
}
