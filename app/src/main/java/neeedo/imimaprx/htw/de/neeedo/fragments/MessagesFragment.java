package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.events.MessagesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageSendEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.message.PostMessageAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.message.PutMessageReadStateAsyncTask;

public class MessagesFragment extends SuperFragment implements View.OnClickListener {
    private ListView messageView;
    private Button sendBtn;
    private Activity activity;
    private String user1Id;
    private String user2Id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.messages_view, container, false);

        messageView = (ListView) view.findViewById(R.id.messages_view_list);

        Bundle args = getArguments();
        user2Id = args.getString("userId2");
        user1Id = args.getString("userId1");
        new GetMessagesAsyncTask(user1Id, user2Id).execute();

        return view;
    }


    @Subscribe
    public void getMessages(MessagesLoadedEvent messagesLoadedEvent) {
        ArrayList<Message> messages = MessagesModel.getInstance().getMessages().getMessages();

        ArrayAdapter<Message> messageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, messages);
        messageView.setAdapter(messageAdapter);
        for (Message m : messages) {
            if (!m.isRead() & m.getRecipient().getId().equals(user1Id))
                new PutMessageReadStateAsyncTask(m.getId()).execute();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        sendBtn = (Button) activity.findViewById(R.id.messages_view_send_button);
        sendBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Message message = new Message();

        switch (view.getId()) {
            case R.id.messages_view_send_button: {
                EditText editText = (EditText) activity.findViewById(R.id.messages_view_answer_text);
                String text = editText.getText().toString();
                if (text.isEmpty()) {
                    return;
                }
                message.setSenderId(user1Id);
                message.setRecipientId(user2Id);
                message.setBody(text);

                new PostMessageAsyncTask(message).execute();
                editText.setText("");


            }
            break;

        }
    }

    @Subscribe
    public void messageSend(UserMessageSendEvent userMessageSendEvent) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.single_offer_fragment_toast_message), Toast.LENGTH_SHORT).show();
        new GetMessagesAsyncTask(user1Id, user2Id).execute();

    }
}
