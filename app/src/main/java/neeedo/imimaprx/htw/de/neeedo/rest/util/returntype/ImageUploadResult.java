package neeedo.imimaprx.htw.de.neeedo.rest.util.returntype;

public class ImageUploadResult extends RestResult {

    private String imageId;

    public ImageUploadResult( RestResult.ReturnType restResult, String imageId) {
        super( restResult);
        this.imageId = imageId;
    }

    public String getImageId() {
        return imageId;
    }
}
