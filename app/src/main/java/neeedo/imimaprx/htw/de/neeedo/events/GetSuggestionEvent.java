package neeedo.imimaprx.htw.de.neeedo.events;

import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.TagResult;

public class GetSuggestionEvent {
    private TagResult tagResult;

    public GetSuggestionEvent(TagResult tagResult) {
        this.tagResult = tagResult;
    }

    public TagResult getTagResult() {
        return tagResult;
    }

}
