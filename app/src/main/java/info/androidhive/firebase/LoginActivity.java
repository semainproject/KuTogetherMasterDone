package info.androidhive.firebase;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.Manifest;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar,wprogress;
    private Button btnSignup, btnLogin, btnReset;
    DatabaseReference db;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Animation uptodown,downtoup;
    RelativeLayout re1_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance

        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.email);
        inputEmail.requestFocus();
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        re1_login = (RelativeLayout) findViewById(R.id.r_cloud);
        re1_login.setAnimation(uptodown);

        btnSignup.setAnimation(downtoup);
        btnLogin.setAnimation(downtoup);
        btnReset.setAnimation(downtoup);

        setSupportActionBar(toolbar);
        ConstraintLayout colayout = (ConstraintLayout) findViewById(R.id.conLayout);
        wprogress = (ProgressBar) findViewById(R.id.progressBarStart);
        LinearLayout layone= (LinearLayout) findViewById(R.id.lay);
        if(checkPermission()==false){
            requestPermission();
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            layone.setVisibility(View.INVISIBLE);
            colayout.setVisibility(View.VISIBLE);
            wprogress.setVisibility(View.VISIBLE);
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String uid = user.getUid();
            infocheck(uid);

        }
        //auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();
                btnLogin.setClickable(false);
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
//
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    progressBar.setVisibility(View.GONE);
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));

                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                    btnLogin.setClickable(true);
                                } else {
                                    btnLogin.setClickable(false);
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    final String uid = user.getUid();
                                    infocheck(uid);
//                                    Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                }
                            }
                        });
            }
        });
    }
    public void infocheck(String uid){
        db = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    InfoUser user = dataSnapshot.getValue(InfoUser.class);
                    String gg = user.getName();
                    Toast.makeText(LoginActivity.this, user.getName(), Toast.LENGTH_LONG).show();
                    if (user != null) {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, NewBoard.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                }
                catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Please insert your information", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, InfoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }


    private void requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){

            Toast.makeText(LoginActivity.this,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
            //ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        } else {

            ActivityCompat.requestPermissions(LoginActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Snackbar.make(view,"Permission Granted, Now you can access location data.",Snackbar.LENGTH_LONG).show();

                } else {

                    //Snackbar.make(view,"Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();
                    finish();

                }
                break;
        }
    }
}

