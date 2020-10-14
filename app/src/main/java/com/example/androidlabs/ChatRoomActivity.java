package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom);
        final Button sendmsg = findViewById(R.id.sendMessage);
        final Button receivemsg = findViewById(R.id.receiveMessage);
        final ListView myList = findViewById(R.id.messageList);
        final MyListAdapter a = new MyListAdapter();
        myList.setAdapter(a);
        sendmsg.setOnClickListener((click) -> {
            Messages m = new Messages(findViewById(R.id.messageText).toString(),true);
            a.addMessage(m);
            a.notifyDataSetChanged();
        });
        receivemsg.setOnClickListener((click) -> {
            Messages m = new Messages(findViewById(R.id.messageText).toString(),false);
            a.addMessage(m);
            a.notifyDataSetChanged();
        });

    }
    public class Messages{
        private String message;
        private boolean sender;
        public Messages(String msg, boolean s){
            message = msg;
            sender = s;
        }
        public String getMessage(){
            return message;
        }
        public boolean getSender(){
            return sender;
        }
    }

    private class MyListAdapter extends BaseAdapter {
        public ArrayList<Messages> elements = new ArrayList<>();
        public void addMessage(Messages m){
            elements.add(m);
        }
        public int getCount() {
            return elements.size();
        }

        public Object getItem(int position) {
            return elements.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View old, ViewGroup parent) {
            View newView = old;
            LayoutInflater inflater = getLayoutInflater();
            if (newView == null) {
                newView = inflater.inflate(R.layout.chatroom, parent, (false));
            }
            if (elements.get(position).getSender()) {
                Button tView = newView.findViewById(R.id.sent_message_text);
                tView.setText( elements.get(position).getMessage() );
            } else {
                Button tView = newView.findViewById(R.id.received_message_text);
                tView.setText(getItem(position).toString());
            }            return newView;
        }
    }
}