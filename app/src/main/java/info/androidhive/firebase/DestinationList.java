package info.androidhive.firebase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.List;

import static info.androidhive.firebase.R.id.imageView;

/**
 * Created by narathorn on 8/14/2017 AD.
 */

public class DestinationList extends ArrayAdapter<DesInfo>  {
    private Activity context;
    private List<DesInfo> desList;

    public DestinationList(Activity context , List<DesInfo> desList) {
        super(context , R.layout.board_layout , desList);
        this.context = context;
        this.desList = desList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.board_layout , null , true);
        TextView nickname = (TextView) listViewItem.findViewById(R.id.nickname);
        TextView start = (TextView) listViewItem.findViewById(R.id.start);
        TextView destination = (TextView) listViewItem.findViewById(R.id.destination);
        TextView time = (TextView) listViewItem.findViewById(R.id.time);
        final ImageView imageBoard = (ImageView) listViewItem.findViewById(R.id.imageBoard);

        DesInfo desInfo = desList.get(position);

        if(desInfo.getPicURL() != null) {
            Glide.with(context).load(desInfo.getPicURL()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageBoard) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    imageBoard.setImageDrawable(circularBitmapDrawable);
                }
            });
        }else{
            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/fir-auth-3fe01.appspot.com/o/USERPICTURE%2Fuserpic.png?alt=media&token=6a225213-9877-4af4-9b59-5bb0267d74c7").into(imageBoard);
        }

        nickname.setText(desInfo.getNickname());
        if(desInfo.getStart().toString().length() > 20){
            start.setText(desInfo.getStart().toString().substring(0,18)+"...");
        }else{
            start.setText(desInfo.getStart().toString());
        }
        switch(desInfo.getDestination().toString()){
            case "13.117676/100.920749":
                destination.setText("โรงอาหาร");
                break;
            case "13.116652/100.921605":
                destination.setText("หอพักนิสิต");
                break;
            case "13.119157/100.919828":
                destination.setText("ตึก17");
                break;
            default:
                destination.setText("ตึก17");
                break;
        }
//        destination.setText(desInfo.getDestination());
        time.setText(desInfo.getTime());
       // Glide.with(context).load(desInfo.getPicURL()).into(imageBoard);
        return listViewItem;
    }


}
