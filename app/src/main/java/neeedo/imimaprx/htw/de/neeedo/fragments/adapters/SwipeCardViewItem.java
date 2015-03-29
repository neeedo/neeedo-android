package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

public class SwipeCardViewItem {

    private final String description;
    private final String title;

    public SwipeCardViewItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
