package info.androidhive.firebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.util.List;

public class MypostActivity extends AppCompatActivity {

    ListView listViewPost;
    List<InfoUser> infoUserList;
    DatabaseReference db;
    DatabaseReference dbDes;
    DatabaseReference dbConnect,cidDB;
    String uid;
    private FirebaseAuth auth;
    TextView startText;
    TextView desText;
    TextView timeText;
    public static final String ID = "id";
    ImageView picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);

        auth = FirebaseAuth.getInstance();
        listViewPost = (ListView) findViewById(R.id.listViewPost);
        infoUserList = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("ReceiverID");
        dbDes = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid);
        dbConnect = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("ConnectedID");
        cidDB = FirebaseDatabase.getInstance().getReference("USER");
        startText = (TextView) findViewById(R.id.startText);
        desText = (TextView) findViewById(R.id.desText);
        timeText = (TextView) findViewById(R.id.desText);
        picture = (ImageView) findViewById(R.id.imageView6);
        final StorageReference myPicStoreage2 = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
        myPicStoreage2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(picture) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        picture.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
        listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final InfoUser infoUser = infoUserList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(MypostActivity.this);
                alert.setMessage("Confirm")
                     .setCancelable(false)
                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dbDes.child("connect").setValue(true);
                             Intent intent = new Intent(MypostActivity.this, DriverConnected.class);
                             intent.putExtra(ID , infoUser.getId());
                             String id = infoUser.getId();
                             db.removeValue();
                             dbConnect.setValue(id);
                             cidDB.child(id).child("ConnectID").child("CID").setValue(uid);
                             startActivity(intent);
                             finish();


                         }
                     })
                     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                infoUserList.clear();
                for (DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
                    InfoUser infoUser = desSnapshot.getValue(InfoUser.class);
                    infoUserList.add(infoUser);

                }

                ReceiverList adapter = new ReceiverList(MypostActivity.this, infoUserList);
                listViewPost.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbDes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
                try {
                    startText.setText(desInfo.getStart().toString().trim());
                    desText.setText(desInfo.getDestination().toString().trim());
                    timeText.setText(desInfo.getTime().toString().trim());
                } catch(Exception e) {
                    startText.setText("Not Found!!");
                    desText.setText("Not Found!!");
                    timeText.setText("Not Found!!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
