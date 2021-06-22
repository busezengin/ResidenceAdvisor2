package com.kotlinegitim.residenceadvisor.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kotlinegitim.residenceadvisor.R;
import com.kotlinegitim.residenceadvisor.classes.Message;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Message> {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final ArrayList<Message> messages;
    private final Activity context;

    public MessageAdapter(ArrayList<Message> messages, Activity context) {
        super(context,android.R.layout.simple_list_item_1,messages);
        this.messages = messages;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.message_item,null,true );
        TextView messageTextView = null;

        if(messages.get(position).getSender().equals(firebaseAuth.getCurrentUser().getUid())){
            messageTextView = customView.findViewById(R.id.right_relative).findViewById(R.id.messageTextViewRight);
            customView.findViewById(R.id.right_relative).setVisibility(View.VISIBLE);
        } else {
            messageTextView = customView.findViewById(R.id.left_relative).findViewById(R.id.messageTextViewLeft);
            customView.findViewById(R.id.left_relative).setVisibility(View.VISIBLE);
        }

        messageTextView.setText(messages.get(position).getMessage());
        return customView;
    }
}
