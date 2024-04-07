package msu.edu.cse476.dhillo17.palatepal;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import msu.edu.cse476.dhillo17.palatepal.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng msu = new LatLng(42.7251, -84.4791);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(msu, 13.4F));

        LatLng akersHall = new LatLng(42.724339,-84.4673472);
        LatLng brodyHall = new LatLng(42.7315271,-84.4981952);
        LatLng caseHall = new LatLng(42.7244014,-84.4910327);
        LatLng holdenHall = new LatLng(42.7210965,-84.4911708);
        LatLng holmesHall = new LatLng(42.7266646,-84.4672193);
        LatLng landonHall = new LatLng(42.7338948,-84.4877161);
        LatLng owenHall = new LatLng(42.726141,-84.4733858);
        LatLng shawHall = new LatLng(42.7267227,-84.4778785);
        LatLng snyphiHall = new LatLng(42.7306801,-84.4755716);
        mMap.addMarker(new MarkerOptions().position(akersHall).title("Marker at Akers Hall!"));
        mMap.addMarker(new MarkerOptions().position(brodyHall).title("Marker at Broday Hall!"));
        mMap.addMarker(new MarkerOptions().position(caseHall).title("Marker at Case Hall!"));
        mMap.addMarker(new MarkerOptions().position(holdenHall).title("Marker at Holden Hall!"));
        mMap.addMarker(new MarkerOptions().position(holmesHall).title("Marker at Holmes Hall!"));
        mMap.addMarker(new MarkerOptions().position(landonHall).title("Marker at Landon Hall!"));
        mMap.addMarker(new MarkerOptions().position(owenHall).title("Marker at Owen Hall!"));
        mMap.addMarker(new MarkerOptions().position(shawHall).title("Marker at Shaw Hall!"));
        mMap.addMarker(new MarkerOptions().position(snyphiHall).title("Marker at Sny-Phi Hall!"));
    }
}