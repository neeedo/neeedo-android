package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;

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

    public void fillSuggestions(GetSuggestionEvent e) {
        Log.d("Suggestion Event", "called");

        for(String suggestion : e.getTagResult().getTag().getAvailableTags()) {
            if(!suggestions.contains(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        suggestionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, suggestions);

        for(String tag : suggestions) {
            Log.d("Tag", tag);
        }
    }
}
