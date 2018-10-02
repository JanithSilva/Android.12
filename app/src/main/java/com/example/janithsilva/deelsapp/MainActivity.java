package com.example.janithsilva.deelsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    CardView foodCard;
    Button loginBtn;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSQLiteHelper = new SQLiteHelper(this,"DEELSDB.sqlite",null,1);

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD_RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, details VARCHAR, image BLOB)");


        foodCard = findViewById(R.id.foodIcon);
        loginBtn = findViewById(R.id.loginBtn);

        foodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,userFoodList.class));

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this,login.class));

            }
        });
    }

//    public void onClickCardFood(View v) {
//
//        Intent ob = new Intent(this, FoodListActivity.class);
//        startActivity(ob);
//
//    }


}
    /* public void onClickRegister(View v){

        Intent ob = new Intent(this, register.class);
        startActivity(ob);

    }

    public void onClickLogin(View v){

        Intent ob = new Intent(this, login.class);
        startActivity(ob);

    }

    public void onClickCardElectronic(View v){

        Intent ob = new Intent(this, electroniclist.class);
        startActivity(ob);

    }

    public void onClickCardTravel(View v){

        Intent ob = new Intent(this, travellist.class);
        startActivity(ob);

    }

    public void onClickCardFashion(View v){

        Intent ob = new Intent(this, fashionlist.class);
        startActivity(ob);

    }

    public void onClickAdminButton(View v){

        Intent ob = new Intent(this, mainAdminPanel.class);
        startActivity(ob);

    }



}
*/