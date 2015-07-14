package neeedo.imimaprx.htw.de.neeedo.utils;

import android.view.View;

public class AutocompletionOnFocusChangeListener implements View.OnFocusChangeListener {
    private View layout;

    public AutocompletionOnFocusChangeListener(View layout) {
        this.layout = layout;
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }
}
