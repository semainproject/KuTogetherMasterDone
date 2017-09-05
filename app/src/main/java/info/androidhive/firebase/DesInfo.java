package info.androidhive.firebase;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import static info.androidhive.firebase.R.id.imageView;

/**
 * Created by narathorn on 8/9/2017 AD.
 */

public class DesInfo {

    String nickname;
    String start;
    String destination;
    String time;
    String note;
    String id;
    String picURL;
    boolean connect;
    //Map timestamp;
    public DesInfo() {

    }

    public DesInfo(String nickname, String start, String destination, String time, String note, String id, String picURL, boolean connect) {
        this.nickname = nickname;
        this.start = start;
        this.destination = destination;
        this.time = time;
        this.note = note;
        this.id = id;
        this.picURL = picURL;
        this.connect = connect;
    }

    public String getNickname() {
        return nickname;
    }

    public String getStart() {
        return start;
    }

    public String getDestination() {
        return destination;
    }

    public String getTime() {
        return time;
    }

    public String getNote() {
        return note;
    }

    public String getId() {
        return id;
    }

    public String getPicURL() {
        return picURL;
    }

    public boolean isConnect() {
        return connect;
    }


}
