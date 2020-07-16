package edu.miracostacollege.cs134.caffeinefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.miracostacollege.cs134.caffeinefinder.model.Location;

public class CaffeineDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = CaffeineDetailsActivity.class.getSimpleName();

    private TextView locationNameTextView;
    private TextView addressTextView;
    private TextView cityStateZipTextView;
    private TextView phoneTextView;
    private TextView latLongTextView;
    private Location selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caffeine_details);

        // Load the support map fragment asynchronously
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsFragment);
        mapFragment.getMapAsync(this);

        locationNameTextView = findViewById(R.id.locationNameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        cityStateZipTextView = findViewById(R.id.cityStateZipTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        latLongTextView = findViewById(R.id.latLongTextView);

        Intent detailsIntent = getIntent();

        // Extract the selectedGame object from the Intent
        selectedLocation = detailsIntent.getParcelableExtra("SelectedLocation");

        locationNameTextView.setText(selectedLocation.getName());
        addressTextView.setText(selectedLocation.getAddress());
        cityStateZipTextView.setText(
                String.format("%s, %s %s",
                        selectedLocation.getCity(),
                        selectedLocation.getState(),
                        selectedLocation.getZipCode()));
        phoneTextView.setText(selectedLocation.getPhone());
        latLongTextView.setText(
                String.format("%s %s",
                        String.valueOf(selectedLocation.getLatitude()),
                        String.valueOf(selectedLocation.getLongitude())));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng oc4800 = new LatLng(33.190802, -117.301805);

        // 1) Place a "marker" there!
        googleMap.addMarker(new MarkerOptions()
                .title("Where the magic happens!")
                .position(oc4800)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));

        LatLng selectedLatLong =
                new LatLng(selectedLocation.getLatitude(), selectedLocation.getLongitude());

        // 2) Add normal markers for each of the caffeine locations
        googleMap.addMarker(new MarkerOptions()
                .title(selectedLocation.getName())
                .position(selectedLatLong));

        // 3) Center our "camera position" on selectedLocation
        CameraPosition position =
                new CameraPosition.Builder().target(selectedLatLong).zoom(12f).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
        googleMap.moveCamera(update);
    }
}
