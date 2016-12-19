package br.com.hisao.restaurantchallenge.Model;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.com.hisao.restaurantchallenge.MainActivity;
import br.com.hisao.restaurantchallenge.Util.Log;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * Created by viniciushisao.
 */

public class Votes {

    private static Votes votesInstance;
    private ArrayList<Vote> voteArrayList;
    private IronIO ironIO;
    private Votes(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cache-aws-us-east-1.iron.io/1/projects/58559c692176810007201121/caches/")
                .build();
        ironIO = retrofit.create(IronIO.class);
        voteArrayList = new ArrayList<>();
        Log.d("Votes:Votes:24 ");

    }

    public static Votes getInstance(){

        if (votesInstance == null){
            votesInstance = new Votes();
        }
        return votesInstance;

    }

    public void loadVotes(final MainActivity.LoadVotesCallback callback){
        Call<ResponseBody> response = ironIO.getAll();
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    BufferedReader reader;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    String values = jsonObject.getString("value");
                    String[] tokens = values.split(TOKEN);

                    for (String s : tokens){
                        if (s != null && s.length() > 0){
                            Vote v = new Vote(s,true);
                            if (v.isValid())
                                voteArrayList.add(v);
                        }
                    }

                    for (Restaurant r : MainActivity.restaurantArrayList){
                        for (Vote v : voteArrayList){
                            if (v.idRestaurant.equalsIgnoreCase(r.getId())){
                                r.incrementVotes(1);
                            }
                        }
                    }


                    callback.onDone();
                    Log.d("Votes:onResponse:74 " + values);
                } catch (Exception e) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Votes:onFailure:53 " + t.getMessage());
            }
        });
    }

    private static String TOKEN = "YYYY";

    public void persistVote (Vote vote){

        String increment = TOKEN + vote.timeOfVoting + vote.idRestaurant;
        for (Vote v : voteArrayList){
            increment = increment +  TOKEN + v.timeOfVoting + v.idRestaurant;
        }
        increment = "{\"value\": \"" +  increment + "\"}";

        Call<ResponseBody> response = ironIO.increment(RequestBody.create(MediaType.parse("application/json"), increment));
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Votes:onResponse:91 " + response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}

interface IronIO {

    @GET("Decisions/items/votes?oauth=EftzTUvuwpkUixwznyp1")
    Call<ResponseBody> getAll();

    @PUT("Decisions/items/votes?oauth=EftzTUvuwpkUixwznyp1")
    Call<ResponseBody> increment(@Body RequestBody increment);
}


