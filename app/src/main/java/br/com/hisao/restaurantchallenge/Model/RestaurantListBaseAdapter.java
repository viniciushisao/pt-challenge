package br.com.hisao.restaurantchallenge.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.hisao.restaurantchallenge.R;

/**
 * Created by vinicius on 12/18/16.
 */

public class RestaurantListBaseAdapter extends BaseAdapter {

    private ArrayList<Restaurant> restaurantArrayList;
    private Context context;

    public RestaurantListBaseAdapter(ArrayList<Restaurant> restaurantArrayList, Context context){
        this.restaurantArrayList = restaurantArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.restaurantArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return restaurantArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.restaurant_item, viewGroup, false);
        }

        Restaurant restaurant = restaurantArrayList.get(i);

        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        tvName.setText(restaurant.getName());
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvAddress.setText(restaurant.getAddress());

        TextView tvVotes = (TextView) view.findViewById(R.id.tv_votes);
        TextView tvVote = (TextView) view.findViewById(R.id.tv_vote);

        ImageView ivRestaurantImage = (ImageView) view.findViewById(R.id.img_restaurant);

        Picasso.with(context).load(restaurant.getImageUrl()).into(ivRestaurantImage);

        return view;
    }
}
