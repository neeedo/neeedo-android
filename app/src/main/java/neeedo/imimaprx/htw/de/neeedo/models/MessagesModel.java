package neeedo.imimaprx.htw.de.neeedo.models;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Messages;
import neeedo.imimaprx.htw.de.neeedo.entities.message.SingleMessage;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.user.Users;

public class MessagesModel {

    private Messages messages;
    private SingleMessage singleMessage;
    private static MessagesModel messagesModel;
    private Users users;
    private int newMessagesCounter = 0;

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
        messages.reverseOrder();
    }

    public void addMessageOnTop(Message message) {
        messages.addSingleMessage(message);
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

    public void increaseMessageCounter(int count) {
        newMessagesCounter += count;

        final Context context = ActiveUser.getInstance().getContext();
        final String text = context.getString(R.string.new_messages).replace("$", "" + newMessagesCounter);

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public int getNewMessagesCounter() {
        return newMessagesCounter;
    }

    public void setNewMessagesCounter(int newMessagesCounter) {
        this.newMessagesCounter = newMessagesCounter;
    }

    public void clearUsers() {
        users = new Users();
    }
}
