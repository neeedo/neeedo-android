package neeedo.imimaprx.htw.de.neeedo.models;


import neeedo.imimaprx.htw.de.neeedo.entities.message.Messages;
import neeedo.imimaprx.htw.de.neeedo.entities.message.SingleMessage;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.user.Users;

public class MessagesModel {

    private Messages messages;
    private SingleMessage singleMessage;
    private static MessagesModel messagesModel;
    private Users users;

    public static MessagesModel getInstance() {
        if (messagesModel == null)
            messagesModel = new MessagesModel();
        return messagesModel;
    }

    private MessagesModel() {
    }

    public SingleMessage getSingleMessage() {
        return singleMessage;
    }

    public void setSingleMessage(SingleMessage singleMessage) {
        this.singleMessage = singleMessage;
    }

    public Messages getMessages() {
        return messages;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public void appendUsers(Users newUsers) {
        if (this.users == null) {
            this.users = newUsers;
        } else {
            for (User user : newUsers.getUsers()) {
                boolean skip = false;
                for (User u : users.getUsers()) {
                    if (u.getId().equals(user.getId())) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
                users.addSingleUser(user);
            }
        }
    }
}
