package info.androidhive.firebase;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;

import static info.androidhive.firebase.R.id.imageView;

public class DestinationActivity extends AppCompatActivity {
    TextView location;
    TextView des;
    Button locationCompass;
    Button desCompass;
    Button timeBtn;
    EditText noteInput;
    Button submitBtn;
    DatabaseReference db;
    DatabaseReference dbUser;
    private FirebaseAuth auth;
    FirebaseUser user;
    String uid;
    String picURL;
    int PLACE_PICKER_REQUEST = 3;
    String locationPlace;
    String destinationPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);
        submitBtn = (Button) findViewById(R.id.submitButton);
        locationCompass = (Button) findViewById(R.id.lobtn);
        desCompass = (Button) findViewById(R.id.desbtn);
        location = (TextView) findViewById(R.id.start);
        timeBtn = (Button) findViewById(R.id.timeBtn);
        noteInput = (EditText) findViewById(R.id.noteTX);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
        db = FirebaseDatabase.getInstance().getReference("Destination Data");
        dbUser = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");
        locationCompass.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(DestinationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        desCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DestinationActivity.this,MapsActivityDes.class);
                startActivityForResult(intent, 2);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDesInfo();
            }
        });
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                picURL = uri.toString();
            }
        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(DestinationActivity.this, android.R.style.Theme_Holo_Light_Dialog , new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeBtn.setText(hourOfDay + "." + minute);
                    }
                }, hour , minute , true);
                timePickerDialog.show();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                Double lat = place.getLatLng().latitude;
                Double lng = place.getLatLng().longitude;
                String toastMsg = String.format("Place: %s", place.getName());
                locationPlace = lat+"/"+lng;
                List<Integer> ptype = place.getPlaceTypes();
                if(ptype.get(0) == 0){
                    //Toast.makeText(this, lat+"/"+lng, Toast.LENGTH_LONG).show();
                    locationCompass.setText("Mylocation");

                }else {
                    //Toast.makeText(this,toastMsg, Toast.LENGTH_LONG).show();
                    locationCompass.setText(toastMsg);
                }
            }

        }
        if(requestCode == 2 && data!=null){
            String message=data.getStringExtra("MESSAGE");
            //Toast.makeText(DestinationActivity.this, message, Toast.LENGTH_LONG).show();
            desCompass.setText(message);
            destinationPlace = message;

        }
    }



    private void addDesInfo() {
        final String start = locationCompass.getText().toString().trim();
        final String destination = desCompass.getText().toString().trim();
        final String time = timeBtn.getText().toString().trim();
        final String note = noteInput.getText().toString().trim();
        final String id = uid;
        final String url = picURL;
        final boolean connect = false;

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String nickname = infoUser.getNickname().toString().trim();
//                if( !start.equals("My Location") && !destination.equals("Go To")) {
                if(!start.equals("") && !destination.equals("") && !time.equals("")) {
                    DesInfo desInfo = new DesInfo(nickname , start , destination , time , note , id , url , connect);
                    db.child(uid).setValue(desInfo);
                    db.child(uid).child("ReceiverID");
                    //db.child(uid).setValue(ServerValue.TIMESTAMP);
                    finish();
                    Toast.makeText(DestinationActivity.this , "added" , Toast.LENGTH_LONG).show();
                }else{
                    Snackbar.make(findViewById(android.R.id.content), Html.fromHtml("<font color=\"#CB4335\"><font size=\"7\">Please Fill All Information</font size></font>"), Snackbar.LENGTH_LONG)
                            //.setAction("Undo", mOnClickListener)
                            .setActionTextColor(Color.RED)
                            .show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        if( (!TextUtils.isEmpty(start)) && (!TextUtils.isEmpty(time)) && (!TextUtils.isEmpty(destination))) {
//            DesInfo desInfo = new DesInfo(nickname , start , destination , time , note , id , url , connect);
//            db.child(uid).setValue(desInfo);
//            db.child(uid).child("ReceiverID");
//            //db.child(uid).setValue(ServerValue.TIMESTAMP);
//            Toast.makeText(this , "Added" , Toast.LENGTH_LONG).show();
//            finish();
//        }else{
//            Snackbar.make(findViewById(android.R.id.content), Html.fromHtml("<font color=\"#CB4335\"><font size=\"7\">Please Fill All Information</font size></font>"), Snackbar.LENGTH_LONG)
//                    //.setAction("Undo", mOnClickListener)
//                    .setActionTextColor(Color.RED)
//                    .show();
//        }
    }
}
