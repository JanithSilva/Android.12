package com.example.janithsilva.deelsapp;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class userFoodList extends AppCompatActivity {

    ListView mListView;
    ArrayList<foodModel> mList;
    FoodListAdapter mAdapter = null;

    ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_food_list);

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new FoodListAdapter(this,R.layout.food_row, mList);
        mListView.setAdapter(mAdapter);

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM FOOD_RECORD");
        mList.clear();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            String details = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            mList.add(new foodModel(id, name, price, details,image));
        }

        mAdapter.notifyDataSetChanged();
        if(mList.size() == 0){
            Toast.makeText(this, "No record found", Toast.LENGTH_SHORT).show();

        }

    }

}
