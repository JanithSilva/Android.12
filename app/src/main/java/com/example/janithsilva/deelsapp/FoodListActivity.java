package com.example.janithsilva.deelsapp;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class FoodListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<foodModel> mList;
    FoodListAdapter mAdapter = null;
    Button addRec;

    public static SQLiteHelper mSQLiteHelper;

    ImageView imageViewIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        mSQLiteHelper = new SQLiteHelper(this,"DEELSDB.sqlite",null,1);

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS FOOD_RECORD(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, price VARCHAR, details VARCHAR, image BLOB)");


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Food List");

        mListView = findViewById(R.id.listView);
        mList = new ArrayList<>();
        mAdapter = new FoodListAdapter(this,R.layout.food_row, mList);
        mListView.setAdapter(mAdapter);
        addRec = findViewById(R.id.addRec);

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

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                final CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(FoodListActivity.this);

                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM FOOD_RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));

                            }
                            showDialogUpdate(FoodListActivity.this, arrID.get(position));

                        }
                        if(i == 1){
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM FOOD_RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));

                        }
                    }
                });
                dialog.show();
                return true;
            }
        });

        addRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FoodListActivity.this,food_input.class));

            }
        });
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(FoodListActivity.this);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Do you really want to delete?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    food_input.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(FoodListActivity.this,"Deleted successfully",Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    Log.e("Error", e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(Activity activity, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.food_update_dialog);
        dialog.setTitle("Update");

        imageViewIcon = dialog.findViewById(R.id.imageViewRecord);
        final EditText editName = dialog.findViewById(R.id.editName);
        final EditText editPrice = dialog.findViewById(R.id.editPrice);
        final EditText editDetails = dialog.findViewById(R.id.editDetails);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM FOOD_RECORD WHERE id="+position);
        mList.clear();

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            editName.setText(name);

            String price = cursor.getString(2);
            editPrice.setText(price);

            String details = cursor.getString(3);
            editDetails.setText(details);

            byte[] image = cursor.getBlob(4);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));

            mList.add(new foodModel(id, name, price, details,image));
        }

        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.9);

        dialog.getWindow().setLayout(width,height);
        dialog.show();

        imageViewIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        FoodListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    food_input.mSQLiteHelper.updateData(
                            editName.getText().toString().trim(),
                            editPrice.getText().toString().trim(),
                            editDetails.getText().toString().trim(),
                            food_input.imageViewToByte(imageViewIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Updated successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception error){
                    Log.e("Update error",error.getMessage());
                }
                updateRecordList();
            }
        });
    }

    private void updateRecordList() {
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM FOOD_RECORD");
        mList.clear();
        while (cursor.moveToNext()){

            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String price = cursor.getString(2);
            String details = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            mList.add(new foodModel(id, name, price, details, image));

        }
        mAdapter.notifyDataSetChanged();
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
        if(requestCode == 888){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);

            }else{
                Toast.makeText(this,"Do not have permission to access file location", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 888 && resultCode == RESULT_OK){
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
                imageViewIcon.setImageURI(resultUri);

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
