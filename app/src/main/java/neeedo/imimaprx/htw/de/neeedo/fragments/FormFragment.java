package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class FormFragment extends SuperFragment {
    protected ArrayList<String> suggestions;
    protected ArrayAdapter<String> suggestionsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        suggestions = new ArrayList<>();
    }

    public void setSuggestions(ArrayList<String> suggestions) {
        this.suggestions = suggestions;
    }
}
