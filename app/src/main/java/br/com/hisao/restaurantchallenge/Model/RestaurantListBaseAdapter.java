package br.com.hisao.restaurantchallenge.Model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        final Restaurant restaurant = restaurantArrayList.get(i);

        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        tvName.setText(restaurant.getName());
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
        tvAddress.setText(restaurant.getAddress());

        TextView tvVotes = (TextView) view.findViewById(R.id.tv_votes);
        tvVotes.setText("Votes: " + restaurant.getVotes());

        ImageView ivRestaurantImage = (ImageView) view.findViewById(R.id.img_restaurant);

        Picasso.with(context).load(restaurant.getImageUrl()).into(ivRestaurantImage);

        LinearLayout llVote = (LinearLayout) view.findViewById(R.id.ll_vote);
        llVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getRootView().getContext())
                        .setTitle("Are you sure?")
                        .setMessage("Votes cannot be undone")
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Vote vote = new Vote(restaurant.getId());
                                Votes.getInstance().persistVote(vote);
                            }
                        })
                        .setNegativeButton("I don't know", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        return view;
    }
}
