package com.android.stocktake;

import com.android.stocktake.stocktake;

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


public class StockEdit extends Activity
{
    sqliteWrapper db;
    int active_id;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        try
        {
            String path = getFilesDir().getAbsolutePath();
            db = new sqliteWrapper(path + "/test");
        }
        catch (NullPointerException e)
        {
            Log.v("Stock edit", "db init failed");
        }

        nextItem();
    }

    private void nextItem()
    {
        resultSet set = db.execute("select * from stocks where go_check = 1 order by id");

        if(set.checkError() == 0)
        {
            if(set.getRowCount() > 0)
            {
                Log.v("Stock edit", "rows: " + set.getRowCount());

                TextView name = (TextView)findViewById(R.id.Name);
                name.setText(set.getValue("name", 0));

                TextView discription = (TextView)findViewById(R.id.Description);
                discription.setText(set.getValue("description", 0));

                EditText stock = (EditText)findViewById(R.id.Stock);
                stock.setText(set.getValue("stock", 0));

                active_id = Integer.parseInt(set.getValue("id", 0).trim()); 
            }
            else
            {
                Intent i = new Intent(this, stocktake.class);
                startActivity(i);
            }
        }
        else
        {
            Log.v("Stock edit", " " + set.getErrorMsg());
        }
        set.delete();
    }

/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * Event handlers
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    public void Next(View view) {
        Log.v("Stock edit", "button working");

        EditText stock_obj = (EditText)findViewById(R.id.Stock);

        String stock_str = stock_obj.getText().toString().trim();

        if(stock_str != "")
        {
            int stock = Integer.parseInt(stock_str);

            Log.v("Stock edit", "stock: " + stock);

            // update db

            String sql = "update stocks set stock = " + stock + ", go_check = 0  where id = " + active_id;

            Log.v("Stock edit", "sql: " + sql);

            resultSet set = db.execute(sql);
            if(set.checkError() == 1)
            {
                Log.v("Stock edit", "update fail");
            }

            set.delete();

            nextItem();
        }
    }
}
