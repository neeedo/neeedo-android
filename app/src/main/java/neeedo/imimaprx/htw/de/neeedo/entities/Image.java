package neeedo.imimaprx.htw.de.neeedo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;


@Root(name = "image")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image implements Serializable, BaseEntity {

    @Element
    private String image;

    public Image() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Image{" +
                "image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image1 = (Image) o;

        return !(image != null ? !image.equals(image1.image) : image1.image != null);

    }

    @Override
    public int hashCode() {
        return image != null ? image.hashCode() : 0;
    }
}
