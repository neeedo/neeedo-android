package neeedo.imimaprx.htw.de.neeedo.entities.user;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;

@Root(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable, BaseEntity {

    @Element
    private String id = "";

    @Element
    private int version = 0;

    @Element
    private String name = "";

    @Element
    private String email = "";

    @Element
    private String password = "";

    @Element
    private boolean hasNewMessages = false;

    public User() {

    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
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

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isHasNewMessages() {
        return hasNewMessages;
    }

    public void setHasNewMessages(boolean hasNewMessages) {
        this.hasNewMessages = hasNewMessages;
    }

    @Override
    public String toString() {
        String text = name;
        Context context = ApplicationContextModel.getInstance().getApplicationContext();
        String s = "";
        if (hasNewMessages) {
            s = context.getString(R.string.new_message_text) + " ";
        } else {
            s = context.getString(R.string.message_text) + " ";
        }
        text = s + name;
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (version != user.version) return false;
        if (hasNewMessages != user.hasNewMessages) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return !(password != null ? !password.equals(user.password) : user.password != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + version;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (hasNewMessages ? 1 : 0);
        return result;
    }
}
