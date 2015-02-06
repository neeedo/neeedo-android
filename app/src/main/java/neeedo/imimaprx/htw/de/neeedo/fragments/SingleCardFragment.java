package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

public class SingleCardFragment extends SuperFragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_card_view, container, false);

        textView = (TextView) view.findViewById(R.id.singleview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SuperHttpAsyncTask asyncTask;

        // execute async task @Subscribe
        asyncTask = new HttpGetAsyncTask();
        asyncTask.execute();
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {

        // TODO show card content

        System.out.println(getArguments());

        textView.setText("foobar");

    }
}
