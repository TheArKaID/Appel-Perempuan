package id.thearka.appelperempuan;

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

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private final static int MAX_VOLUME = 1000;
    private final String TAG = MainActivity.class.getSimpleName();
    GoogleMap mMap;
    Location lastLocation;
    Marker currentLocationMarker;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
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

        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        final int originalVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        statusRequest = false;
        CardView cvHelpRequest = findViewById(R.id.cvHelp);
        cvHelpRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastLocation != null) {
                    if (!statusRequest) {
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                        mPlayer.prepareAsync();
                        requestHelp();
                        statusRequest = true;
                        mPlayer.start();
                        mPlayer.setLooping(true);
                    } else {
                        if (mPlayer.isPlaying() || isReady) {
                            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                            mPlayer.stop();
                            isReady = false;
                            cancellHelps();
                            statusRequest = false;
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Sedang Menemukan Lokasi Anda. Pastikan GPS Aktif!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);
        initPlayer();
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
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == Objects.requireNonNull(navigationView.getCheckedItem()).getItemId()) {
            return true;
        }
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_setting) {
            //Setting
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit) {
            confirmOut("Keluar", "Anda akan keluar tanpa Log Out");
        } else if (id == R.id.nav_logout) {
            confirmOut("Logout", "Anda akan Log Out.");
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        mMap.setMyLocationEnabled(true);
        LatLng latLng = new LatLng(-7.8086832, 110.3189663);

        buildGoogleApiClient();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "onConnectionFail " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        if (currentLocationMarker != null)
            currentLocationMarker.remove();

        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Posisi Anda");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        currentLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, mMap.getCameraPosition().zoom));

        findHelplessPeople();
    }

    private void findHelplessPeople() {

        GeoFire fire = new GeoFire(user);
        GeoQuery geoQuery = fire.queryAtLocation(new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), 1);
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                Location targetLocation = new Location("");
                targetLocation.setLatitude(location.latitude);
                targetLocation.setLongitude(location.longitude);
                float distance = lastLocation.distanceTo(targetLocation);
                final String[] jarak = Float.toString(distance).split("\\.");
                final LatLng latLng = new LatLng(location.latitude, location.longitude);

                userInfo.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        markerOptions.position(latLng);
                        markerOptions.title(dataSnapshot.child("nama").getValue() + "("
                                + dataSnapshot.child("verification").getValue() + ")"
                                + ". Jarak = " + jarak[0] + " Meter");
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                        currentLocationMarker = mMap.addMarker(markerOptions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onKeyExited(String key) {
                currentLocationMarker.remove();
                mMap.clear();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                currentLocationMarker.remove();
                LatLng latLng = new LatLng(location.latitude, location.longitude);
                markerOptions.position(latLng);
                markerOptions.title(key);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                currentLocationMarker = mMap.addMarker(markerOptions);
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    void requestHelp() {
        GeoFire fire = new GeoFire(user);
        fire.setLocation(userID, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Help Requested", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cancellHelps() {
        GeoFire fire = new GeoFire(user);
        fire.removeLocation(userID,
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        Toast.makeText(MainActivity.this, "Help Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void initPlayer() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.alert2);
        try {
            mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isReady = true;
                mPlayer.start();
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
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
