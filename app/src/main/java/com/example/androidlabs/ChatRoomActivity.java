package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidlabs.database.MessageRepository;
import com.example.androidlabs.models.Message;
import com.example.androidlabs.models.Message.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    DetailsFragment fragment;
    private final List<Message> messages = new ArrayList<>();
    MessageListAdapter messageListAdapter = new MessageListAdapter();

    MessageRepository messageRepository;
    private boolean onTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Check if is on a tablet.
        if (findViewById(R.id.frame) != null) {
            onTablet = true;
        }

        messageRepository = new MessageRepository(this);

        setListeners();
        load();
    }

    /**
     * Loads the data from database and notifies the adapter.
     */
    private void load() {
        messages.addAll(messageRepository.findAll());
        messageListAdapter.notifyDataSetChanged();
    }

    /**
     * Set event listeners on Views of this Activity.
     */
    private void setListeners() {
        // Set the list's adapter: how it will function.
        ListView messageList = findViewById(R.id.list);
        messageList.setAdapter(messageListAdapter);

        // Set actions for send and receive button.
        EditText text = findViewById(R.id.message);
        findViewById(R.id.send).setOnClickListener(v -> {
            addMessage(text.getText().toString(), Type.SENT);
            text.setText(null);
        });
        findViewById(R.id.receive).setOnClickListener(v -> {
            addMessage(text.getText().toString(), Type.RECEIVED);
            text.setText(null);
        });

        // Set long click listener for each item in the row.
        messageList.setOnItemLongClickListener(((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.
                setTitle(getString(R.string.delete))
                .setMessage(
                    getString(R.string.row) + (position + 1) + "\n" +
                        getString(R.string.database) + messageListAdapter.getItemId(position)
                )
                .setPositiveButton(getString(R.string.yes), (click, arg) -> {
                    messageRepository.delete((Message) messageListAdapter.getItem(position));
                    messages.remove(position);
                    // Close fragment if it exists.
                    if (onTablet && fragment != null) {
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    messageListAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.no, (click, arg) -> {
                })
                .show();

            return false;
        }));

        messageList.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            Message message = messages.get(position);
            dataToPass.putString("message", message.getText());
            dataToPass.putLong("id", message.getId());
            dataToPass.putBoolean("status", message.getType().ordinal() == 0);
            dataToPass.putBoolean("onTablet", onTablet);

            if (onTablet) {
                fragment = new DetailsFragment();
                fragment.setArguments(dataToPass);
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
            } else {
                Intent nextActivity =
                    new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }

        });

    }

    /**
     * Add message to messages database and list, then notifies the adapter.
     *
     * @param text the message to add
     * @param type the type of message
     */
    private void addMessage(String text, Type type) {
        Message message = new Message(text, type);
        messages.add(messageRepository.save(message));
        messageListAdapter.notifyDataSetChanged();
    }

    private class MessageListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int position) {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ((Message) getItem(position)).getId();
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View newView = view;
            LayoutInflater inflater = getLayoutInflater();

            if (messages.get(position).getType() == Type.SENT) {
                newView = inflater.inflate(R.layout.row_send, parent, false);
            } else if (messages.get(position).getType() == Type.RECEIVED) {
                newView = inflater.inflate(R.layout.row_receive, parent, false);
            }

            TextView messageText = newView.findViewById(R.id.message);
            messageText.setText(messages.get(position).getText());

            return newView;
        }
    }
}
