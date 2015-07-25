package neeedo.imimaprx.htw.de.neeedo.events;

public class UserMessageContactsLoadedEvent {

    private boolean readState;

    public UserMessageContactsLoadedEvent(boolean readState) {
        this.readState = readState;
    }

    public boolean isReadState() {
        return readState;
    }

    public void setReadState(boolean readState) {
        this.readState = readState;
    }
}
