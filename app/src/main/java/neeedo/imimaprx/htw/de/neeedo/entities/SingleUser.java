package neeedo.imimaprx.htw.de.neeedo.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "user")
public class SingleUser implements Serializable {

    @Element
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
