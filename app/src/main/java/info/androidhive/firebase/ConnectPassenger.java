package info.androidhive.firebase;

import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class ConnectPassenger extends AppCompatActivity {

    TextView nameText;
    TextView nicknameText;
    Button connectBtn;
    DatabaseReference dbUser;
    DatabaseReference dbUpdate;
    DatabaseReference dbUserInfo;
    DatabaseReference dbDes;
    String uid;
    FirebaseAuth auth;
    ImageView userPIC;
    CheckBox maleCheck,femaleCheck;
    TextView startText;
    TextView desText;
    TextView timeText;
    TextView noteText;
//    Intent intent = getIntent();
//    final String id = intent.getStringExtra(NewBoard.passengerID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_passenger);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        maleCheck = (CheckBox) findViewById(R.id.maleCHK);
        femaleCheck = (CheckBox) findViewById(R.id.femaleCHK);
        startText = (TextView) findViewById(R.id.startText);
        desText = (TextView) findViewById(R.id.desText);
        timeText = (TextView) findViewById(R.id.timeText);
        noteText = (TextView) findViewById(R.id.noteText);
        connectBtn = (Button) findViewById(R.id.connectBtn);
        nameText = (TextView) findViewById(R.id.nameText);
        nicknameText = (TextView) findViewById(R.id.nicknameText);
        userPIC = (ImageView) findViewById(R.id.passengerPIC);
        connectBtn.setText("I'll take you");
        //genderText = (TextView) findViewById(R.id.genderText);
        Intent intent = getIntent();
        final String id = intent.getStringExtra(NewBoard.passengerID);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("USERPICTURE/").child(id+"_PIC");
        dbUser = FirebaseDatabase.getInstance().getReference("USER").child(id).child("INFORMATION");
        dbUserInfo = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");
        dbUpdate = FirebaseDatabase.getInstance().getReference("Destination Data").child(id).child("ReceiverID");
        dbDes = FirebaseDatabase.getInstance().getReference("Destination Data").child(id);
        dbUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String driver = infoUser.getTypePassDriv();
                if(driver.equals("Passenger")){
                    connectBtn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(userPIC) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        userPIC.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReceiver();
            }
        });


    }
    @Override
    public void onBackPressed() {
        finish();
    }

    private void setReceiver() {
        dbUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String name = infoUser.getNickname();
                String gender = infoUser.getGender();
                String bikeBrand = infoUser.getBrand();
                String bikeColor = infoUser.getColor();
                String bikeID = infoUser.getBikeID();
                int like = infoUser.getLike();
                int dislike = infoUser.getUnlike();
                InfoUser info = new InfoUser(name , gender , bikeBrand , bikeColor , bikeID , uid , like , dislike);
                dbUpdate.child(uid).setValue(info);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ConnectPassenger.this , "ERROR TO SEND INFORMATION" , Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onStart() {
       super.onStart();
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String name = infoUser.getName().toString().trim();
                String lastname = infoUser.getLastName().toString();
                String nickname = infoUser.getNickname().toString().trim();
                //String picURL = infoGetter.get
                String gender = infoUser.getGender().toString().trim();
                if(gender.equals("Male")){
                    maleCheck.setChecked(true);
                }else{
                    femaleCheck.setChecked(true);
                }
                nameText.setText(name+"  "+lastname);
                nicknameText.setText(nickname);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbDes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
                    String start = desInfo.getStart().toString().trim();
                    String des = desInfo.getDestination().toString().trim();
                    String time = desInfo.getTime().toString().trim();
                    String note = desInfo.getNote().toString().trim();
                    startText.setText(start);
                    desText.setText(des);
                    timeText.setText(time);
                    noteText.setText("*" + note);
                } catch(Exception e) {
                    startText.setText("Not Found");
                    desText.setText("Not Found");
                    timeText.setText("Not Found");
                    noteText.setText("Not Found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
