package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;

public class DiolorSwipeFragment extends SuperFragment {

    private ArrayAdapter<String> titleArrayAdapter;
//    private ArrayAdapter<String> descriptionArrayAdapter;

    private ArrayList<String> titleAdapter;
//    private ArrayList<String> descriptionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.diolor_main, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.frame);

        titleAdapter = new ArrayList<String>();
        titleAdapter.add(getActivity().getString(R.string.sample_title));
        titleAdapter.add(getActivity().getString(R.string.sample_title));
        titleArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.diolor_item, R.id.card_title, titleAdapter);



//        descriptionAdapter = new ArrayList<String>();
//        descriptionAdapter.add(getActivity().getString(R.string.sample_describtion));
//        descriptionAdapter.add(getActivity().getString(R.string.sample_describtion));
//        descriptionArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.diolor_item, R.id.card_description, descriptionAdapter);

        flingContainer.setAdapter(titleArrayAdapter);
//        flingContainer.setAdapter(descriptionArrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                titleAdapter.remove(0);
//                descriptionAdapter.remove(0);
                titleArrayAdapter.notifyDataSetChanged();
//                descriptionArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(getActivity(), "Dismissed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(getActivity(), "Liked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                titleAdapter.add(getActivity().getString(R.string.sample_title));
//                descriptionAdapter.add(getActivity().getString(R.string.sample_describtion));
                titleArrayAdapter.notifyDataSetChanged();
//                descriptionArrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(getActivity(), "Clicked!");
            }
        });
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}
