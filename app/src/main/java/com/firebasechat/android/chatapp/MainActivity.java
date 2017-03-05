package com.firebasechat.android.chatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    //DECLARE ALL THE FIELDS
    EditText roomName;
    Button createRoom;
    ListView roomList;
    ArrayList<String> roomArrayList;
    ArrayAdapter<String> roomAdapter;

    //DATABASE REFERENCE FIELD FOR FIREBASE
    DatabaseReference databaseReference;
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DEFINE REFERENCES FOR ALL THE VIEWS
        roomName = (EditText) findViewById(R.id.editText);
        createRoom = (Button) findViewById(R.id.button3);
        roomList = (ListView) findViewById(R.id.roomListView);

        roomArrayList = new ArrayList<String>();
        roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomArrayList);

        roomList.setAdapter(roomAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot();

        request_name();

        //CREATE AN ENTRY FOR NEW ROOM IN FIREBASE DATABASE ON BUTTON CLICK
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> map = new  HashMap<String, Object>();
                map.put(roomName.getText().toString(), "");
                databaseReference.updateChildren(map);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator iterator = dataSnapshot.getChildren().iterator();

                Set<String> set = new HashSet<String>();

                while(iterator.hasNext())
                {

                    //GET NAMES OF ALL THE ROOMS ONE BY ONE FROM YOUR DATABASE
                    set.add( (String) ((DataSnapshot)iterator.next()).getKey());

                }

                roomArrayList.clear();
                roomArrayList.addAll(set);

                roomAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, Chat_room.class);
                intent.putExtra("Room_name", ((TextView)view).getText().toString());
                intent.putExtra("User_name", userName);
                startActivity(intent);

            }
        });


    }


    //REQUEST USER NAME ON ALERT DIALOG
    private void request_name() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter your name");
        final EditText editText = new EditText(this);

        builder.setView(editText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                  userName = editText.getText().toString();

                   if(!TextUtils.isEmpty(userName))
                   {


                   }
                   else
                   {
                       request_name();

                   }


            }
        }).setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                  dialog.cancel();
                 request_name();

            }
        });

        builder.show();
    }




}
