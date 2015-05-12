package neeedo.imimaprx.htw.de.neeedo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    @Element
    private String id = "";

    @Element
    private int version = 0;

    @Element
    private String username;

    @Element
    private String email;

    @Element
    private String password = "";

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
