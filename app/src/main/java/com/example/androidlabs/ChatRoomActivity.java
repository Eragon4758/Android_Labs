package com.example.androidlabs;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidlabs.models.Message;
import com.example.androidlabs.models.Message.Type;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChatRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatRoomActivity";
    private final List<Message> messages = new ArrayList<>();
    MessageListAdapter messageListAdapter = new MessageListAdapter();
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setListeners();
        loadDataFromDatabase();
        messageListAdapter.notifyDataSetChanged();
    }

    /**
     * Set event listeners on Views of this Activity
     */
    private void setListeners() {
        // Set the list's adapter: how it will function.
        ListView messageList = findViewById(R.id.list);
        messageList.setAdapter(messageListAdapter);

        // Set actions for send and receive button.
        EditText text = findViewById(R.id.message);
        findViewById(R.id.send).setOnClickListener(v -> {
            //add to database here. get Id into a variable and send id to addMessage
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_SENDER, "SENT");
            newRowValues.put(MyOpener.COL_MESSAGE,text.getText().toString());
            long id = db.insert(MyOpener.TABLE_NAME, "NullColumnName", newRowValues);
            addMessage( Type.SENT, text.getText().toString(), id);
            text.setText(null);
        });
        findViewById(R.id.receive).setOnClickListener(v -> {
            //add to database here. get Id into a variable and send id to addMessage
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_SENDER, "RECEIVED");
            newRowValues.put(MyOpener.COL_MESSAGE,text.getText().toString());
            long id = db.insert(MyOpener.TABLE_NAME, "NullColumnName", newRowValues);
            addMessage(Type.RECEIVED, text.getText().toString(), id);
            text.setText(null);
        });

        // Set long click listener for each item in the row.
        messageList.setOnItemLongClickListener(((parent, view, position, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.
                    setTitle(getString(R.string.delete))
                    .setMessage(
                            getString(R.string.row) + position + "\n" +
                                    getString(R.string.database) + messageListAdapter.getItemId(position)
                    )
                    .setPositiveButton(getString(R.string.yes), (click, arg) -> {
                        //delete from database here
                        db.delete(MyOpener.TABLE_NAME, "_id=?", new String[] {Long.toString(messageListAdapter.getItemId(position))});
                        messages.remove(position);

                        messageListAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, (click, arg) -> {
                    })
                    .show();

            return false;
        }));
    }

    /**
     * Add message to messages list and notifies the adapter.
     *
     * @param text the message to add
     * @param type the type of message
     */
    private void addMessage( Type type, String text, long ID) {
        Message message = new Message(type, text, ID);
        messages.add(message);
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
            return messages.get(position).getID();
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
    protected void loadDataFromDatabase(){
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_SENDER, MyOpener.COL_MESSAGE};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        int IDIndex = results.getColumnIndex(MyOpener.COL_ID);
        int SenderIndex = results.getColumnIndex(MyOpener.COL_SENDER);
        int MessageIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        while(results.moveToNext())
        {
            String typeS = results.getString(SenderIndex);
            String msg = results.getString(MessageIndex);
            long id = results.getLong(IDIndex);
            Type t;
            if (typeS.equals("SENT")){
                t = Type.SENT;
            }
            else{
                t = Type.RECEIVED;
            }
            //add the new Contact to the array list:
            messages.add(new Message(t, msg, id));
        }
        printCursor(results);
    }
    protected void printCursor( Cursor c ){
        c.moveToFirst();
        Log.v(TAG,"Database version number: " + db.getVersion());
        Log.v(TAG,"Number of columns in cursor: " + c.getColumnCount());
        Log.v(TAG,"Columns in cursor" + c.getColumnNames());
        Log.v(TAG,"Number of results: " + c.getCount());
        Log.v(TAG,"rows: ");
        while(c.moveToNext()){
            Log.v(TAG,c.getString(0) + " | " + c.getString(1)+" | " + c.getString(2));
        }
    }
}