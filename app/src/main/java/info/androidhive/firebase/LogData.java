package info.androidhive.firebase;

/**
 * Created by narathorn on 10/10/2017 AD.
 */

public class LogData {
    String start;
    String des;
    String time;
    String id;

    public LogData() {

    }

    public LogData(String start, String des, String time, String id) {
        this.start = start;
        this.des = des;
        this.time = time;
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public String getDes() {
        return des;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
