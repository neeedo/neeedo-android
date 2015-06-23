package neeedo.imimaprx.htw.de.neeedo.entities.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMin implements Serializable, BaseEntity {

    @Element
    private String id = "";

    @Element
    private String name = "";

    public UserMin() {

    }

    public UserMin(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserMin userMin = (UserMin) o;

        if (id != null ? !id.equals(userMin.id) : userMin.id != null) return false;
        return !(name != null ? !name.equals(userMin.name) : userMin.name != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
