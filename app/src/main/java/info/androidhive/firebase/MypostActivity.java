package info.androidhive.firebase;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MypostActivity extends AppCompatActivity {

    ListView listViewPost;
    List<InfoUser> infoUserList;
    DatabaseReference db;
    DatabaseReference dbDes;
    DatabaseReference dbConnect,cidDB;
    String uid;
    private FirebaseAuth auth;
    TextView startText;
    TextView desText;
    TextView timeText;
    public static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);

        auth = FirebaseAuth.getInstance();
        listViewPost = (ListView) findViewById(R.id.listViewPost);
        infoUserList = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        db = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("ReceiverID");
        dbDes = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid);
        dbConnect = FirebaseDatabase.getInstance().getReference("Destination Data").child(uid).child("ConnectedID");
        cidDB = FirebaseDatabase.getInstance().getReference("USER");
        startText = (TextView) findViewById(R.id.startText);
        desText = (TextView) findViewById(R.id.desText);
        timeText = (TextView) findViewById(R.id.desText);

        listViewPost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final InfoUser infoUser = infoUserList.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(MypostActivity.this);
                alert.setMessage("Confirm")
                     .setCancelable(false)
                     .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dbDes.child("connect").setValue(true);
                             Intent intent = new Intent(MypostActivity.this, Connected.class);
                             intent.putExtra(ID , infoUser.getId());
                             String id = infoUser.getId();
                             db.removeValue();
                             dbConnect.setValue(id);
                             cidDB.child(id).child("ConnectID").child("CID").setValue(uid);
                             startActivity(intent);
                             finish();


                         }
                     })
                     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();
                         }
                     });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                infoUserList.clear();
                for (DataSnapshot desSnapshot : dataSnapshot.getChildren()) {
                    InfoUser infoUser = desSnapshot.getValue(InfoUser.class);
                    infoUserList.add(infoUser);

                }

                ReceiverList adapter = new ReceiverList(MypostActivity.this, infoUserList);
                listViewPost.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbDes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DesInfo desInfo = dataSnapshot.getValue(DesInfo.class);
                startText.setText(desInfo.getStart().toString().trim());
                desText.setText(desInfo.getDestination().toString().trim());
                timeText.setText(desInfo.getTime().toString().trim());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
