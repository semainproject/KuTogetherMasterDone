package info.androidhive.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DriverConnected extends AppCompatActivity {
    ImageView myPic,passPic;
    FloatingActionButton mapBtn , finishBtn , cancelBtn;
    DatabaseReference db , dbDelDes , dbDelConnectID , dbDelLocation , dbUser , dbDes , dbLog;
    String uid;
    public void setVal(final String id){
        final StorageReference myPicStoreage2 = FirebaseStorage.getInstance().getReference("USERPICTURE").child(id+"_PIC");
        myPicStoreage2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(passPic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        passPic.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverConnected.this, ConnectedMap.class);
                intent.putExtra("partnerid",id);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_connected);
        myPic = (ImageView) findViewById(R.id.driverPic);
        passPic = (ImageView) findViewById(R.id.passPic);
        mapBtn = (FloatingActionButton) findViewById(R.id.mapBtn);
        finishBtn = (FloatingActionButton) findViewById(R.id.finishBtn);
        cancelBtn = (FloatingActionButton) findViewById(R.id.cancelBtn);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("ConnectID");
        dbDelConnectID = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("ConnectID");
        dbDelDes = FirebaseDatabase.getInstance().getReference("Destination Data");
        dbDelLocation = FirebaseDatabase.getInstance().getReference("Location");
        dbUser = FirebaseDatabase.getInstance().getReference("USER").child(uid);
        dbDes = FirebaseDatabase.getInstance().getReference("Destination Data");
        dbLog = FirebaseDatabase.getInstance().getReference("Log").child(uid);
        final StorageReference myPicStoreage = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
        myPicStoreage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(myPic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        myPic.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });

//        dbUser.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
//                String type = infoUser.getTypePassDriv().toString();
//
//                //DRIVER PART//
//
//                if(type.equals("Driver")) {
//                    db.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            final String idd = dataSnapshot.getValue(String.class);
//                            setVal(idd);
//
//                            finishBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    deleteData(idd);
//                                    finish();
//                                }
//                            });
//
//                            cancelBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    deleteData(idd);
//                                    finish();
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                    //PASSENGER PART//
//
//                } else if(type.equals("Passenger")) {
//                    dbDes.child(uid).child("ConnectedID").addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                            final String id = dataSnapshot.getValue(String.class);
//                            setVal(id);
//
//                            finishBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    deleteData(id);
//                                    finish();
//                                }
//                            });
//
//                            cancelBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    deleteData(id);
//                                    finish();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });





        dbUser.child("INFORMATION").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String type = infoUser.getTypePassDriv().toString();
                Toast.makeText(DriverConnected.this , type , Toast.LENGTH_LONG).show();

                //DRIVER PART//

                if(type.equals("Driver")) {
                    db.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String idd = dataSnapshot.getValue(String.class);
                            Toast.makeText(DriverConnected.this,idd,Toast.LENGTH_LONG).show();
                            setVal(idd);

                            finishBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteData(idd);
                                    finish();
                                }
                            });

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteData(idd);
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Toast.makeText(DriverConnected.this,type,Toast.LENGTH_LONG).show();
                //PASSENGER PART
                    // finishBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    deleteData(idg);
//                                    finish();
//                                }
//                            });
//
//                            cancelBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    deleteData(idg);
//                                    finish();
//                                }
//                            });
                }else if(type.equals("Passenger")) {

                    Toast.makeText(DriverConnected.this,"gunsorry",Toast.LENGTH_LONG).show();
                    dbDes.child(uid).child("ConnectedID").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String id = dataSnapshot.getValue(String.class);
                            Toast.makeText(DriverConnected.this, id, Toast.LENGTH_SHORT).show();
                            setVal(id);

                             finishBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteData(id);
                                    finish();
                                }
                            });

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteData(id);
                                    finish();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void deleteData(final String id) {
        dbDelDes.child(id).removeValue();
        dbDelConnectID.removeValue();
        dbDelLocation.child(uid).removeValue();
        dbDelLocation.child(id).removeValue();
    }

    public void log(final String id) {
        dbDes.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
                final String start = desInfo.getStart().toString();
                final String des = desInfo.getDestination().toString();
                final String time = desInfo.getTime().toString();
                dbDes.child(uid).child("ConnectedID").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot connectedID) {
                        final String connectID = connectedID.getValue(String.class);
                        LogData logData = new LogData(start , des , time , connectID);
                        dbLog.setValue(logData);
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
}
