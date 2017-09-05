//package info.androidhive.firebase;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Adapter;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BoardActivity extends AppCompatActivity {
//
//    DatabaseReference dbUser;
//    DatabaseReference dbDes;
//    ListView listViewDestination;
//    List<DesInfo> desList;
//    Button addBtn;
//    Button signoutBtn;
//
//    private FirebaseAuth auth;
//    FirebaseUser user;
//    String uid;
//
//    public String gender;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_board);
//        signoutBtn = (Button) findViewById(R.id.button2);
//        auth = FirebaseAuth.getInstance();
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
//
//        dbUser = FirebaseDatabase.getInstance().getReference("USER").child(uid).child("INFORMATION");
//        dbDes = FirebaseDatabase.getInstance().getReference("Destination Data");
//
//        addBtn = (Button) findViewById(R.id.addBtn);
//        listViewDestination = (ListView) findViewById(R.id.listViewDestination);
//        desList = new ArrayList<>();
//
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(BoardActivity.this , gender , Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(BoardActivity.this, DestinationActivity.class);
//                startActivity(intent);
//            }
//        });
//        signoutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                auth.signOut();
//                Intent intent = new Intent(BoardActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
////        db.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////
////                desList.clear();
////                for(DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
////                    DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
////                    desList.add(desInfo);
////
////                }
////
////                DestinationList adapter = new DestinationList(BoardActivity.this , desList);
////                listViewDestination.setAdapter(adapter);
////
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
//
////        db.orderByChild("time").startAt("10.00").endAt("13.00").addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                desList.clear();
////                for(DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
////                    DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
////                    desList.add(desInfo);
////
////                }
////
////                DestinationList adapter = new DestinationList(BoardActivity.this , desList);
////                listViewDestination.setAdapter(adapter);
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });
//
//        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                InfoGetter user = dataSnapshot.getValue(InfoGetter.class);
//                String gender2 = user.getGender().toString().trim();
//                if(gender2 == "Male") {
//                    isMale();
//                } else {
//                    isFemale();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if(gender == "Male") {
//            addBtn.setVisibility(View.INVISIBLE);
//            dbDes.orderByChild("gender").equalTo("Male").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    desList.clear();
//                    for(DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
//                        DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
//                        desList.add(desInfo);
//                    }
//                    DestinationList adapter = new DestinationList(BoardActivity.this , desList);
//                    listViewDestination.setAdapter(adapter);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        } else {
//            Toast.makeText(BoardActivity.this , gender , Toast.LENGTH_LONG).show();
//        }
//
//        if(gender == "Female") {
//            dbDes.orderByChild("gender").equalTo("Female").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    desList.clear();
//                    for(DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
//                        DesInfo desInfo = desSnapshot.getValue(DesInfo.class);
//                        desList.add(desInfo);
//                    }
//                    DestinationList adapter = new DestinationList(BoardActivity.this , desList);
//                    listViewDestination.setAdapter(adapter);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//
//    }
//    public void isMale() {
//        gender = "Male";
//    }
//    public void isFemale() {
//        gender = "Female";
//    }
//}
//
