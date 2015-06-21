package neeedo.imimaprx.htw.de.neeedo.entities.user;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "user")
public class SingleUser implements Serializable, BaseEntity {

    @Element
    private User user;

    @Element
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasErrorCode() {
        return !(error == null);
    }
}
