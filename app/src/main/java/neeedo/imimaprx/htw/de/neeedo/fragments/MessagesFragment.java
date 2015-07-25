package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.events.MessagesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageSendEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.MessageListArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.message.PostMessageAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.message.PutMessageReadStateAsyncTask;

public class MessagesFragment extends SuperFragment implements View.OnClickListener {
    public static final String FRAGMENT_ARGS_USER1ID = "userId1";
    public static final String FRAGMENT_ARGS_USER2ID = "userId2";
    public static final String FRAGMENT_ARGS_USERNAME = "username";
    private ListView messageView;
    private Button sendBtn;
    private Activity activity;
    private String user1Id;
    private String user2Id;
    private String username;
    private String matchUser = "Neeedo";
    private EditText editText;
    private TextView tvHeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.messages_view, container, false);

        messageView = (ListView) view.findViewById(R.id.messages_view_list);
        tvHeader = (TextView) view.findViewById(R.id.tvHeader);

        Bundle args = getArguments();
        user2Id = args.getString(FRAGMENT_ARGS_USER2ID);
        user1Id = args.getString(FRAGMENT_ARGS_USER1ID);
        username = args.getString(FRAGMENT_ARGS_USERNAME);
        new GetMessagesAsyncTask(user1Id, user2Id).execute();

        return view;
    }

    @Subscribe
    public void getMessages(MessagesLoadedEvent messagesLoadedEvent) {
        ArrayList<Message> messages = MessagesModel.getInstance().getMessages();

        if (user2Id.equals(matchUser)) {
            for (Message message : messages) {
                message.setMatchFoundMassage(true);
            }
        }

        ArrayAdapter<Message> messageAdapter = new MessageListArrayAdapter<>(getActivity(), R.layout.messages_message_item, messages);
        messageView.setAdapter(messageAdapter);
        for (Message m : messages) {
            if (!m.isRead() && m.getRecipient().getId().equals(user1Id))
                new PutMessageReadStateAsyncTask(m.getId()).execute();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvHeader.setText(username);

        activity = getActivity();
        sendBtn = (Button) activity.findViewById(R.id.messages_view_send_button);
        sendBtn.setOnClickListener(this);

        editText = (EditText) activity.findViewById(R.id.messages_view_answer_text);

        if (user2Id.equals(matchUser)) {
            matchMessageActions();
        }
    }


    private void matchMessageActions() {
        sendBtn.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);

        messageView.setClickable(true);
        messageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Message message = (Message) messageView.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new SingleOfferFragment();

                Bundle args = new Bundle();
                args.putString("id", message.getBody());
                fragment.setArguments(args);
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Message message = new Message();

        switch (view.getId()) {
            case R.id.messages_view_send_button: {

                String text = editText.getText().toString();
                if (text.isEmpty() || text.matches("[ ]+")) {
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
        getMessages(null);
    }
}
