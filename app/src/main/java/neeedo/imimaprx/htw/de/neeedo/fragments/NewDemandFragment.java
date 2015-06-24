package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.MainActivity;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendDemandHandler;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;


public class NewDemandFragment extends FormDemandFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (locationAvailable) {
            etLocationLat.setText(String.valueOf(locationLatitude));
            etLocationLon.setText(String.valueOf(locationLongitude));
        }

        btnSubmit.setOnClickListener(new SendDemandHandler(
                        BaseAsyncTask.SendMode.CREATE,
                        etMustTags,
                        etShouldTags,
                        etLocationLat,
                        etLocationLon,
                        etDistance,
                        etPriceMin,
                        etPriceMax
                )
        );

        return view;
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        redirectToFragment(ListDemandsFragment.class, MainActivity.MENU_LIST_DEMANDS);
    }

    @Subscribe
    public void fillSuggestions(GetSuggestionEvent e) {
        Log.d("Suggestion Event", "called");

        suggestions = e.getTagResult().getTag().getAvailableTags();
        suggestionsAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, suggestions);
        etMustTags.setAdapter(suggestionsAdapter);

        for(String tag : suggestions) {
            Log.d("Tag", tag);
        }

        // rip off last tag of current text
        final String[] tags = etMustTags.getText().toString().split("[ ,]+");
        String currentTextWithoutLastTag = "";
        for(int i = 0; i < tags.length-1; i++) {
            if(i != 0) {
                currentTextWithoutLastTag = currentTextWithoutLastTag + ", " + tags[i];
            } else {
                currentTextWithoutLastTag = tags[i];
            }
        }
        final String currentText = currentTextWithoutLastTag;

        // add selected suggestion tag to already entered tags
        etMustTags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("Autocomplete", "Item " + position + " selected");

                String newText;
                if(currentText.length() != 0) {
                    newText = currentText + ", " + adapterView.getItemAtPosition(position);
                } else {
                    newText = (String) adapterView.getItemAtPosition(position);
                }
                etMustTags.setText(newText);
                etMustTags.setSelection(etMustTags.getText().length());
            }
        });

        suggestionsAdapter.notifyDataSetChanged();
    }
}
