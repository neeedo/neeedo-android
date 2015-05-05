package neeedo.imimaprx.htw.de.neeedo.factory;

import org.apache.http.client.HttpClient;

public abstract class AbstractFactoryBean<T> {

    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    protected HttpClient createInstance(int timeout) throws Exception {
        return null;
    }
}
