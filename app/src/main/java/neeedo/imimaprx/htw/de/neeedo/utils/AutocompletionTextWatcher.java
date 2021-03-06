package neeedo.imimaprx.htw.de.neeedo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import neeedo.imimaprx.htw.de.neeedo.fragments.FormFragment;
import neeedo.imimaprx.htw.de.neeedo.rest.completion.GetCompletionAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class AutocompletionTextWatcher implements TextWatcher {
    FormFragment fragment;
    EditText etInput;
    EditText etInputOther;
    BaseAsyncTask.CompletionType completionType;

    public AutocompletionTextWatcher(FormFragment fragment, EditText etInput, EditText etInputOther, BaseAsyncTask.CompletionType completionType) {
        this.fragment = fragment;
        this.etInput = etInput;
        this.etInputOther = etInputOther;
        this.completionType = completionType;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String otherTagsField = "";
        if (etInputOther != null) {
            otherTagsField = etInputOther.getText().toString();
        }

        if (completionType.equals(BaseAsyncTask.CompletionType.TAG) && charSequence.length() > 0) {
            String tagsText = etInput.getText().toString();
            String tags[] = tagsText.split("[ ,]+");
            String lastTag = tags[tags.length - 1]; // get last tag for completion
            if (tagsText.length() > 1) {
                // do completion for last tag
                if (lastTag.matches("[A-Za-z0-9]+")) {
                    new GetCompletionAsyncTask(lastTag, BaseAsyncTask.CompletionType.TAG, charSequence).execute();
                }
            }
        } else if (completionType.equals(BaseAsyncTask.CompletionType.PHRASE) && charSequence.length() > 0) {
            String tagsText = etInput.getText().toString() + otherTagsField;
            if (tagsText.length() > 1) {
                char lastCharacter = charSequence.charAt(charSequence.length() - 1);
                // do suggestion for all tags
                if (tagsText.matches("[A-Za-z0-9, ]+") && (lastCharacter == ',' || lastCharacter == ' ')) {
                    new GetCompletionAsyncTask(tagsText, BaseAsyncTask.CompletionType.PHRASE, charSequence).execute();
                }
            }
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
