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
                        infoDB.child("gender").setValue("male");
                    }
                }
            });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    male.setChecked(false);
                    infoDB.child("gender").setValue("Female");
                }
            }
        });




    }

}
