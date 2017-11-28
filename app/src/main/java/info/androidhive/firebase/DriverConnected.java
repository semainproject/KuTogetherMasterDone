package info.androidhive.firebase;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Date;

public class DriverConnected extends AppCompatActivity {
    ImageView passPic;
    FloatingActionButton mapBtn , finishBtn , cancelBtn;
    DatabaseReference db , dbDelDes , dbDelConnectID , dbDelLocation , dbUser , dbDes , dbLog , dbRating , dbWait;
    TextView textView7,plateID,nickName,color,brand , drivText;
    String uid;
    View line;
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
        passPic = (ImageView) findViewById(R.id.passPic);
        mapBtn = (FloatingActionButton) findViewById(R.id.mapBtn);
        finishBtn = (FloatingActionButton) findViewById(R.id.finishBtn);
        cancelBtn = (FloatingActionButton) findViewById(R.id.cancelBtn);
        textView7 = (TextView) findViewById(R.id.textView7);
        plateID = (TextView) findViewById(R.id.plateID);
        nickName = (TextView) findViewById(R.id.nickName);
        color = (TextView) findViewById(R.id.color);
        brand = (TextView) findViewById(R.id.brand);
        drivText = (TextView) findViewById(R.id.textView14);
        line = (View) findViewById(R.id.view10);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("ConnectID");
        dbDelConnectID = FirebaseDatabase.getInstance().getReference("USER");
        dbDelDes = FirebaseDatabase.getInstance().getReference("Destination Data");
        dbDelLocation = FirebaseDatabase.getInstance().getReference("Location");
        dbUser = FirebaseDatabase.getInstance().getReference("USER").child(uid);
        dbDes = FirebaseDatabase.getInstance().getReference("Destination Data");
        dbLog = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("Log");
        dbRating = FirebaseDatabase.getInstance().getReference("USER");
        final StorageReference myPicStoreage = FirebaseStorage.getInstance().getReference("USERPICTURE").child(uid+"_PIC");
//        myPicStoreage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Glide.with(getApplicationContext()).load(uri).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(new BitmapImageViewTarget(myPic) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        myPic.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
//            }
//        });


        dbUser.child("INFORMATION").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                InfoUser infoUser = dataSnapshot.getValue(InfoUser.class);
                String type = infoUser.getTypePassDriv().toString();

                //DRIVER PART//

                if(type.equals("Driver")) {
                    drivText.setText("Passenger Information");

                    finishBtn.setVisibility(View.INVISIBLE);
                    textView7.setVisibility(View.INVISIBLE);
                    db.child("CID").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String id = dataSnapshot.getValue(String.class);
                            try {
                                dbDes.child(id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot desSnapshot) {
                                        boolean close = false;
                                        try {
                                            DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
                                            nickName.setText("Name : " + desInfo.getNickname());
                                            plateID.setText("From : " + desInfo.getStart());
                                            color.setText("To : " + desInfo.getDestination());
                                            brand.setText("Time : " + desInfo.getTime());
                                            close = true;
                                        } catch (Exception e) {

                                        }
                                        if (close == false) {
                                            finish();
                                            stopService(new Intent(DriverConnected.this, GpsService.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } catch(Exception ee) {
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    db.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final String idd = dataSnapshot.getValue(String.class);
                            //Toast.makeText(DriverConnected.this,idd,Toast.LENGTH_LONG).show();
                            setVal(idd);

//                            finishBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    logForDriv(idd);
//                                    deleteDataDriv(idd);
//                                    Intent intent = new Intent(DriverConnected.this, NewBoard.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            });

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDataDriv(idd);
                                    stopService(new Intent(DriverConnected.this, GpsService.class));
                                    stopService(new Intent(DriverConnected.this, ServiceLocation.class));
                                    Intent intent = new Intent(DriverConnected.this, NewBoard.class);
                                    startActivity(intent);
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
                }else if(type.equals("Passenger")) {
                    try {
                        dbDes.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot desSnapshot) {
                                boolean close = false;
                                try {
                                    DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
                                    if(desInfo != null){
                                        close = true;
                                        String pid = desInfo.getId();
                                    }
                                } catch (Exception e) {
                                    //finish();
                                }
                                if (close == false) {
                                    finish();
                                    stopService(new Intent(DriverConnected.this, GpsService.class));
                                    stopService(new Intent(DriverConnected.this, ServiceLocation.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                finish();
                            }
                        });
                    } catch(Exception ee) {
                        finish();
                    }
                    dbDes.child(uid).child("ConnectedID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final String id = dataSnapshot.getValue(String.class);
                            dbDelConnectID.child(id).child("INFORMATION").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    InfoUser info = dataSnapshot.getValue(InfoUser.class);
                                    plateID.setText("License plate : "+info.getBikeID());
                                    nickName.setText("Name : "+info.getName().toString());
                                    brand.setText("Bike Brand : "+info.getBrand());
                                    color.setText("Color : "+info.getColor());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //Toast.makeText(DriverConnected.this, id, Toast.LENGTH_SHORT).show();
                            setVal(id);
                            logForPass(id);
                            finishBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final Dialog dialog = new Dialog(DriverConnected.this);
                                    dialog.setContentView(R.layout.like_layout);
                                    dialog.show();

                                    Button likeButton = (Button) dialog.findViewById(R.id.likeBtn);
                                    likeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dbRating.child(id).child("INFORMATION").child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot likeSnapshot) {
                                                    int like = likeSnapshot.getValue(int.class);
                                                    like = like + 1;
                                                    dbRating.child(id).child("INFORMATION").child("like").setValue(like);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            deleteDataPass(id);
                                            stopService(new Intent(DriverConnected.this, GpsService.class));
                                            stopService(new Intent(DriverConnected.this, ServiceLocation.class));
                                            Intent intent = new Intent(DriverConnected.this, NewBoard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    Button dislikeButton = (Button) dialog.findViewById(R.id.dislikeBtn);
                                    dislikeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dbRating.child(id).child("INFORMATION").child("unlike").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dislikeSnapshot) {
                                                    int dislike = dislikeSnapshot.getValue(int.class);
                                                    dislike = dislike + 1;
                                                    dbRating.child(id).child("INFORMATION").child("unlike").setValue(dislike);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                            deleteDataPass(id);
                                            Intent intent = new Intent(DriverConnected.this, NewBoard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                }
                            });

                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteDataPass(id);
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

    public void deleteDataDriv(final String id) {
        dbDelDes.child(id).removeValue();
        dbDelConnectID.child(uid).child("ConnectID").removeValue();
        dbDelLocation.child(uid).removeValue();
        dbDelLocation.child(id).removeValue();
        dbDelConnectID.child(uid).child("Waiting").removeValue();
    }

    public void deleteDataPass(final String id) {
        dbDelDes.child(uid).removeValue();
        dbDelConnectID.child(id).child("ConnectID").removeValue();
        dbDelLocation.child(uid).removeValue();
        dbDelLocation.child(id).removeValue();
        dbDelConnectID.child(id).child("Waiting").removeValue();
    }

    public void logForPass(final String id) {
        dbDes.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
                try {
                    final String start = desInfo.getStart().toString();
                    final String des = desInfo.getDestination().toString();
                    final String time = desInfo.getTime().toString();
                    LogData logData = new LogData(start, des, time, id);
                    Date curDate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                    String dateToStr = format.format(curDate).toString();
                    dbLog.child(dateToStr).setValue(logData);
                } catch(Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void logForDriv(final String id) {
        dbDes.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
                try {
                    final String start = desInfo.getStart().toString();
                    final String des = desInfo.getDestination().toString();
                    final String time = desInfo.getTime().toString();
                    LogData logData = new LogData(start, des, time, uid);
                    Date curDate = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
                    String dateToStr = format.format(curDate).toString();
                    dbLog.child(dateToStr).setValue(logData);
                } catch(Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
