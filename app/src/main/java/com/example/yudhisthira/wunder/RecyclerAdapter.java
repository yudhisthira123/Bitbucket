package com.example.yudhisthira.wunder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudhisthira on 09/05/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemHolder>{

    private List<Car> mCarList = new ArrayList<>();

    public RecyclerAdapter() {

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Car car = mCarList.get(position);
        holder.mCarName.setText(car.getCarName());
        holder.mCarAddress.setText(car.getCarAddress());

        holder.mCoordinates.setText("(" + car.getLatitude() + " , " + car.getLongitude() + ")");

    }

    @Override
    public int getItemCount() {
        int size = 0;

        if(null != mCarList) {
            size = mCarList.size();
        }

        return size;
    }

    public void setData(List<Car> carList) {
        mCarList = carList;
        notifyDataSetChanged();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mCarName;
        private TextView mCarAddress;
        private TextView mCoordinates;

        public ItemHolder(View v) {
            super(v);

            mCarName = (TextView) v.findViewById(R.id.carName);
            mCarAddress = (TextView) v.findViewById(R.id.carAddres);
            mCoordinates = (TextView) v.findViewById(R.id.coordinates);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }
    }
}
