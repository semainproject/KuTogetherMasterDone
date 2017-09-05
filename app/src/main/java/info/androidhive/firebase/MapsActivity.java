package info.androidhive.firebase;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.id.message;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        // Add a marker in Sydney and move the camera
        LatLng horBor = new LatLng(13.113976, 100.926320);
//        LatLng apartment = new LatLng(13.116652, 100.921605);
//        LatLng building17 = new LatLng(13.119157, 100.919828);
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(horBor).title("เลือก หอบริบูริณ์")).showInfoWindow();
//        mMap.addMarker(new MarkerOptions().position(apartment).title("เลือก หน้าหอพักนิสิต"));
//        mMap.addMarker(new MarkerOptions().position(building17).title("เลือก อาคารเรียนรวม อาคาร17"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(horBor, 17));

        mMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener(){
                    @Override
                    public void onInfoWindowClick(Marker arg0) {

                        arg0.hideInfoWindow();
                        double dlat =arg0.getPosition().latitude;
                        double dlon =arg0.getPosition().longitude;
                        String slat = String.valueOf(dlat);
                        String slon = String.valueOf(dlon);
                        //Toast.makeText(MapsActivity.this, slat, Toast.LENGTH_LONG).show();
                        String message = slat+"/"+slon;
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE",message);
                        setResult(2,intent);
                        finish();
                    }
                }
        );
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
