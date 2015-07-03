package neeedo.imimaprx.htw.de.neeedo.events;


import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.UserMessageResult;

public class UserMessageContactsLoadedEvent {

    private UserMessageResult userMessageResult;
    private boolean read;

    public UserMessageContactsLoadedEvent(UserMessageResult userMessageResult, boolean read) {
        this.userMessageResult = userMessageResult;
    }

    public UserMessageResult getUserMessageResult() {
        return userMessageResult;
    }

    public void setUserMessageResult(UserMessageResult userMessageResult) {
        this.userMessageResult = userMessageResult;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
