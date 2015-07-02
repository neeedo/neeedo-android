package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.events.MessagesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesAsyncTask;

public class MessagesFragment extends SuperFragment {
    private ListView messageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.messages_view, container, false);

        messageView = (ListView) view.findViewById(R.id.messages_view_list);

        Bundle args = getArguments();
        String user2Id = args.getString("id");
        new GetMessagesAsyncTask(UserModel.getInstance().getUser().getId(), user2Id).execute();

        return view;
    }


    @Subscribe
    public void getMessages(MessagesLoadedEvent messagesLoadedEvent) {
        ArrayList<Message> messages = MessagesModel.getInstance().getMessages().getMessages();

        for (Message message : messages) {
            Log.i(this.getClass().getSimpleName(), "MessageFragment: " + message);
        }

        if(!messages.isEmpty()) {
            ArrayAdapter<Message> messageAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, messages);
            messageView.setAdapter(messageAdapter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
