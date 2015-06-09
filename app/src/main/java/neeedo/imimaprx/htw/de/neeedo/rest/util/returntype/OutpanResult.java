package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

public class OutpanResult extends RestResult{

    private String tags;

    public OutpanResult(String creatorClass, ReturnType result, String tags) {
        super(creatorClass, result);
        this.tags =tags;
    }

    public String getTags() {
        return tags;
    }
}
