package edu.miracostacollege.cs134.caffeinefinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.miracostacollege.cs134.caffeinefinder.model.DBHelper;
import edu.miracostacollege.cs134.caffeinefinder.model.Location;
import edu.miracostacollege.cs134.caffeinefinder.model.LocationListAdapter;

//DONE: (1) Add a new Google Maps Activity to the project (New -> Google -> Google Maps Activity)
//DONE: This will entail generating a Google Maps API Key and adding it to your
//DONE: google_maps_api.xml file (instructions in this file)

//DONE: (2) Open the activity_main.xml layout and add a <fragment> in the LinearLayout.
//DONE: The layout_height="0dp" and layout_weight="1" for both the <fragment> and <ListView>
//DONE: Note: the android:name of the map fragment is "com.google.android.gms.maps.SupportMapFragment"


//DONE: (3) Implement the OnMapReadyCallback interface for Google Maps
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DBHelper db;
    private List<Location> allLocationsList;
    private ListView locationsListView;
    private LocationListAdapter locationListAdapter;

    // Load a Google Map into our mapsFragment
    private GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importLocationsFromCSV("locations.csv");

        allLocationsList = db.getAllCaffeineLocations();
        locationsListView = findViewById(R.id.locationsListView);
        locationListAdapter = new LocationListAdapter(this, R.layout.location_list_item, allLocationsList);
        locationsListView.setAdapter(locationListAdapter);

        //DONE: (4) Load the support map fragment asynchronously
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapsFragment);
        mapFragment.getMapAsync(this);
    }

    // DONE: (5) Implement the onMapReady method, which will add a special "marker" for our usual location,
    // DONE: which is  33.190802, -117.301805  (OC4800 building)
    // DONE: Then add normal markers for all the caffeine locations from the allLocationsList.
    // DONE: Set the zoom level of the map to 15.0f
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Event: the map comes back from Google, what to do with it?
        // Center our location on OC 4800
        map = googleMap;
        LatLng oc4800 = new LatLng(33.190802, -117.301805);

        // 1) Place a "marker" there!
        map.addMarker(new MarkerOptions()
            .title("Where the magic happens!")
            .position(oc4800)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.my_marker)));

        // 2) Center our "camera position" on oc4800
        CameraPosition position = new CameraPosition.Builder().target(oc4800).zoom(15f).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
        map.moveCamera(update);

        // 3) Add normal markers for each of the caffeine locations
        for (Location location : allLocationsList) {
            map.addMarker(new MarkerOptions()
                .title(location.getName())
                .position(new LatLng(location.getLatitude(), location.getLongitude())));
        }
    }

    // DONE: (6) Create a viewLocationsDetails(View v) method to create a new Intent to the
    // DONE: CaffeineDetailsActivity class, sending it the selectedLocation the user picked
    // DONE: from the locationsListView
    public void viewLocationsDetails(View v) {
         Location selectedLocation = (Location) v.getTag();
         Intent detailsIntent = new Intent(this, CaffeineDetailsActivity.class);
         detailsIntent.putExtra("SelectedLocation", selectedLocation);
         startActivity(detailsIntent);
    }
}
