package neeedo.imimaprx.htw.de.neeedo.events;

public class NewEanNumberScannedEvent {
    private final String eanNumber;

    public NewEanNumberScannedEvent(String eanNumber) {
        this.eanNumber = eanNumber;
    }

    public String getEanNumber(){
        return eanNumber;
    }
}
