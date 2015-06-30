package neeedo.imimaprx.htw.de.neeedo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.fragments.FormFragment;
import neeedo.imimaprx.htw.de.neeedo.rest.completion.GetCompletionAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class AutocompletionTextWatcher implements TextWatcher {
    FormFragment fragment;
    EditText etInput;
    BaseAsyncTask.CompletionType completionType;

    public AutocompletionTextWatcher(FormFragment fragment, EditText etInput, BaseAsyncTask.CompletionType completionType) {
        this.fragment = fragment;
        this.etInput = etInput;
        this.completionType = completionType;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(completionType.equals(BaseAsyncTask.CompletionType.TAG)) {
            String tagsText = etInput.getText().toString();
            String tags[] = tagsText.split("[ ,]+");
            String lastTag = tags[tags.length-1]; // get last tag for completion
            if (tagsText.length() > 0) {
                // do completion for last tag
                if (lastTag.matches("[A-Za-z0-9]+")) {
                    new GetCompletionAsyncTask(lastTag, BaseAsyncTask.CompletionType.TAG).execute();
                }
            }
        } else if(completionType.equals(BaseAsyncTask.CompletionType.PHRASE)) { // TODO only if last tag completed
            String tagsText = etInput.getText().toString();
            char lastCharacter = charSequence.charAt(charSequence.length()-1);
            if(tagsText.length() > 0) {
                // do suggestion for all tags
                if(tagsText.matches("[A-Za-z0-9, ]+")) { // TODO check why phrases with "," don't work
                    new GetCompletionAsyncTask(tagsText, BaseAsyncTask.CompletionType.PHRASE).execute();
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
