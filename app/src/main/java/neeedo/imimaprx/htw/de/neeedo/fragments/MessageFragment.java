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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageContactsLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.MessageUserArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesByUserIdAndReadStateAsyncTask;

public class MessageFragment extends SuperFragment {
    private ListView messageView;
    private ArrayList<User> users;
    private ArrayAdapter<User> userAdapter;
    private String userId1;
    private TextView tvEmpty;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.message_view, container, false);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        progressBar = (ProgressBar) view.findViewById(R.id.list_message_progressbar);
        users = new ArrayList<User>();

        messageView = (ListView) view.findViewById(R.id.message_view_user_list);
        userAdapter = new MessageUserArrayAdapter<>(getActivity(), R.layout.message_user_item, users);
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
                args.putString("username", user.getName());
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
            tvEmpty.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.INVISIBLE);
            Context context = ApplicationContextModel.getInstance().getApplicationContext();
            Toast.makeText(context, context.getString(R.string.exception_message_login), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Subscribe
    public void getUsers(UserMessageContactsLoadedEvent userMessageContactsLoadedEvent) {

        users = MessagesModel.getInstance().getUsers();

        if (users.size() <= 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

            return;
        } else {
            tvEmpty.setVisibility(View.GONE);
            messageView.setVisibility(View.VISIBLE);
        }

        userAdapter = new MessageUserArrayAdapter<>(getActivity(), R.layout.message_user_item, users);
        messageView.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }
}
