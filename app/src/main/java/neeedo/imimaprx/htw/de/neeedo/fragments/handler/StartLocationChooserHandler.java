package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;

public class StartLocationChooserHandler implements View.OnClickListener {
    private final Fragment fragment;

    public StartLocationChooserHandler(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivity(intent);
    }
}
