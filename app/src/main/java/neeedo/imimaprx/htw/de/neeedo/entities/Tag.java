package neeedo.imimaprx.htw.de.neeedo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;


@Root(name = "tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag implements Serializable, BaseEntity {

    @Element
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tag='" + tag + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag1 = (Tag) o;

        return !(tag != null ? !tag.equals(tag1.tag) : tag1.tag != null);

    }

    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }
}
