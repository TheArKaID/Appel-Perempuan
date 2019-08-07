package id.thearka.appelperempuan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextView login;
    EditText etEmail, etPassword, etNama;
    Button btnRegister;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    DatabaseReference registrationRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseApp.initializeApp(this);
        Objects.requireNonNull(getSupportActionBar()).hide();

        etEmail = findViewById(R.id.etemailregister);
        etPassword = findViewById(R.id.etpasswordregister);
        etNama = findViewById(R.id.etnama);
        btnRegister = findViewById(R.id.btnregister);
        mAuth = FirebaseAuth.getInstance();
        registrationRef = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(this);

        login = findViewById(R.id.letlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(etEmail.getText()).toString();
                String password = Objects.requireNonNull(etPassword.getText()).toString();
                String nama = Objects.requireNonNull(etNama.getText()).toString();

                prosesLogin(email, password, nama);
            }
        });
    }

    private void prosesLogin(final String email, final String password, final String nama) {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Email atau Password kosong", Toast.LENGTH_SHORT).show();
        } else{
            progressDialog.setTitle("Mendaftarkan");
            progressDialog.setMessage("Sedang mendaftarkan anda.....");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                saveData(email, password);
                                registrationRef.child(mAuth.getCurrentUser().getUid()).child("nama").setValue(nama);
                                registrationRef.child(mAuth.getCurrentUser().getUid()).child("verification").setValue("unverified");
                                Toast.makeText(RegisterActivity.this, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else{
                                Toast.makeText(RegisterActivity.this, "Pendaftaran Gagal, Coba Lagi!", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    private void saveData(String email, String password) {
        sharedPreferences = getApplicationContext().getSharedPreferences("id.thearka.appelperempuan", MODE_PRIVATE);

        if(sharedPreferences.getString("email", null)==null && sharedPreferences.getString("password", null)==null) {
            editor = sharedPreferences.edit();
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        }
    }
}
