package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class FormFragment extends SuperFragment {
    protected ArrayList<String> completions;
    protected ArrayAdapter<String> completionsAdapter;
    protected ArrayList<String> suggestions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        completions = new ArrayList<>();
        suggestions = new ArrayList<>();
    }

    public void fillSuggestions(GetSuggestionEvent e) {
        Log.d("Suggestion Event", e.getCompletionType()+" called");

        suggestions = e.getTagResult().getTag().getSuggestedTags();
        completions = e.getTagResult().getTag().getCompletedTags();

        completionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, completions);

        for(String tag : completions) {
            Log.d("Tag", tag);
        }
    }

    protected void showSuggestionTags(final ViewGroup layout, final EditText tags, GetSuggestionEvent e) {
        if(e.getCompletionType().equals(BaseAsyncTask.CompletionType.PHRASE)) {
            layout.removeAllViewsInLayout();
        }

        for(final String suggestion : suggestions) {
            final TextView tag = new TextView(getActivity());
            tag.setText(suggestion);
            tag.setPadding(25, 20, 25, 20);
            tag.setBackgroundColor(Color.parseColor("#88BEB1"));
            tag.setTextSize(16);

            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 10, 15);

            layout.addView(tag, layoutParams);

            tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tagText = tag.getText().toString();
                    tags.setText(tags.getText().toString() + tagText + ", ");
                    tags.setSelection(tags.length());
                    layout.removeViewInLayout(tag);
                    suggestions.remove(suggestion);
                }
            });
        }
    }
}
