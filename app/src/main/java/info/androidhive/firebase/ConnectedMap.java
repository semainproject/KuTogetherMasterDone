package info.androidhive.firebase;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConnectedMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String serverKey = "AIzaSyBuRqc8Od_cWC5m1t7d4qL7EaBUx55WflY";
    LatLng origin = new LatLng(13.113237, 100.927201);
    LatLng des = new LatLng(13.121457, 100.919846);
    String partnerid;
    String uid;
    Marker marker;
    Marker myMarker;
    Marker friendMarker;
//    ArrayList<LatLng> latList = new ArrayList<>();
    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Location");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_map);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        Intent iin= getIntent();
        Bundle pid = iin.getExtras();
        partnerid = (String) pid.get("partnerid");
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

        LatLng kasert = new LatLng(13.121457, 100.919846);
        mMap.addMarker(new MarkerOptions().position(kasert).title("Marker in Kasert"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kasert));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(kasert, 17));
        //final Marker marker = mMap.addMarker(new MarkerOptions().position(kasert).title("your position"));
        //Toast.makeText(ConnectedMap.this, partnerid, Toast.LENGTH_LONG).show();
//        db.child(partnerid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                LocationLatLng ltlng = dataSnapshot.getValue(LocationLatLng.class);
//                LatLng location = new LatLng(ltlng.getLat(), ltlng.getLng());
//                latList.add(location);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        ////////////////////WARNING THIS WILL SPIN YOUR MIND //////////////////////////////////////////
        ///////////////////////CONFUSE IN MANY CONFUSE //////////////////////////////////////
        db.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot2) {
                //db.child()
                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Location").child(partnerid);
                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        LocationLatLng ltlng = dataSnapshot2.getValue(LocationLatLng.class);
                        LatLng location = new LatLng(ltlng.getLat(), ltlng.getLng());
                        LocationLatLng ltlng2 = dataSnapshot.getValue(LocationLatLng.class);
                        LatLng location2 = new LatLng(ltlng2.getLat(), ltlng2.getLng());
                        GoogleDirection.withServerKey(serverKey)
                                .from(location)
                                .to(location2)
                                .transportMode(TransportMode.DRIVING)
                                .execute(new DirectionCallback() {
                                    @Override
                                    public void onDirectionSuccess(Direction direction, String rawBody) {
                                        String status = direction.getStatus();
                                        if(direction.isOK()) {
                                            // Do something
                                            Route route = direction.getRouteList().get(0);
                                            Leg leg = route.getLegList().get(0);
                                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(ConnectedMap.this, directionPositionList, 5, Color.RED);
                                            mMap.addPolyline(polylineOptions);
                                        }else{
                                            Toast.makeText(ConnectedMap.this, status, Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onDirectionFailure(Throwable t) {
                                        Toast.makeText(ConnectedMap.this, "Something Wrong 2", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        db.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(marker != null){
                    marker.remove();
                }
                LocationLatLng ltlng = dataSnapshot.getValue(LocationLatLng.class);
                LatLng location = new LatLng(ltlng.getLat(), ltlng.getLng());
                MarkerOptions meMark =  new MarkerOptions().position(location).title("your position");
                marker = mMap.addMarker(meMark);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        db.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                friendMarker.remove();
//                LocationLatLng ltlng = dataSnapshot.getValue(LocationLatLng.class);
//                LatLng location = new LatLng(ltlng.getLat(), ltlng.getLng());
//                MarkerOptions fMark =  new MarkerOptions().position(location).title("friend position");
//                friendMarker = mMap.addMarker(fMark);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }
}