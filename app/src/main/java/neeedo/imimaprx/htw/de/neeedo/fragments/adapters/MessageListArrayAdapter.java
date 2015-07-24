package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;

public class MessageListArrayAdapter<Object> extends ArrayAdapter<Object> {
    private final String currentUserId = ActiveUser.getInstance().getUserId();
    private Context context;
    private int layoutResourceId;
    private List<Object> messages;

    private LinearLayout layoutMessageItem;
    private TextView tvMessage;
    private TextView tvTime;

    public MessageListArrayAdapter(Context context, int layoutResourceId, List<Object> messages) {
        super(context, layoutResourceId, messages);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        row = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);

        layoutMessageItem = (LinearLayout) row.findViewById(R.id.layoutMessageItem);
        tvMessage = (TextView) row.findViewById(R.id.tvMessage);
        tvTime = (TextView) row.findViewById(R.id.tvTime);

        Message message = (Message) messages.get(position);

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
        Date date = new Date(message.getTimestamp());

        String messageSender = message.getSender().getId();
        if(messageSender != null && messageSender.equals(currentUserId)) {
            layoutMessageItem.setGravity(Gravity.LEFT);
            tvMessage.setBackground(context.getResources().getDrawable(R.drawable.shape_message_item_left));
        }

        String messageText = message.getBody();
        String messageTime = formatter.format(date);

        tvTime.setText(messageTime);
        tvMessage.setText(messageText);

        return row;
    }
}
