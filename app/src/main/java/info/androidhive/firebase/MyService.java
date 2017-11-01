package info.androidhive.firebase;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class MyService extends Service {
    public MyService() {
    }
    String uid;
    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(MyService.this, "service Start from myservice", Toast.LENGTH_SHORT).show();
        DatabaseReference firebaseRef,ref;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        firebaseRef = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("ReceiverID");
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("USER").child(uid);
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    InfoUser id = data.getValue(InfoUser.class);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Intent intentTomypost = new Intent(MyService.this,MypostActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intentTomypost, 0);
                    Notification notification =
                        new NotificationCompat.Builder(MyService.this) // this is context
                                .setSmallIcon(R.mipmap.ic_driver)
                                .setContentTitle("มีคนมารับจ้าา")
                                .setContentText(id.getNickname()+" กำลังมารับ")
                                .setSound(alarmSound)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .build();
                Random random = new Random();
                int randomNumber = random.nextInt(9999 - 1000) + 1000;
                if(id.getId() != null) {
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(randomNumber, notification);
                    break;
                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        ref = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("connect");
        final Intent i = new Intent(MyService.this, ServiceLocation.class);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Boolean.class) != null) {
                    Boolean status = dataSnapshot.getValue(Boolean.class);
                    if (status == true) {
                        Toast.makeText(MyService.this, "TRUE", Toast.LENGTH_SHORT).show();
                        startService(i);
                    } else {
                        Toast.makeText(MyService.this, "FALSE", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    stopService(new Intent(MyService.this, ServiceLocation.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "service Stopped", Toast.LENGTH_SHORT).show();
//        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
