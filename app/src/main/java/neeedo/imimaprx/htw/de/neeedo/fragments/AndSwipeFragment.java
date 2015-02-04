package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

import neeedo.imimaprx.htw.de.neeedo.R;

public class AndSwipeFragment extends SuperFragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.and_swipe, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CardContainer mCardContainer = (CardContainer) getActivity().findViewById(R.id.and_swipe);

        mCardContainer.setOrientation(Orientations.Orientation.Ordered);

        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());

        adapter.add(new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title2", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title3", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title4", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title5", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title6", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title2", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title3", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title4", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title5", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title6", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title2", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title3", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title4", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title5", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title6", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title2", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title3", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title4", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title5", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title6", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title2", "Description goes here", getResources().getDrawable(R.drawable.picture2)));
        adapter.add(new CardModel("Title3", "Description goes here", getResources().getDrawable(R.drawable.picture3)));
        adapter.add(new CardModel("Title4", "Description goes here", getResources().getDrawable(R.drawable.picture1)));
        adapter.add(new CardModel("Title5", "Description goes here", getResources().getDrawable(R.drawable.picture2)));

        mCardContainer.setAdapter(adapter);

    }
}
