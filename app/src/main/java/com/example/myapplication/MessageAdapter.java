package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.text.format.DateUtils.formatDateTime;

public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);

        if (message.isFromCurrentUser()) {
            convertView = messageInflater.inflate(R.layout.my_message, null);

        } else {
            convertView = messageInflater.inflate(R.layout.theirs_message, null);
        }
        holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
        holder.time = (TextView) convertView.findViewById(R.id.message_time);
        convertView.setTag(holder);
        holder.messageBody.setText(message.getText());
        holder.time.setText(message.getTimeString());
        return convertView;
    }

    class MessageViewHolder {
        public TextView messageBody;
        public TextView time;

    }

}
