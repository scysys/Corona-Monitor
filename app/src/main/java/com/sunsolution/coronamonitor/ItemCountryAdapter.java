package com.sunsolution.coronamonitor;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.sunsolution.coronamonitor.models.Country;

import java.util.ArrayList;
import java.util.List;

public class ItemCountryAdapter extends RecyclerView.Adapter<ItemCountryAdapter.ItemCountryViewHolder> {

    private List<Country> countries;

    public ItemCountryAdapter(List<Country> countries) {
        if (countries == null)
            this.countries = new ArrayList<>();
        else
            this.countries = countries;

    }

    public void replaceCountries(List<Country> countries) {
        if (countries != null){
            this.countries.clear();
            this.countries.addAll(countries);
            notifyDataSetChanged();
        }
    }



    @NonNull
    @Override
    public ItemCountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemCountryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country_sick,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCountryViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    class ItemCountryViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCountry,tvCounter;
        private ImageView ivFlag;

        public ItemCountryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvCounter = itemView.findViewById(R.id.tvCounter);
            ivFlag = itemView.findViewById(R.id.ivFlag);
        }

        public void onBind(int position) {
            Country country = countries.get(position);
            if (!TextUtils.isEmpty(country.getName()))
                tvCountry.setText(country.getName());
            if (!TextUtils.isEmpty(country.getCounter()))
                tvCounter.setText(country.getCounter());
            if (!TextUtils.isEmpty(country.getFlag()))
                Picasso.get().load(country.getFlag()).into(ivFlag);
        }
    }

}
