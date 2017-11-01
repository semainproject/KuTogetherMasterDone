package info.androidhive.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static info.androidhive.firebase.R.id.imageView;

/**
 * Created by narathorn on 8/21/2017 AD.
 */

public class ReceiverList extends ArrayAdapter<InfoUser> {
    public Activity context;
    private List<InfoUser> infoUserList;
    public ReceiverList(Activity context , List<InfoUser> infoUserList) {
        super(context , R.layout.receiver_layout , infoUserList);
        this.context = context;
        this.infoUserList = infoUserList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.receiver_layout , null , true);
        TextView nameText = (TextView) listViewItem.findViewById(R.id.nameText);
        TextView genderText = (TextView) listViewItem.findViewById(R.id.genderText);
        TextView bikeBrandText = (TextView) listViewItem.findViewById(R.id.bikeBrandText);
        TextView bikeColorText = (TextView) listViewItem.findViewById(R.id.bikeColorText);
        TextView bikeIDText = (TextView) listViewItem.findViewById(R.id.bikeIDText);
        final TextView startText = (TextView) listViewItem.findViewById(R.id.startText);
        final ImageView imageViewRec = (ImageView) listViewItem.findViewById(R.id.imageViewRec);
        TextView like = (TextView) listViewItem.findViewById(R.id.like);
        TextView dislike = (TextView) listViewItem.findViewById(R.id.dislike);

        InfoUser infoUser = infoUserList.get(position);
        String id = infoUser.getId();
        int likeNumber = infoUser.getLike();
        int dislikeNumber = infoUser.getUnlike();

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("USERPICTURE").child(id+"_PIC");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageViewRec) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageViewRec.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }
        });

        nameText.setText(infoUser.getNickname());
        genderText.setText(infoUser.getGender());
        bikeBrandText.setText(infoUser.getBrand());
        bikeColorText.setText(infoUser.getColor());
        bikeIDText.setText(infoUser.getBikeID());
        like.setText(String.valueOf(likeNumber));
        dislike.setText(String.valueOf(dislikeNumber));
        return listViewItem;
    }
}
