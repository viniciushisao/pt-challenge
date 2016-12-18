package br.com.hisao.restaurantchallenge.Model;

import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.hisao.restaurantchallenge.MainActivity;
import br.com.hisao.restaurantchallenge.Util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vinicius on 12/18/16.
 */

public class LoadRestaurants {

    public static void loadRestaurants(double latitude, double longitute, final MainActivity.RestaurantCallback restaurantCallback) {
        Log.d("MainActivity:loadRestaurants:96 ");
        Map<String, String> params = new HashMap<>();

        params.put("term", "food");
        params.put("limit", "5");
        params.put("lang", "en");

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(latitude)
                .longitude(longitute).build();
        Call<SearchResponse> call = MainActivity.yelpAPI.search(coordinate, params);

        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                try {
                    ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                    ArrayList<Business> businessArrayList = searchResponse.businesses();
                    for (Business busines : businessArrayList) {
                        String id = busines.id();
                        String name = busines.name();
                        String address = busines.location().displayAddress().toString();
                        String imageUrl = busines.imageUrl();

                        Restaurant restaurant = new Restaurant(id, name, address, imageUrl);
                        Log.d("MainActivity:onResponse:65 " + restaurant.toString());
                        restaurantArrayList.add(restaurant);
                    }
                    restaurantCallback.onDone(restaurantArrayList);
                } catch (Exception e) {
                    Log.d("MainActivity:onResponse:131 " + e.getMessage());
                    restaurantCallback.onError(e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d("MainActivity:onFailure:138 " + t.getMessage());
                restaurantCallback.onError(t.getMessage());
            }
        };
        call.enqueue(callback);
    }
}
