package info.androidhive.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editProfile extends AppCompatActivity {
    CheckBox male;
    CheckBox female;
    EditText name;
    EditText lastname;
    EditText studentID;
    Button submit;
    CheckBox Passenger_box;
    CheckBox Driver_box;
    EditText BikeID;
    EditText brand;
    EditText color;

    String uid,change_name,change_lastname,change_id;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    DatabaseReference infoDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        submit = (Button) findViewById(R.id.Submit);
        studentID = (EditText) findViewById(R.id.Stu_id);
        lastname = (EditText) findViewById(R.id.lastnametx);
        name = (EditText) findViewById(R.id.nameTx);
        female = (CheckBox) findViewById(R.id.female);
        male = (CheckBox) findViewById(R.id.male);
        Passenger_box = (CheckBox) findViewById(R.id.Passenger_box);
        Driver_box = (CheckBox) findViewById(R.id.Driver_box);
        BikeID = (EditText) findViewById(R.id.BikeID);
        brand = (EditText) findViewById(R.id.brand);
        color = (EditText) findViewById(R.id.color);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        infoDB = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");
        infoDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                name.setHint(infoUser.getName());
                lastname.setHint(infoUser.getLastName());
                studentID.setHint(infoUser.getStudentID());
                change_name = infoUser.getName();
                change_lastname = infoUser.getLastName();
                change_id = infoUser.getStudentID();
                if(infoUser.getGender().equals("Male")){
                    male.setChecked(true);
                }
                else{
                    female.setChecked(true);
                }

                if(infoUser.getTypePassDriv().equals("Passenger")){
                    Passenger_box.setChecked(true);
                    BikeID.setVisibility(View.INVISIBLE);
                    brand.setVisibility(View.INVISIBLE);
                    color.setVisibility(View.INVISIBLE);
                }
                else{
                    Driver_box.setChecked(true);
                    Passenger_box.setChecked(false);
                    BikeID.setVisibility(View.VISIBLE);
                    brand.setVisibility(View.VISIBLE);
                    color.setVisibility(View.VISIBLE);
                }
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name.getText().equals("") ){
                            infoDB.child("name").setValue(change_name.toString());
                        }
                        if(lastname.getText().equals("") ){
                            infoDB.child("lastName").setValue(change_lastname.toString());
                        }
                        if(studentID.getText().equals("") ){
                            infoDB.child("studentID").setValue(change_id.toString());
                        }
                        if(!name.getText().equals("") ){
                            infoDB.child("name").setValue(name.getText().toString());
                        }
                        if(!lastname.getText().equals("") ){
                            infoDB.child("lastName").setValue(lastname.getText().toString());
                        }
                        if(!studentID.getText().equals("") ){
                            infoDB.child("studentID").setValue(studentID.getText().toString());
                        }
                        if(male.isChecked()) {
                            infoDB.child("gender").setValue("Male");
                        }
                        if(female.isChecked()){
                            infoDB.child("gender").setValue("Female");
                        }
                        if(Driver_box.isChecked()){
                            infoDB.child("bikeID").setValue(BikeID.getText().toString());
                            infoDB.child("brand").setValue(brand.getText().toString());
                            infoDB.child("color").setValue(color.getText().toString());
                        }
                        if(Passenger_box.isChecked()){
                            infoDB.child("bikeID").setValue("");
                            infoDB.child("brand").setValue("");
                            infoDB.child("color").setValue("");
                        }
                    }



                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    female.setChecked(false);

                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    male.setChecked(false);

                }
            }
        });

        Passenger_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    Driver_box.setChecked(false);
                    BikeID.setVisibility(View.INVISIBLE);
                    brand.setVisibility(View.INVISIBLE);
                    color.setVisibility(View.INVISIBLE);
                }
            }
        });

        Driver_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    Passenger_box.setChecked(false);
                    BikeID.setVisibility(View.VISIBLE);
                    brand.setVisibility(View.VISIBLE);
                    color.setVisibility(View.VISIBLE);
                }

            }
        });





    }

}
