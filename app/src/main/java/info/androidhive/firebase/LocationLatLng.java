package info.androidhive.firebase;

/**
 * Created by KITTAPORN on 9/1/2017.
 */

public class LocationLatLng {
    Double lng;
    Double lat;

    LocationLatLng() {

    }

    public LocationLatLng(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public Double getLat() {
        return lat;
    }
}
