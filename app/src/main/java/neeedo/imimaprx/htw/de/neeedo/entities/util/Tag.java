package neeedo.imimaprx.htw.de.neeedo.entities.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Root(name = "tag")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tag implements Serializable, BaseEntity {

    private ArrayList<String> suggestedTags;

    private ArrayList<String> completedTags;

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

    public List<String> getAvailableTags() {
        List<String> temp = new ArrayList<>();
        for (String s : suggestedTags) {
            temp.add(s);
        }
        for (String s : completedTags) {
            temp.add(s);
        }
        return temp;
    }


}
