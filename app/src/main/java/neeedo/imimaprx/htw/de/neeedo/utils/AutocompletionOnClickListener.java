package neeedo.imimaprx.htw.de.neeedo.utils;

import android.view.View;
import android.widget.MultiAutoCompleteTextView;

public class AutocompletionOnClickListener implements View.OnClickListener {
    private boolean open;
    private MultiAutoCompleteTextView etInput;

    public AutocompletionOnClickListener(MultiAutoCompleteTextView etInput) {
        open = false;
        this.etInput = etInput;
    }

    @Override
    public void onClick(View view) {
        if (open) {
            etInput.dismissDropDown();
            open = false;
        } else {
            etInput.showDropDown();
            open = true;
        }
    }
}
