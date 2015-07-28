package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;

public class ListFragment extends SuperFragment {
    protected final ActiveUser activeUser = ActiveUser.getInstance();
    protected ListView listView;
    protected TextView tvHeader;
    protected View view;
    protected ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.list_products_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);
        tvHeader = (TextView) view.findViewById(R.id.tvHeader);
        progressBar = (ProgressBar) view.findViewById(R.id.list_offers_progessbar);

        return view;
    }

}
