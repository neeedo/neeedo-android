package neeedo.imimaprx.htw.de.neeedo.entities.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;


@Root(name = "article")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article implements Serializable {

    @Element
    private String gtin;

    @Element
    private String name;

    @Element
    @JsonIgnore
    private Attributes attributes;

    @Element
    private ArrayList<String> images;

    @Element
    private ArrayList<String> videos;

    public String getGtin() {

        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<String> videos) {
        this.videos = videos;
    }


    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {

        return "Article{" +
                "gtin='" + gtin + '\'' +
                ", name='" + name + '\'' +
                ", attributes=" + attributes +
                ", images=" + images +
                ", videos=" + videos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (gtin != null ? !gtin.equals(article.gtin) : article.gtin != null) return false;
        if (name != null ? !name.equals(article.name) : article.name != null) return false;
        if (attributes != null ? !attributes.equals(article.attributes) : article.attributes != null)
            return false;
        if (images != null ? !images.equals(article.images) : article.images != null) return false;
        return !(videos != null ? !videos.equals(article.videos) : article.videos != null);

    }

    @Override
    public int hashCode() {
        int result = gtin != null ? gtin.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        result = 31 * result + (videos != null ? videos.hashCode() : 0);
        return result;
    }


}
