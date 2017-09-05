package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 8/29/2017.
 */

public class ConnectedIDGetter {
    String ConnectedID;
    String nickname;
    String start;
    String destination;
    String time;
    String note;
    String id;
    String picURL;
    boolean connect;
    public ConnectedIDGetter(){

    }
    public ConnectedIDGetter(String ConnectedID,String nickname, String start, String destination, String time, String note, String id, String picURL, boolean connect){
        this.ConnectedID = ConnectedID;
        this.nickname = nickname;
        this.start = start;
        this.destination = destination;
        this.time = time;
        this.note = note;
        this.id = id;
        this.picURL = picURL;
        this.connect = connect;
    }

    public String getConnectedID() {
        return ConnectedID;
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
