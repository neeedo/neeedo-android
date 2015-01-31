package neeedo.imimaprx.htw.de.neeedo.factory;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

//TODO rename class to something sane
public class ClientHttpRequestFactoryProvider {

    public static ClientHttpRequestFactory getClientHttpRequestFactory(final int millisecondsToTimeout) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(millisecondsToTimeout);
        factory.setConnectTimeout(millisecondsToTimeout);
        return factory;
    }
}
