package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 9/4/2017.
 */

public class DriverIDGetter {
    String CID;
    public DriverIDGetter(){

    }
    public DriverIDGetter(String CID){
        this.CID = CID;
    }

    public String getId() {
        return CID;
    }
}
