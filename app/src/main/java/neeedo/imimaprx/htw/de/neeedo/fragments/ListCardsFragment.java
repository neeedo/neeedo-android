package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;

public class ListCardsFragment extends Fragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.list_cards_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO fill list with card objects

        // arraylist with dummy items
        List<String> list = new ArrayList<String>();
        for(int i = 1; i <= 10; i++) {
            list.add("Card " + i); // fill dummy items
        }

        // array adapter shows items in list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // TODO set item listeners for single view

    }
}
