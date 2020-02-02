package com.sunsolution.coronamonitor;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunsolution.coronamonitor.models.City;

import java.util.ArrayList;
import java.util.List;

public class ItemCityAdapter extends RecyclerView.Adapter<ItemCityAdapter.ItemCityViewHolder> {

    private List<City> cities;

    public ItemCityAdapter(List<City> cities) {
        if (cities == null)
            this.cities = new ArrayList<>();
        else
            this.cities = cities;

    }

    public void replaceCities(List<City> cities) {
        if (cities != null){
            this.cities.clear();
            this.cities.addAll(cities);
            notifyDataSetChanged();
        }
    }



    @NonNull
    @Override
    public ItemCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemCityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vietnam_sick,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCityViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    class ItemCityViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCity,tvCounter;

        public ItemCityViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCity = itemView.findViewById(R.id.tvCity);
            tvCounter = itemView.findViewById(R.id.tvCounter);
        }

        public void onBind(int position) {
            City city = cities.get(position);
            if (!TextUtils.isEmpty(city.getName()))
                tvCity.setText(city.getName());
            if (!TextUtils.isEmpty(city.getCounter()))
                tvCounter.setText(city.getCounter());
        }
    }

}
