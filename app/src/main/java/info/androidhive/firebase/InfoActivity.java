package info.androidhive.firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InfoActivity extends AppCompatActivity {
    CheckBox driverBtn;
    CheckBox passengerBtn;
    EditText username;
    EditText userlastname;
    EditText userstudentID;
    EditText userNickname;
    EditText bikecolor;
    EditText bikeid;
    EditText bikebrand;
    Button signOutButton;
    Button submitBtn;
    //ProgressBar progress2;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    public boolean driver=false;
    public boolean passenger = false;
    CheckBox malebtn;
    CheckBox femalebtn;
    String uid;
    DatabaseReference infoDB;
    public void isDriverCheck(){
        driver = true;
    }
    public void isPassengerCheck(){
        passenger = true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        //progress2 = (ProgressBar) findViewById(R.id.progressBar2);
        auth = FirebaseAuth.getInstance();
        submitBtn = (Button) findViewById(R.id.submitBtn);
        signOutButton = (Button) findViewById(R.id.signoutBtn);
        driverBtn = (CheckBox) findViewById(R.id.driverCheck);
        passengerBtn = (CheckBox) findViewById(R.id.passengerCheck);
        bikecolor = (EditText) findViewById(R.id.colorInput);
        bikeid = (EditText) findViewById(R.id.bikeIDinput);
        bikebrand = (EditText) findViewById(R.id.bikeBrandInput);
        malebtn = (CheckBox) findViewById(R.id.Malebtn);
        femalebtn = (CheckBox) findViewById(R.id.Femalebtn);
        username = (EditText) findViewById(R.id.nameInput);
        userlastname = (EditText) findViewById(R.id.lastNameInput);
        userstudentID = (EditText) findViewById(R.id.studentInput);
        userNickname = (EditText) findViewById(R.id.nickInput);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        infoDB = FirebaseDatabase.getInstance().getReference("USER");

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        //////////////////////////////////////////////////////////////////////////////////////
        driverBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    passengerBtn.setChecked(false);
                    bikebrand.setVisibility(View.VISIBLE);
                    bikeid.setVisibility(View.VISIBLE);
                    bikecolor.setVisibility(View.VISIBLE);
                    isDriverCheck();
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////
        passengerBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    driverBtn.setChecked(false);
                    bikebrand.setVisibility(View.INVISIBLE);
                    bikeid.setVisibility(View.INVISIBLE);
                    bikecolor.setVisibility(View.INVISIBLE);
                    isPassengerCheck();
                }
            }
        });
        //////////////////////////////////////////
        malebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    femalebtn.setChecked(false);
                }
            }
        });
        femalebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    malebtn.setChecked(false);
                }
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                if(!TextUtils.isEmpty(username.getText().toString())){
                    //Toast.makeText(InfoActivity.this,"corrcted", Toast.LENGTH_LONG).show();
                    if(!TextUtils.isEmpty(userlastname.getText().toString())){
                        if(!TextUtils.isEmpty(userstudentID.getText().toString())){
                            if(!TextUtils.isEmpty(userNickname.getText().toString())){
                                if(getBike() == "Passenger"){
                                    //progress2.setVisibility(View.VISIBLE);
                                    passDataWithDriverInformation(username.getText().toString(),userlastname.getText().toString(),userstudentID.getText().toString(),userNickname.getText().toString(),getGender(),getBike(),uid,"0","0","0",0,0);
                                    Intent i = new Intent(InfoActivity.this,NewBoard.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    if(!TextUtils.isEmpty(bikecolor.getText().toString())){
                                        if(!TextUtils.isEmpty(bikebrand.getText().toString())){
                                            if(!TextUtils.isEmpty(bikeid.getText().toString())){
                                                passDataWithDriverInformation(username.getText().toString(),userlastname.getText().toString(),userstudentID.getText().toString(),userNickname.getText().toString(),getGender(),getBike(),uid,bikebrand.getText().toString(),bikecolor.getText().toString(),bikeid.getText().toString(),0,0);
                                            }else{
                                                Toast.makeText(InfoActivity.this,"Please type bike ID", Toast.LENGTH_LONG).show();
                                            }
                                        }else{
                                            Toast.makeText(InfoActivity.this,"Please type bike brand", Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(InfoActivity.this,"Please type bike color", Toast.LENGTH_LONG).show();
                                    }
                                }
                                /////////////////////////////////////////////////////////////////////////////////
                                //passDataToPote(username.getText().toString(),userlastname.getText().toString(),userstudentID.getText().toString(),blankbox.getText().toString(),getGender(),getBike(),uid);
                            }else{
                                Toast.makeText(InfoActivity.this,"Please type your Nickname", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(InfoActivity.this,"Please type your StudentID", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(InfoActivity.this,"Please type your lastname", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(InfoActivity.this,"Please type your name", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void signOut() {
        auth.signOut();
        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public String getGender(){
        if(malebtn.isChecked()){
            return "Male";
        }else{
            return "Female";
        }
    }
    public String getBike(){
        if(driverBtn.isChecked()){
            return "Driver";
        }else{
            return "Passenger";
        }
    }

//    public void passDataToPote(String username,String userlastname,String userstudentID,String userNickname,String gender,String isdriver,String UID){
//        //////////////////////////////////////////////////////////////////
//        //SEND DATA HER
//        InfoUser infoUser = new InfoUser(username , userlastname , userNickname , userstudentID , gender , isdriver , uid);
//        try {
//            infoDB.child(UID).child("INFORMATION").setValue(infoUser);
//            Toast.makeText(InfoActivity.this, "Sucsess", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(InfoActivity.this, NewBoard.class);
//            startActivity(intent);
//            finish();
//        }catch (Exception e){
//            Toast.makeText(InfoActivity.this, "Unsucsess", Toast.LENGTH_LONG).show();
//        }
//       // progress2.setVisibility(View.INVISIBLE);
//
//        //Toast.makeText(InfoActivity.this,UID, Toast.LENGTH_LONG).show();
//        /////////////////////////////////////////////////////////////////
//    }
    public void passDataWithDriverInformation(String username,String userlastname,String userstudentID,String userNickname,String gender,String isdriver,String UID,String brand,String color,String bikeID,int like,int unlike){
        //////////////////////////////////////////////////
        InfoUser infoUser = new InfoUser(username , userlastname , userNickname , userstudentID , gender , isdriver , brand , color , bikeID , uid , like , unlike);
        infoDB.child(UID).child("INFORMATION").setValue(infoUser);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //auth.signOut();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        auth.signOut();
        finish();
    }
}
