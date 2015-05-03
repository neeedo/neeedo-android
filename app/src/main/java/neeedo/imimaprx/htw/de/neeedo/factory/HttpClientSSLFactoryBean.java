package neeedo.imimaprx.htw.de.neeedo.factory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.security.KeyStore;

import javax.net.ssl.SSLContext;

public class HttpClientSSLFactoryBean extends AbstractFactoryBean<HttpClient> {

    @Override
    protected HttpClient createInstance() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        TrustStrategy allTrust = new TrustStrategy() {


            @Override
            public boolean isTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
                return true;
            }
        };

        SSLContext sslcontext =
                SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, allTrust).build();

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);


        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();



        return httpClient;
    }

}
