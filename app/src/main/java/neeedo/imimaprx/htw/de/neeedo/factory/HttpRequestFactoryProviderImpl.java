package neeedo.imimaprx.htw.de.neeedo.factory;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Class delivers an instance of {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory} with defineable timeout.
 * {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory} is now used because it supports Https.
 */
public class HttpRequestFactoryProviderImpl {

    /**
     * Return an instance of {@link org.springframework.http.client.HttpComponentsClientHttpRequestFactory} with the given timeout set.
     *
     * @param millisecondsToTimeout
     * @return
     */
    public static ClientHttpRequestFactory getClientHttpRequestFactory(final int millisecondsToTimeout) {
        // SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(millisecondsToTimeout);
        factory.setConnectTimeout(millisecondsToTimeout);
        return factory;
    }
}
