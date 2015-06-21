package neeedo.imimaprx.htw.de.neeedo.entities.message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "messages")
public class Messages implements Serializable, BaseEntity {

    @Element
    private ArrayList<Message> messages;

    public Messages() {
    }


    public boolean isEmpty() {
        if (messages == null || messages.size() == 0) {
            return true;
        }
        return false;
    }

    public void addSingleDemand(Message message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "messages=" + messages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Messages messages1 = (Messages) o;

        return !(messages != null ? !messages.equals(messages1.messages) : messages1.messages != null);

    }

    @Override
    public int hashCode() {
        return messages != null ? messages.hashCode() : 0;
    }
}
