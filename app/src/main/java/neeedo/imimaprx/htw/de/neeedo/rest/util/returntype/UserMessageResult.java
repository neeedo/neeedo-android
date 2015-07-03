package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;


public class UserMessageResult extends RestResult {

    private boolean readState;

    public UserMessageResult(ReturnType restResult, boolean readState) {
        super(restResult);
    }

    public boolean isReadState() {
        return readState;
    }
}
