package fr.wildcodeschool.culture;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1550;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted : ask for it
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Permission granted
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                } else {
                    // Permission denied
                }
                break;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        else{
            mMap.setMyLocationEnabled(true);
        }
        String json = null;

        try {
            InputStream is = getAssets().open("Toulouse-musees.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            JSONArray root = new JSONArray(json);
            for (int i = 0; i < root.length(); i++) {
                JSONObject rooter = (JSONObject) root.get(i);
                JSONObject fields = rooter.getJSONObject("fields");
                for (int b = 0; b < fields.length(); b++){
                    JSONArray geolocalisation =(JSONArray) fields.get("geo_point_2d");
                    String name = (String) fields.get("eq_nom_equipement");
                    Double latitude = (Double) geolocalisation.get(0);
                    Double longitude = (Double) geolocalisation.get(1);

                    LatLng musée = new LatLng(latitude,longitude);

                    mMap.addMarker(new MarkerOptions().position(musée).title(name));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LatLng toulouse = new LatLng(43.604, 1.443);
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toulouse, zoomLevel));
    }
}

