package com.example.janithsilva.deelsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

public class food_input extends AppCompatActivity {

    EditText mEdtName, mEdtPrice, mEdtDetails;
    Button mBtnAdd, mBtnList;
    ImageView mImageView;

    final int REQUEST_CODE_GALLERY = 999;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);

        mEdtName = findViewById(R.id.editName);
        mEdtPrice = findViewById(R.id.editPrice);
        mEdtDetails = findViewById(R.id.editDetails);
        mBtnAdd = findViewById(R.id.btnAdd);
        mBtnList = findViewById(R.id.btnList);
        mImageView = findViewById(R.id.imageView);

        mSQLiteHelper = new SQLiteHelper(this,"DEELSDB.sqlite",null,1);

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD_RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, details VARCHAR, image BLOB)");

        mImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {

                ActivityCompat.requestPermissions(
                        food_input.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_GALLERY
                );

            }
        });


        mEdtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mEdtName.getText().length() < 1){
                    mEdtName.setError("Name can not be empty!");
                }
            }
        });

        mEdtPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mEdtPrice.getText().length() < 1){
                    mEdtPrice.setError("Price can not be empty!");
                }
            }
        });

        mEdtDetails.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(mEdtDetails.getText().length() < 1){
                    mEdtDetails.setError("Details can not be empty!");
                }
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(mEdtName.getText().length() > 4 && mEdtPrice.getText().length() > 2)  {
                        mSQLiteHelper.insertData(
                                mEdtName.getText().toString().trim(),
                                mEdtPrice.getText().toString().trim(),
                                mEdtDetails.getText().toString().trim(),
                                imageViewToByte(mImageView)
                        );
                        Toast.makeText(food_input.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(food_input.this, "Addition Failed due to blank or insufficient inputs", Toast.LENGTH_SHORT).show();
                    }
                    mEdtName.setText("");
                    mEdtPrice.setText("");
                    mEdtDetails.setText("");
                    mImageView.setImageResource(R.drawable.add_photo);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        mBtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(food_input.this,FoodListActivity.class));

            }
        });

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);

            }else{
                Toast.makeText(this,"Do not have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).
                    setGuidelines(CropImageView.Guidelines.ON).
                    setAspectRatio(1,1).
                    start(this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                mImageView.setImageURI(resultUri);

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
