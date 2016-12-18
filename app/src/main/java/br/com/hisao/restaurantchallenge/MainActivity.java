package br.com.hisao.restaurantchallenge;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.hisao.restaurantchallenge.Model.Restaurant;
import br.com.hisao.restaurantchallenge.Model.RestaurantListBaseAdapter;
import br.com.hisao.restaurantchallenge.Util.Log;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.POST;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llLoading;
    private ListView lstRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        llLoading.setVisibility(View.VISIBLE);

        lstRestaurants = (ListView) findViewById(R.id.lst_restaurant);
        lstRestaurants.setVisibility(View.GONE);

        YelpAPIFactory apiFactory = new YelpAPIFactory("lWuDsW3aIGmVT3alTW7XmQ", "QTrQHUOpGVZwSfTBEa5hUFzhqCA", "bczWgEjYIFy8PH9NKaZWOIAmk4yq6pVK", "pzKC6jaGTCCtt_XoM46KGsFpzL8");
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();


        params.put("term", "food");
        params.put("limit", "3");
        params.put("lang", "en");

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(37.7577)
                .longitude(-122.4376).build();
        Call<SearchResponse> call = yelpAPI.search(coordinate, params);


        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                try {
                    ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                    int totalNumberOfResult = searchResponse.total();  // 3
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

                    lstRestaurants.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.GONE);

                    lstRestaurants.setAdapter(new RestaurantListBaseAdapter(restaurantArrayList, getApplicationContext()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.d("error" + t.getMessage());
            }
        };

        call.enqueue(callback);


        //https://cache-aws-us-east-1.iron.io/1/projects/58559c692176810007201121/caches?oauth=EftzTUvuwpkUixwznyp1
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cache-aws-us-east-1.iron.io/1/projects/58559c692176810007201121/caches/")

                .build();

        IronIO ironIO = retrofit.create(IronIO.class);
        //   Call<ResponseBody> response = ironIO.clear();
        //  Log.d("vindincidciad" + response.toString());


    }

    public interface IronIO {
        @POST("Decisions/clear?oauth=EftzTUvuwpkUixwznyp1")
        Call<ResponseBody> clear();
    }


}
