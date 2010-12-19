package com.android.stocktake;

import com.android.stocktake.StockEdit;

import sqliteWrapper.sqliteWrapper;
import sqliteWrapper.resultSet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;


public class stocktake extends Activity
{
    sqliteWrapper db;

    stocktake thiz;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        thiz = this;

        resultSet set;


        try
        {
            String path = getFilesDir().getAbsolutePath();
            db = new sqliteWrapper(path + "/test");

            set = db.execute("create table if not exists stocks "+
                "(id INTEGER PRIMARY KEY, name TEXT, description TEXT, "+
                "stock INTEGER, go_check INTEGER);");

            if(set.checkError() == 1)
                Log.v("Stock take", "sql error: " + set.getErrorMsg());

            set.delete();

            set = db.execute("select * from stocks;");

            if(set.checkError() == 0)
            {
                Log.v("Stock take", "no error " + set.getRowCount());

                resultSet set1;

                if(set.getRowCount() == 0)
                {
                    db.execute("insert into stocks (name, description, go_check, stock) values ('obj1', 'object 1', 1, 0);");
                    db.execute("insert into stocks (name, description, go_check, stock) values ('obj2', 'object 2', 1, 0);");
                    db.execute("insert into stocks (name, description, go_check, stock) values ('obj3', 'object 3', 1, 0);");
                    db.execute("insert into stocks (name, description, go_check, stock) values ('obj4', 'object 4', 1, 0);");
                }
            }
            else
                Log.v("Stock take", "sql error: " + set.getErrorMsg());

            set.delete();
        }
        catch (NullPointerException e)
        {
            Log.v("Stock take", "db init failed");
        }


        refresh_ui();


    // assign event handlers
        Button btn_continue = (Button)findViewById(R.id.Continue);
        btn_continue.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent i = new Intent(thiz, StockEdit.class);
                startActivity(i);
            }});
    }

    public void refresh_ui()
    {
        TextView console = (TextView)findViewById(R.id.message);

        resultSet set = db.execute("select * from stocks where go_check = 1");

        if(set.checkError() == 0)
        {
            int rows = set.getRowCount();

            Button btn_continue = (Button)findViewById(R.id.Continue);

            if(rows > 0)
            {
                console.setText(rows + " items need to be counted");
                btn_continue.setVisibility(0);
            }
            else
            {
                console.setText("Nothing to be done");
                btn_continue.setVisibility(8);
            }
        }

        set.delete();
    }

    public void Reset(View view) {
        resultSet set = db.execute("update stocks set go_check = 1;");
        set.delete();
        refresh_ui();
    }
}
