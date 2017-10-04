package info.androidhive.firebase;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static info.androidhive.firebase.R.id.image;
import static info.androidhive.firebase.R.id.imageView;
import static info.androidhive.firebase.R.id.imageView5;
import static java.security.AccessController.getContext;

public class NewBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference db;
    DatabaseReference dbforinfo;
    DatabaseReference dbConnect;
    DatabaseReference dbToken;
    ListView listViewDestination;
    List<DesInfo> desList;
    private FirebaseAuth auth;
    String uid;
    ImageView status;
    public Uri filePath;
    public static final String passengerID = "id";
    private static final int PICK_IMAGE_REQUEST = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("Destination Data");
        dbConnect = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("ConnectID");
        dbforinfo = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");
        dbToken = FirebaseDatabase.getInstance().getReference("USER").child(uid);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
        ////////////////////FCM start /////////////////////////////////
        dbToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbToken.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //////////////////////////////////////////////////////////////
        listViewDestination = (ListView) findViewById(R.id.listViewDestination);
        desList = new ArrayList<>();
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        dbforinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String driver = infoUser.getTypePassDriv();
                if(driver.equals("Driver")){
                    fab.setVisibility(View.INVISIBLE);
                    NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView2.getHeaderView(0);
                    status = (ImageView) headerView.findViewById(imageView5);
                    status.setImageResource(R.mipmap.ic_driver);
                    Intent intent = new Intent(NewBoard.this, DriverService.class);
                    startService(intent);
                }else{
                    NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
                    View headerView = navigationView2.getHeaderView(0);
                    status = (ImageView) headerView.findViewById(imageView5);
                    status.setImageResource(R.mipmap.ic_passenger);
                    Intent i = new Intent(NewBoard.this, MyService.class);
                    startService(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    DesInfo valueC = dataSnapshot.getValue(DesInfo.class);
//                    if (valueC.isConnect() == true) {
//                        startService(new Intent(NewBoard.this, ServiceLocation.class));
//                    }
//                }catch (Exception e){
//                    Toast.makeText(NewBoard.this ,"service not started" , Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //////////////////////////////////////////////////////////////////
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //Toast.makeText(BoardActivity.this , gender , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(NewBoard.this, DestinationActivity.class);
                startActivity(intent);
            }
        });
        ///////////////////get user real name from server////////////////////
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView2.getHeaderView(0);
                final ImageView drawerImage = (ImageView) headerView.findViewById(imageView);
                Glide.with(getApplicationContext()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(drawerImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        drawerImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
        //////////////SET NAV HEADER TO USER NAME AND STUDENT ID/////////////////////
        dbforinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView2.getHeaderView(0);
                ImageView drawerImage = (ImageView) headerView.findViewById(R.id.imageView);
                drawerImage.setOnClickListener(new View.OnClickListener(){
                                                   @Override
                                                   public void onClick(View view) {
                                                       showFileChooser();

                                                   }

                                               });
                TextView drawerstdID = (TextView) headerView.findViewById(R.id.IDText);
                TextView drawerUsername = (TextView) headerView.findViewById(R.id.fullname);
                InfoUser user = dataSnapshot.getValue(InfoUser.class);
                final String name = user.getName();
                final String lastname = user.getLastName();
                String fname = name + "  " + lastname;
                drawerUsername.setText(fname);
                drawerstdID.setText(user.getStudentID());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //throw databaseError.toException();
            }
        });
        listViewDestination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DesInfo desInfo = desList.get(position);
                if(desInfo.getId().toString() == uid){
                    Snackbar.make(findViewById(android.R.id.content), Html.fromHtml("<font color=\"#CB4335\"><font size=\"7\">It's You!</font size></font>"), Snackbar.LENGTH_LONG)
                            //.setAction("Undo", mOnClickListener)
                            .setActionTextColor(Color.RED)
                            .show();
                }else {
                    Intent intent = new Intent(NewBoard.this, ConnectPassenger.class);
                    intent.putExtra(passengerID, desInfo.getId());
                    startActivity(intent);
                }
            }
        });

        //Toast.makeText(NewBoard.this, "5555", Toast.LENGTH_LONG).show();
        //////////////////////////////////////////////////////////
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            AlertDialog.Builder alertExit = new AlertDialog.Builder(NewBoard.this);
            alertExit.setMessage("Are you sure to close it?")
                    .setCancelable(false)
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertExit.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Toast.makeText(getApplicationContext(), "MyPost", Toast.LENGTH_LONG).show();
       getMenuInflater().inflate(R.menu.new_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_driver) {
//            Intent intent = new Intent(NewBoard.this, DriverConnected.class);
//            startActivity(intent);
//            //finish();
//        }
        if(id == R.id.action_favorite){
            dbforinfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot type) {

                    db.child(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot passConnect) {

                            dbConnect.child("CID").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot drivConnect) {
                                    DesInfo desInfo = passConnect.getValue(DesInfo.class);
                                    InfoUser infoUser = type.getValue(InfoUser.class);
                                    String connect = drivConnect.getValue(String.class);
                                    if(infoUser.getTypePassDriv().equals("Driver") && drivConnect != null) {
                                        Intent intent = new Intent(NewBoard.this, DriverConnected.class);
                                        startActivity(intent);
                                    } else if(infoUser.getTypePassDriv().equals("Passenger") && desInfo.isConnect() == false) {
                                        Intent intent = new Intent(NewBoard.this, MypostActivity.class);
                                        startActivity(intent);
                                    } else if(infoUser.getTypePassDriv().equals("Passenger") && desInfo.isConnect() == true) {
                                        Intent intent = new Intent(NewBoard.this, Connected.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(NewBoard.this , "ํYou don't have a passenger yet" , Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



//            db.child(uid).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
//                    try {
//                        if (desInfo.isConnect() == false) {
//                            Intent intent = new Intent(NewBoard.this, MypostActivity.class);
//                            startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(NewBoard.this, Connected.class);
//                            startActivity(intent);
//                        }
//                    } catch(Exception exception) {
//                        AlertDialog.Builder alert = new AlertDialog.Builder(NewBoard.this);
//                        alert.setMessage("You don't have posted")
//                                .setCancelable(false)
//                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.cancel();
//                                    }
//                                });
//                        AlertDialog alertDialog = alert.create();
//                        alertDialog.show();
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent2 = new Intent(NewBoard.this, MapsActivityDes.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);
        auth = FirebaseAuth.getInstance();
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {

            auth.signOut();
            Intent intent = new Intent(NewBoard.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
// else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //imageView.setImageBitmap(bitmap);
                CropImage.activity(filePath)
                        .setAspectRatio(1,1)
                        .start(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filePath = result.getUri();
                uploadFile();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            String fileName = uid+"_PIC";
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference("USERPICTURE").child(fileName);
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    NavigationView navigationView2 = (NavigationView) findViewById(R.id.nav_view);
                                    View headerView = navigationView2.getHeaderView(0);
                                    final ImageView drawerImage = (ImageView) headerView.findViewById(imageView);
                                    Glide.with(getApplicationContext()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(drawerImage) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            drawerImage.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });
                                }
                            });
                            //finish();
//                            overridePendingTransition(0, 0);
//                            startActivity(getIntent());
//                            overridePendingTransition(0, 0);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(getApplicationContext(), "Some thing Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        final ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(NewBoard.this, android.R.style.Theme_Material_Light_Dialog_MinWidth));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading");
        dialog.show();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                desList.clear();
                for (DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
                    DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
                        desList.add(desInfo);

                }

                DestinationList adapter = new DestinationList(NewBoard.this, desList);
                listViewDestination.setAdapter(adapter);
                dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}
