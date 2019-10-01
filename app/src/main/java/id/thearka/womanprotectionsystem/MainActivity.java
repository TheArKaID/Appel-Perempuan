package id.thearka.womanprotectionsystem;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final static int MAX_VOLUME = 1000;
    private final String TAG = MainActivity.class.getSimpleName();
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String userID;
    DatabaseReference user;
    DatabaseReference userInfo;
    MarkerOptions markerOptions = new MarkerOptions();
    boolean statusRequest;
    boolean logoutUser = false;
    NavigationView navigationView;
    AudioManager mAudioManager;
    TextView nav_nama;
    private MediaPlayer mPlayer = null;
    private boolean isReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser != null ? firebaseUser.getUid() : null;
        user = FirebaseDatabase.getInstance().getReference().child("userHelpless");
        userInfo = FirebaseDatabase.getInstance().getReference().child("Users");

        setCurrentUser();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

//        Intent intent = new Intent(this, Home.class);
//        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            confirmOut("Keluar", "Anda akan keluar tanpa Log Out");
        }
    }

    private void confirmOut(final String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (title.equals("Keluar")) {
                                    dialog.dismiss();
                                    finishAndRemoveTask();
                                    System.exit(0);
                                } else {
                                    dialog.dismiss();
                                    logoutUser = true;
                                    cancellHelps();
                                    logout();
                                }
                            }
                        })
                .setNeutralButton("Batal", null);
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == Objects.requireNonNull(navigationView.getCheckedItem()).getItemId()) {
            return true;
        }
        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Home()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        } else if (id == R.id.nav_setting) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new SettingsActivity()).commit();
            navigationView.setCheckedItem(R.id.nav_setting);
//        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_edu) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Eduzone()).commit();
            navigationView.setCheckedItem(R.id.nav_edu);
        } else if (id == R.id.nav_news) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new NewsInformation()).commit();
            navigationView.setCheckedItem(R.id.nav_news);
        } else if (id == R.id.nav_report) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMain, new Report()).commit();
            navigationView.setCheckedItem(R.id.nav_report);
        } else if (id == R.id.nav_exit) {
            confirmOut("Keluar", "Anda akan keluar tanpa Log Out");
        } else if (id == R.id.nav_logout) {
            confirmOut("Logout", "Anda akan Log Out.");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void moveToSetting() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 9);
    }

    private void cancellHelps() {
        if(statusRequest){
            GeoFire fire = new GeoFire(user);
            fire.removeLocation(userID,
                    new GeoFire.CompletionListener() {
                        @Override
                        public void onComplete(String key, DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Help Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!logoutUser) {
            cancellHelps();
        }
    }

    private void logout() {
        firebaseAuth.signOut();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("id.thearka.appelperempuan", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("email", null);
        editor.putString("password", null);

        editor.apply();

        Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
        intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentLogout);
    }

    private void setCurrentUser() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        nav_nama = hView.findViewById(R.id.nav_header_nama);
        TextView nav_email = hView.findViewById(R.id.nav_header_email);
        userInfo.child(userID).child("nama").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nav_nama.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        nav_email.setText(firebaseUser.getEmail());
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
