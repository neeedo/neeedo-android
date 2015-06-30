package neeedo.imimaprx.htw.de.neeedo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class LocationAutoCompleteTextView extends AutoCompleteTextView {

    public LocationAutoCompleteTextView(Context context) {
        super(context);
    }

    public LocationAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocationAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        //disables filter
        String filterText = "";
        super.performFiltering(filterText, keyCode);
    }
}