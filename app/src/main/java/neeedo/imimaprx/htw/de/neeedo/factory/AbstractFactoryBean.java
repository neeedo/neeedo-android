package neeedo.imimaprx.htw.de.neeedo.factory;

import org.apache.http.client.HttpClient;

/**
 * Created by Admin on 29.03.2015.
 */
public abstract class AbstractFactoryBean<T> {

    public Class<?> getObjectType() {
        return HttpClient.class;
    }

    protected HttpClient createInstance() throws Exception {
        return null;
    }


}
