package info.androidhive.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static info.androidhive.firebase.R.id.imageView;
import static info.androidhive.firebase.R.id.imageView5;
import static info.androidhive.firebase.R.id.mypic;

public class Connected extends AppCompatActivity {
    TextView partnerNameText;
    ImageView yourImage,partnerImage;
    DatabaseReference dbforinfo;
    DatabaseReference db;
    String partnerID;
    Button driverLocation;
    public void setVal(String id){
        dbforinfo = FirebaseDatabase.getInstance().getReference("USER").child(id).child("INFORMATION");
        final StorageReference partnerPicStoreage = FirebaseStorage.getInstance().getReference("USERPICTURE").child(id+"_PIC");
        dbforinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                partnerNameText.setText(infoUser.getNickname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        partnerPicStoreage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(partnerImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        partnerImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);
        driverLocation = (Button) findViewById(R.id.driverLocation);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid);
        final StorageReference myPicStoreage = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
        //final StorageReference partnerPicStoreage = FirebaseStorage.getInstance().getReference("USERPICTURE").child(id+"_PIC");
        //dbforinfo = FirebaseDatabase.getInstance().getReference("USER").child(id).child("INFORMATION");
        ////////////////////////////////////////////////////// get partner id //////////////////////////
        partnerNameText = (TextView) findViewById(R.id.partnerName);
        yourImage = (ImageView) findViewById(R.id.mypic);
        partnerImage = (ImageView) findViewById(R.id.partnerpic);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConnectedIDGetter partner = dataSnapshot.getValue(ConnectedIDGetter.class);
                //partnerNameText.setText(partner.getConnectedID());
                setVal(partner.getConnectedID());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myPicStoreage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(yourImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        yourImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });

        driverLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ConnectedIDGetter partner = dataSnapshot.getValue(ConnectedIDGetter.class);
                        Intent intent = new Intent(Connected.this, ConnectedMap.class);
                        intent.putExtra("partnerid",partner.getConnectedID());
                        //intent.putExtra("",partner.getConnectedID());
                        startService(new Intent(Connected.this, ServiceLocation.class));
                        startActivity(intent);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Connected.this, NewBoard.class);
        startActivity(intent);
        finish();
    }
}
