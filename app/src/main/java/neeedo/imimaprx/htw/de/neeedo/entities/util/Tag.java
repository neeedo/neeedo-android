package neeedo.imimaprx.htw.de.neeedo.entities.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;


@Root(name = "tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag implements Serializable, BaseEntity {

    private ArrayList<String> suggestedTags = new ArrayList<>();

    private ArrayList<String> completedTags = new ArrayList<>();

    public Tag() {

    }

    public ArrayList<String> getSuggestedTags() {
        return suggestedTags;
    }

    public void setSuggestedTags(ArrayList<String> suggestedTags) {
        this.suggestedTags = suggestedTags;
    }

    public ArrayList<String> getCompletedTags() {
        return completedTags;
    }

    public void setCompletedTags(ArrayList<String> completedTags) {
        this.completedTags = completedTags;
    }

    public ArrayList<String> getAvailableTags() {
        ArrayList<String> temp = new ArrayList<>();
        for (String s : suggestedTags) {
            if (!temp.contains(s)) {
                temp.add(s);
            }
        }
        for (String s : completedTags) {
            if (!temp.contains(s)) {
                temp.add(s);
            }
        }
        return temp;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "suggestedTags=" + suggestedTags +
                ", completedTags=" + completedTags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (suggestedTags != null ? !suggestedTags.equals(tag.suggestedTags) : tag.suggestedTags != null)
            return false;
        return !(completedTags != null ? !completedTags.equals(tag.completedTags) : tag.completedTags != null);

    }

    @Override
    public int hashCode() {
        int result = suggestedTags != null ? suggestedTags.hashCode() : 0;
        result = 31 * result + (completedTags != null ? completedTags.hashCode() : 0);
        return result;
    }
}
