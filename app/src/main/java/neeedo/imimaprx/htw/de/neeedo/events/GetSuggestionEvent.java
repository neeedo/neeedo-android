package neeedo.imimaprx.htw.de.neeedo.events;

import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.TagResult;

public class GetSuggestionEvent {
    private TagResult tagResult;
    private BaseAsyncTask.CompletionType completionType;

    public GetSuggestionEvent(TagResult tagResult, BaseAsyncTask.CompletionType completionType) {
        this.tagResult = tagResult;
        this.completionType = completionType;
    }

    public TagResult getTagResult() {
        return tagResult;
    }

    public BaseAsyncTask.CompletionType getCompletionType() {
        return completionType;
    }

}
