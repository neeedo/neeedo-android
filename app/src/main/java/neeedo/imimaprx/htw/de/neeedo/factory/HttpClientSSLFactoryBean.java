package neeedo.imimaprx.htw.de.neeedo.factory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

public class HttpClientSSLFactoryBean extends AbstractFactoryBean<HttpClient> {

    @Override
    protected HttpClient createInstance(int timeout) throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        TrustStrategy allTrust = new TrustStrategy() {


            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                return true;
            }
        };

        SSLContext sslcontext =
                SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, allTrust).build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpClientBuilder httpClientBuilder = HttpClients.custom().setSSLSocketFactory(sslsf);

       /*
        final String userToken = ActiveUser.getInstance().getUserToken();

        if (userToken != null) {

            httpClientBuilder.setUserTokenHandler(new UserTokenHandler() {
                @Override
                public Object getUserToken(HttpContext httpContext) {

                    httpContext.setAttribute("user", userToken);
                    return httpContext.getAttribute("user");
                }
            });
        }

        */
        //set the Timeout
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(timeout);
        requestBuilder = requestBuilder.setConnectionRequestTimeout(timeout);
        requestBuilder = requestBuilder.setSocketTimeout(timeout);
        requestBuilder.setAuthenticationEnabled(true);

        httpClientBuilder.setDefaultRequestConfig(requestBuilder.build());

        CloseableHttpClient httpClient = httpClientBuilder.build();


        return httpClient;
    }

}
