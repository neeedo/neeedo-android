package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;


public class RestResult {

    public enum ReturnType {
        SUCCESS, FAILED
    }

    private String creatorClass;
    private ReturnType result;

    public RestResult(String creatorClass, ReturnType result) {
        this.creatorClass = creatorClass;
        result = result;
    }

    public String getCreatorClass() {
        return creatorClass;
    }

    public void setCreatorClass(String creatorClass) {
        this.creatorClass = creatorClass;
    }

    public ReturnType getResult() {
        return result;
    }

    public void setResult(ReturnType result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RestResult{" +
                "creatorClass='" + creatorClass + '\'' +
                ", result=" + result +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RestResult that = (RestResult) o;

        if (creatorClass != null ? !creatorClass.equals(that.creatorClass) : that.creatorClass != null)
            return false;
        return result == that.result;

    }

    @Override
    public int hashCode() {
        int result1 = creatorClass != null ? creatorClass.hashCode() : 0;
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }
}
