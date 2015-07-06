package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import neeedo.imimaprx.htw.de.neeedo.LocationChooserActivity;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class StartLocationChooserHandler implements View.OnClickListener {
    private final Fragment fragment;

    public StartLocationChooserHandler(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(fragment.getActivity(), LocationChooserActivity.class);
        fragment.startActivityForResult(intent, RequestCodes.FIND_LOCATION_REQUEST_CODE);
    }
}
