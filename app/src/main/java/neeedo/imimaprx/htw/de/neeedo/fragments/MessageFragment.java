package neeedo.imimaprx.htw.de.neeedo.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageContactsLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesByUserIdAndReadStateAsyncTask;

public class MessageFragment extends SuperFragment {


    private ListView messageView;
    private ArrayList<User> users;
    private ArrayAdapter<User> userAdapter;
    private EditText editText;
    private String userId1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.message_view, container, false);
        users = new ArrayList<User>();

        messageView = (ListView) view.findViewById(R.id.message_view_user_list);
        userAdapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_list_item_1, users);
        editText = (EditText) view.findViewById(R.id.message_view_edit_text);
        messageView.setAdapter(userAdapter);

        messageView.setClickable(true);
        messageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user = (User) messageView.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new MessagesFragment();

                Bundle args = new Bundle();
                args.putString("userId2", user.getId());
                args.putString("userId1", userId1);
                fragment.setArguments(args);
                MessagesModel.getInstance().clearUsers();
                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        MessagesModel.getInstance().clearUsers();
        if (ActiveUser.getInstance().hasActiveUser()) {

            User user = UserModel.getInstance().getUser();

            if (user == null) {
                userId1 = ActiveUser.getInstance().getUserId();
            } else {
                userId1 = user.getId();
            }
            new GetMessagesByUserIdAndReadStateAsyncTask(userId1, false, false).execute();
            new GetMessagesByUserIdAndReadStateAsyncTask(userId1, true, false).execute();


        } else {
            editText.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.INVISIBLE);
            Context context = ActiveUser.getInstance().getContext();
            Toast.makeText(context, context.getString(R.string.exception_message_login), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Subscribe
    public void getUsers(UserMessageContactsLoadedEvent userMessageContactsLoadedEvent) {

        users = MessagesModel.getInstance().getUsers();

        if (users.size() > 0) {
            editText.setVisibility(View.INVISIBLE);
            messageView.setVisibility(View.VISIBLE);

        } else {
            editText.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.INVISIBLE);

            return;
        }

        userAdapter = new ArrayAdapter<User>(getActivity(), android.R.layout.simple_list_item_1, users);
        messageView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
    }
}
