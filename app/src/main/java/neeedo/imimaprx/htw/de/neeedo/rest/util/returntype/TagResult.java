package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

import neeedo.imimaprx.htw.de.neeedo.entities.Tag;

public class TagResult extends RestResult {

    private Tag tag;

    public TagResult(Object creatorInstance, ReturnType restResult, Tag tag) {
        super(creatorInstance, restResult);
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }
}
