package neeedo.imimaprx.htw.de.neeedo.utils;

public class GeoLocationUtils {
    public static int convertCommaToFullIntCoord(double doubleCoord) {
        return (int) (doubleCoord * 1E6); //shifts comma 6 figures to the left
    }
}
