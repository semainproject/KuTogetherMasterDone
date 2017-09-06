package info.androidhive.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DriverConnected extends AppCompatActivity {
    ImageView myPic,passPic;
    FloatingActionButton mapBtn;
    DatabaseReference db,db2;
    public void setVal(final String id){
        final StorageReference myPicStoreage2 = FirebaseStorage.getInstance().getReference("USERPICTURE").child(id+"_PIC");
        myPicStoreage2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(passPic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        passPic.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverConnected.this, ConnectedMap.class);
                intent.putExtra("partnerid",id);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_connected);
        myPic = (ImageView) findViewById(R.id.driverPic);
        passPic = (ImageView) findViewById(R.id.passPic);
        mapBtn = (FloatingActionButton) findViewById(R.id.tripmap);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("ConnectID");
        final StorageReference myPicStoreage = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
        myPicStoreage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(myPic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        myPic.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String idd = dataSnapshot.getValue(String.class);
                setVal(idd);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
