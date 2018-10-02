package com.example.janithsilva.deelsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodListAdapter extends BaseAdapter{

    private Context context;
    private int layout;
    private ArrayList<foodModel> foodList;

    public FoodListAdapter(Context context, int layout, ArrayList<foodModel> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int i) {
        return foodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtPrice, txtDetails;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        ViewHolder holder = new ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtPrice = row.findViewById(R.id.txtPrice);
            holder.txtDetails = row.findViewById(R.id.txtDetails);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);

        }else{
            holder = (ViewHolder)row.getTag();
        }

        foodModel foodModel = foodList.get(i);

        holder.txtName.setText(foodModel.getName());
        holder.txtPrice.setText(foodModel.getPrice());
        holder.txtDetails.setText(foodModel.getDetails());

        byte[] recordImage = foodModel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0,recordImage.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
