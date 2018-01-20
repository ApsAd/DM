package com.example.android.dm;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ADITYA on 1/20/2018.
 */
public class DisplayActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);
        TextView t=(TextView)findViewById(R.id.data);

        Cursor cursor = Registration1.db.getAllUsers();
        String ans="";
        while(cursor.moveToNext()){
            ans+=cursor.getString(cursor.getColumnIndex("userid"));
            ans+="\n";
            ans+=cursor.getString(cursor.getColumnIndex("name"));
            ans+="\n";
            ans+=cursor.getString(cursor.getColumnIndex("email"));
            ans+="\n";
            ans+=cursor.getString(cursor.getColumnIndex("age"));
            ans+="\n";
            ans+=cursor.getString(cursor.getColumnIndex("gender"));
            ans+="\n";
            ans+=cursor.getString(cursor.getColumnIndex("phonenumber"));
            ans+="\n";
            ans+=cursor.getString(cursor.getColumnIndex("preference"));
            ans+="\n";
        }
     t.setText(ans);
    }
}
