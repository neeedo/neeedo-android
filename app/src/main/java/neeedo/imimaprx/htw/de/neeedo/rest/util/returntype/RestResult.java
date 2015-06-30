package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;


public class RestResult {

    public enum ReturnType {
        SUCCESS, FAILED
    }

    private ReturnType result;

    public RestResult(ReturnType result) {
        this.result = result;
    }

    public ReturnType getResult() {
        return result;
    }
}
