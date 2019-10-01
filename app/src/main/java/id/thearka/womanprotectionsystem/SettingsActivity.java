package id.thearka.womanprotectionsystem;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends Fragment {

    View v;
    CircleImageView iv_closeup;
    ImageView iv_ktp;
    EditText et_nama, et_pass, et_repass, et_confirmpass;
    TextView tv_infoktp, tv_infocloseup;
    Button btn_simpan;

    FirebaseAuth user;
    DatabaseReference userInfo;
    AuthCredential userCredential;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_settings, container, false);

        iv_closeup = v.findViewById(R.id.iv_closeUp);
        iv_ktp = v.findViewById(R.id.iv_ktp);
        et_nama = v.findViewById(R.id.et_nama);
        et_pass = v.findViewById(R.id.et_password);
        et_repass = v.findViewById(R.id.et_repassword);
        et_confirmpass = v.findViewById(R.id.et_konfirmPass);
        btn_simpan = v.findViewById(R.id.btn_simpan);

        user = FirebaseAuth.getInstance();
        userInfo = FirebaseDatabase.getInstance().getReference().child("Users");

        userInfo.child(user.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                et_nama.setText(dataSnapshot.child("nama").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        return v;
    }

    private void saveData() {
        String nama, pass, repass, confirmpass;
        nama = et_nama.getText().toString();
        pass = et_pass.getText().toString();
        repass = et_repass.getText().toString();
        confirmpass = et_confirmpass.getText().toString();

        userCredential = EmailAuthProvider.getCredential(user.getCurrentUser().getEmail(), confirmpass);

        if(!TextUtils.isEmpty(confirmpass)) {
            if(TextUtils.isEmpty(nama)) {
                Toast.makeText(this.getContext(), "Nama tidak boleh kosong", Toast.LENGTH_LONG).show();
            } else {
                if(!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(repass)){
                    if(pass.equals(repass)){
                        final String finalPass = pass;
                        user.getCurrentUser().reauthenticate(userCredential).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            user.getCurrentUser().updatePassword(finalPass);
                                            Toast.makeText(SettingsActivity.this.getContext(), "Password Updated", Toast.LENGTH_LONG).show();
                                        } else{
                                            Toast.makeText(SettingsActivity.this.getContext(), task.getException().getMessage()!=null?task.getException().getMessage():"Gagal", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                        );
                    }else {
                        Toast.makeText(this.getContext(), "Password baru tidak sama", Toast.LENGTH_LONG).show();
                    }
                }
                final String finalNama = nama;
                user.getCurrentUser().reauthenticate(userCredential).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    userInfo.child(user.getCurrentUser().getUid()).child("nama").setValue(finalNama);
                                    Toast.makeText(SettingsActivity.this.getContext(), "Nama Updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SettingsActivity.this.getContext(), task.getException().getMessage()!=null?task.getException().getMessage():"Gagal", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                );
            }

        } else {
            Toast.makeText(this.getContext(), "Password saat ini tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
    }
}
