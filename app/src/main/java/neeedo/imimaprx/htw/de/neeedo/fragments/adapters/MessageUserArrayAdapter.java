package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;

public class MessageUserArrayAdapter<Object> extends ArrayAdapter<Object> {
    private Context context;
    private int layoutResourceId;
    private List<Object> users;

    private TextView tvAvatar;
    private TextView tvNew;
    private TextView tvUser;

    public MessageUserArrayAdapter(Context context, int layoutResourceId, List<Object> users) {
        super(context, layoutResourceId, users);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.users = users;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        row = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);

        tvAvatar = (TextView) row.findViewById(R.id.tvAvatar);
        tvNew = (TextView) row.findViewById(R.id.tvNew);
        tvUser = (TextView) row.findViewById(R.id.tvUser);

        User user = (User) users.get(position);

        String userName = user.getName();
        String userLetter = String.valueOf(userName.charAt(0)).toUpperCase();
        boolean hasNewMessages = user.isHasNewMessages();

        if(hasNewMessages) {
            tvNew.setVisibility(View.VISIBLE);
            tvUser.setTypeface(null, Typeface.BOLD);
        } else {
            tvNew.setVisibility(View.GONE);
            tvUser.setTypeface(null, Typeface.NORMAL);
        }

        tvAvatar.setText(userLetter);
        tvUser.setText(userName);

        return row;
    }
}
