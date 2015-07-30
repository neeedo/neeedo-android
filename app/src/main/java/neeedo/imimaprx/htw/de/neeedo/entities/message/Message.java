package neeedo.imimaprx.htw.de.neeedo.entities.message;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;


@Root(name = "message")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable, BaseEntity {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");

    @Element
    private String id;

    @Element
    private User sender;

    @Element
    private User recipient;

    @Element
    private Long timestamp;

    @Element
    private boolean read;

    @Element
    private String senderId;

    @Element
    private String recipientId;

    @Element
    private String messageId;

    @Element
    private String body;

    @Element
    private boolean matchFoundMassage = false;

    public Message() {

    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public static void setFormatter(SimpleDateFormat formatter) {
        Message.formatter = formatter;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public boolean isMatchFoundMassage() {
        return matchFoundMassage;
    }

    public void setMatchFoundMassage(boolean matchFoundMassage) {
        this.matchFoundMassage = matchFoundMassage;
    }

    @Override
    public String toString() {
        String text = "";

        if (!matchFoundMassage) {
            Date date = new Date(timestamp);
            text = formatter.format(date) + ": " + body;
        } else {
            Context context = ApplicationContextModel.getInstance().getApplicationContext();
            text = context.getString(R.string.new_match_text);
        }
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (read != message.read) return false;
        if (matchFoundMassage != message.matchFoundMassage) return false;
        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        if (sender != null ? !sender.equals(message.sender) : message.sender != null) return false;
        if (recipient != null ? !recipient.equals(message.recipient) : message.recipient != null)
            return false;
        if (timestamp != null ? !timestamp.equals(message.timestamp) : message.timestamp != null)
            return false;
        if (senderId != null ? !senderId.equals(message.senderId) : message.senderId != null)
            return false;
        if (recipientId != null ? !recipientId.equals(message.recipientId) : message.recipientId != null)
            return false;
        if (messageId != null ? !messageId.equals(message.messageId) : message.messageId != null)
            return false;
        return !(body != null ? !body.equals(message.body) : message.body != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sender != null ? sender.hashCode() : 0);
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        result = 31 * result + (read ? 1 : 0);
        result = 31 * result + (senderId != null ? senderId.hashCode() : 0);
        result = 31 * result + (recipientId != null ? recipientId.hashCode() : 0);
        result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        result = 31 * result + (matchFoundMassage ? 1 : 0);
        return result;
    }
}
