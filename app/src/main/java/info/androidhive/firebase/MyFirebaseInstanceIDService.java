package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 10/4/2017.
 */

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    DatabaseReference dbConnect;
    public String currrentToken;

    @Override
    public void onCreate() {
        super.onCreate();
        currrentToken = FirebaseInstanceId.getInstance().getToken();
        //currentToken = FirebaseInstanceId.getInstance().getToken();
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        if(refreshedToken!=null) {
            sendRegistrationToServer(refreshedToken);
        }
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        dbConnect = FirebaseDatabase.getInstance().getReference("USER").child(uid);

        if(!currrentToken.equals(token)) {
            dbConnect.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dbConnect.child("FCMTOKEN").setValue(token);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        }else{
            return;
        }
    }
}