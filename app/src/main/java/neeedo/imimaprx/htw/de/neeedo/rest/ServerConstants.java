package neeedo.imimaprx.htw.de.neeedo.rest;

public class ServerConstants {
    private static final String OUTPAN_SERVER = "https://api.outpan.com/v1/products/";

    private static final String LOCALHOST = "http://10.0.2.2:9000/";
    private static final String LOCALHOST_SSL = "https://10.0.2.2:9443/";

    private static final String LIVE_SERVER = "http://46.101.162.213/";
    private static final String LIVE_SERVER_SSL = "https://46.101.162.213/";

    public static String getActiveServer() {
        return LIVE_SERVER_SSL;
    }

    public static String getOutpanServer() {
        return OUTPAN_SERVER;
    }
}
