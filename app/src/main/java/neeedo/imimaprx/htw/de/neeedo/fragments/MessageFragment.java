package neeedo.imimaprx.htw.de.neeedo.fragments;


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
                args.putString("id", user.getId());
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });


        if (ActiveUser.getInstance().hasActiveUser()) {
            new GetMessagesByUserIdAndReadStateAsyncTask(UserModel.getInstance().getUser().getId(), false).execute();
            new GetMessagesByUserIdAndReadStateAsyncTask(UserModel.getInstance().getUser().getId(), true).execute();
        } else {
            editText.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Subscribe
    public void getUsers(UserMessageContactsLoadedEvent userMessageContactsLoadedEvent) {

        for (User user : MessagesModel.getInstance().getUsers().getUsers()) {
            users.add(user);
        }
        if (users.size() > 0) {
            editText.setVisibility(View.INVISIBLE);
            messageView.setVisibility(View.VISIBLE);
        } else {
            editText.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.INVISIBLE);
        }

        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
