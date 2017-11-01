package info.androidhive.firebase;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivityDes extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_des);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = FirebaseDatabase.getInstance().getReference("Marker");
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
      LatLng canteen = new LatLng(13.117676, 100.920749);
//        LatLng apartment = new LatLng(13.116652, 100.921605);
//        LatLng building17 = new LatLng(13.119157, 100.919828);
//        LatLng building10 = new LatLng(13.118338, 100.920140);
//        LatLng coffeeShop = new LatLng(13.118467, 100.920914);
//        LatLng building15 = new LatLng(13.120001, 100.920164);
//        LatLng building2 = new LatLng(13.120800, 100.920145);
//        LatLng building1 = new LatLng(13.120923, 100.918973);
//        LatLng library = new LatLng(13.122025, 100.919112);
//        LatLng building13 = new LatLng(13.123723, 100.918342);
//        LatLng building18 = new LatLng(13.124760, 100.919645);
//        LatLng gate2 = new LatLng(13.119648, 100.917346);
//        LatLng gate1 = new LatLng(13.124127, 100.917725);
//        mMap.addMarker(new MarkerOptions().position(building10).title("เลือก อาคาร10"));
//        mMap.addMarker(new MarkerOptions().position(coffeeShop).title("เลือก กาแฟมวลชล"));
//        mMap.addMarker(new MarkerOptions().position(building15).title("เลือก อาคาร15"));
//        mMap.addMarker(new MarkerOptions().position(building2).title("เลือก อาคาร2"));
//        mMap.addMarker(new MarkerOptions().position(building1).title("เลือก อาคาร1"));
//        mMap.addMarker(new MarkerOptions().position(library).title("เลือก หอสมุด"));
//        mMap.addMarker(new MarkerOptions().position(building13).title("เลือก อาคาร13 อาคารพละ"));
//        mMap.addMarker(new MarkerOptions().position(building18).title("เลือก อาคาร18"));
//        mMap.addMarker(new MarkerOptions().position(gate1).title("เลือก ประตู1"));
//        mMap.addMarker(new MarkerOptions().position(gate2).title("เลือก ประตู2"));
//        mMap.addMarker(new MarkerOptions().position(canteen).title("เลือก โรงอาหาร"));
//        mMap.addMarker(new MarkerOptions().position(apartment).title("เลือก หน้าหอพักนิสิต"));
//        mMap.addMarker(new MarkerOptions().position(building17).title("เลือก อาคารเรียนรวม อาคาร17"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(canteen, 17));
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
                    MapMarkerGetter mapMark = desSnapshot.getValue(MapMarkerGetter.class);
                    LatLng locationFromdatabase = new LatLng(mapMark.getLat(), mapMark.getLng());
                    mMap.addMarker(new MarkerOptions().position(locationFromdatabase).title(mapMark.getName()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                        String message = arg0.getTitle();
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE",message);
                        setResult(2,intent);
                        finish();
                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
