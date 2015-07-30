package neeedo.imimaprx.htw.de.neeedo.utils;

import java.util.ArrayList;

public class ProductUtils {

    public static String tagsToString(ArrayList<String> tags) {
        String returnString = "";
        int counter = 0;

        if (tags != null && tags.size() > 0) {
            for (String tag : tags) {
                if (counter == 0) {
                    returnString = tag;
                } else {
                    returnString = returnString + ", " + tag;
                }
                counter++;
            }
        }

        return returnString;
    }
}
