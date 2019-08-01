package id.thearka.appelperempuan;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static id.thearka.appelperempuan.MainActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class LoginActivity extends AppCompatActivity {

    Button login;
    TextInputEditText etEmail, etPassword;
    TextView register;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        cekPermission();
        Objects.requireNonNull(getSupportActionBar()).hide();
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        register = findViewById(R.id.letregister);
        login = findViewById(R.id.btnlogin);
        etEmail = findViewById(R.id.etemaillogin);
        etPassword = findViewById(R.id.etpasswordlogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(etEmail.getText()).toString();
                String password = Objects.requireNonNull(etPassword.getText()).toString();

                prosesLogin(email, password);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
            }
        });
    }

    private void cekPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Access Granted", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Maaf, anda harus memberi aplikasi ini izin", Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
            }
        }
    }

    private void prosesLogin(final String email, final String password) {
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Email atau Password kosong", Toast.LENGTH_SHORT).show();
        } else{
            progressDialog.setTitle("Login");
            progressDialog.setMessage("Sedang mengecek data anda.....");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                                sharedPreferences = getApplicationContext().getSharedPreferences("id.thearka.appelperempuan", MODE_PRIVATE);

                                if(sharedPreferences.getString("email", null)==null && sharedPreferences.getString("password", null)==null){
                                    editor = sharedPreferences.edit();
                                    Email = email;
                                    Password = password;
                                    editor.putString("email", Email);
                                    editor.putString("password", Password);
                                    editor.apply();

                                    prosesLogin(Email, Password);
                                    return;
                                }

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else{
                                Toast.makeText(LoginActivity.this, "Login Gagal, Coba Lagi!", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }
}
