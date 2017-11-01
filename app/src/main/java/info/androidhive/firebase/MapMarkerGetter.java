package info.androidhive.firebase;

/**
 * Created by Kittaporn on 11/1/2017.
 */

public class MapMarkerGetter {
    double lat;
    double lng;
    String name;

    public MapMarkerGetter(double lat, double lng, String name) {
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }

    public MapMarkerGetter() {
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }
}
