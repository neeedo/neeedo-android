package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

public class ImageUploadResult extends RestResult {

    private String imageId;

    public ImageUploadResult(Object creatorInstance, RestResult.ReturnType restResult, String imageId) {
        super(creatorInstance, restResult);
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }
}
