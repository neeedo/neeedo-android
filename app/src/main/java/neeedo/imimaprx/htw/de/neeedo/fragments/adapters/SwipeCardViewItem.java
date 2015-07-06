package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import java.util.ArrayList;

public class SwipeCardViewItem {

    private final String description;
    private final String title;
    private final ArrayList<String> images;

    public SwipeCardViewItem(String title, String description, ArrayList<String> images) {
        this.title = title;
        this.description = description;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
