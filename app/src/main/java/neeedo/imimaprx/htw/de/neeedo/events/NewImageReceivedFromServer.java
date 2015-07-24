package neeedo.imimaprx.htw.de.neeedo.events;

import neeedo.imimaprx.htw.de.neeedo.vo.OfferImage;

public class NewImageReceivedFromServer {

    private final OfferImage offerImage;

    public NewImageReceivedFromServer(OfferImage offerImage) {
        this.offerImage = offerImage;
    }

    public OfferImage getOfferImage() {
        return offerImage;
    }

}
