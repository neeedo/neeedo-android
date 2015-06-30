package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class FormFragment extends SuperFragment {
    protected ArrayList<String> completions;
    protected ArrayAdapter<String> completionsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        completions = new ArrayList<>();
    }

    public void fillSuggestions(GetSuggestionEvent e) {
        Log.d("Suggestion Event", "called");

        if(e.getCompletionType().equals(BaseAsyncTask.CompletionType.TAG)) {
            completions = e.getTagResult().getTag().getCompletedTags();

            completionsAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1, completions);

            for(String tag : completions) {
                Log.d("Tag", tag);
            }
        }
    }
}
