package info.androidhive.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeDriverStatus extends AppCompatActivity {
    Button submit;
    EditText brand,color,plateID;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_driver_status);
        submit = (Button) findViewById(R.id.button3);
        brand = (EditText) findViewById(R.id.editText);
        color = (EditText) findViewById(R.id.editText2);
        plateID = (EditText) findViewById(R.id.editText3);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!brand.getText().equals("") &&!color.getText().equals("") && !plateID.getText().equals("")){
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            db.child("typePassDriv").setValue("Driver");
                            db.child("bikeID").setValue(plateID.getText().toString());
                            db.child("brand").setValue(brand.getText().toString());
                            db.child("color").setValue(color.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    finish();
                }else{
                    Toast.makeText(ChangeDriverStatus.this,"Please fill your information", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
