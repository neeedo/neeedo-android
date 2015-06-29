package neeedo.imimaprx.htw.de.neeedo;

import org.json.JSONObject;

public class FoundLocation {
    private double importance;
    private String name;
    private String latitude;
    private String longitude;

    public FoundLocation(JSONObject currentLocationObject) {
        try {
            this.latitude = currentLocationObject.getString("lat");
            this.longitude = currentLocationObject.getString("lon");
            this.name = currentLocationObject.getString("display_name");
            this.importance = currentLocationObject.getDouble("importance");
        } catch (Exception e) {

        }
    }
}
