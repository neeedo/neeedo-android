package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

import neeedo.imimaprx.htw.de.neeedo.entities.util.Tag;

public class TagResult extends RestResult {

    private Tag tag;

    public TagResult(ReturnType restResult, Tag tag) {
        super(restResult);
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }
}
