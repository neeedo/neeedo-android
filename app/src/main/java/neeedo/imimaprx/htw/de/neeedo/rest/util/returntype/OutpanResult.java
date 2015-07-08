package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

public class OutpanResult extends RestResult {

    private String tags;

    public OutpanResult(ReturnType result, String tags) {
        super(result);
        this.tags = tags;
    }

    public OutpanResult(ReturnType result) {
        super(result);
    }

    public String getTags() {
        return tags;
    }
}
