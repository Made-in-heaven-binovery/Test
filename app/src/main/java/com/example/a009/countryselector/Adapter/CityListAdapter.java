package com.example.a009.countryselector.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a009.countryselector.R;
import com.example.a009.countryselector.RealmModel.CountryModel;
import com.example.a009.countryselector.ShowInfoCountry;

/**
 * Created by 009 on 22.05.2017.
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {

    private static CountryModel city;

    public CityListAdapter(CountryModel city) {
        this.city = city;
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        CityViewHolder holder = new CityViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        holder.city_item.setText(city.getCity().get(position).getCity());
    }


    @Override
    public int getItemCount() {
        return city.getCity().size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView city_item;
        CityViewHolder(final View itemView) {
            super(itemView);
            city_item = (TextView) itemView.findViewById(R.id.item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), ShowInfoCountry.class).putExtra("city",""+city.getCity().get(getAdapterPosition()).getCity()));
                }
            });
        }
    }
}
