package neeedo.imimaprx.htw.de.neeedo.utils;

public class ServerConstantsUtils {
    private static final String OUTPAN_SERVER = "https://api.outpan.com/v1/products/";

    private static final String LOCALHOST = "http://10.0.2.2:9000/";
    private static final String LOCALHOST_SSL = "https://10.0.2.2:9443/";

    private static final String LIVE_SERVER = "http://api.neeedo.com/";
    private static final String LIVE_SERVER_SSL = "https://api.neeedo.com/";

    public static String getActiveServer() {
        return LOCALHOST_SSL;
    }

    public static String getOutpanServer() {
        return OUTPAN_SERVER;
    }
}
