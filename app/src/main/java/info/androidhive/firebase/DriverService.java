package info.androidhive.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverService extends Service {
    DatabaseReference db;
    public DriverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final Intent i = new Intent(DriverService.this, GpsService.class);
        Toast.makeText(getApplicationContext(), "service started", Toast.LENGTH_LONG).show();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("ConnectID");
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String driverID = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), "DataChange to :"+driverID, Toast.LENGTH_LONG).show();
                if(dataSnapshot.getKey().toString().equals("CID") ){
                    startService(i);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //DriverIDGetter idd = dataSnapshot.getValue(DriverIDGetter.class);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                stopService(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
