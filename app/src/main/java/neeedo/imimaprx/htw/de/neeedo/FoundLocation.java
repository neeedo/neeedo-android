package neeedo.imimaprx.htw.de.neeedo;

import org.json.JSONObject;

public class FoundLocation {
    private double importance;
    private String name;
    private double latitude;
    private double longitude;

    public FoundLocation(JSONObject currentLocationObject) {
        try {
            this.latitude = currentLocationObject.getDouble("lat");
            this.longitude = currentLocationObject.getDouble("lon");
            this.name = currentLocationObject.getString("display_name");
            this.importance = currentLocationObject.getDouble("importance");
        } catch (Exception e) {

        }
    }

    @Override
    public String toString() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
