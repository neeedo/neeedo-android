package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
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
    }

    protected void showSuggestionTags(final ViewGroup layout, final MultiAutoCompleteTextView etInput, GetSuggestionEvent e) {
        if(e.getCompletionType().equals(BaseAsyncTask.CompletionType.PHRASE)) {
            layout.removeAllViewsInLayout();
        }

        if(!suggestions.isEmpty() && etInput.hasFocus())  {
            layout.setVisibility(View.VISIBLE);

            final CharSequence charSequence = e.getCharSequence();

            for(final String suggestion : suggestions) {
                final TextView tvTag = new TextView(getActivity());
                tvTag.setText(suggestion);
                tvTag.setPadding(25, 20, 25, 20);
                tvTag.setBackgroundColor(Color.parseColor("#88BEB1"));
                tvTag.setTextSize(16);

                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(10, 10, 10, 15);

                layout.addView(tvTag, layoutParams);

                tvTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        char lastCharacter = charSequence.charAt(charSequence.length() - 1);
                        String tagText = tvTag.getText().toString();
                        String inputText = etInput.getText().toString();
                        etInput.setText(inputText + ((lastCharacter == ',' || lastCharacter == ' ') ? "" : ", ") + tagText + ", ");
                        etInput.setText(inputText + tagText);
                        etInput.setSelection(etInput.length());
                        layout.removeViewInLayout(tvTag);
                        suggestions.remove(suggestion);
                    }
                });
            }
        } else {
            layout.setVisibility(View.GONE);
        }
    }
}
