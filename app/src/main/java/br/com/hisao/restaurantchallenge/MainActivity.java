package br.com.hisao.restaurantchallenge;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;

import java.util.ArrayList;

import br.com.hisao.restaurantchallenge.Model.LoadRestaurants;
import br.com.hisao.restaurantchallenge.Model.Restaurant;
import br.com.hisao.restaurantchallenge.Model.RestaurantListBaseAdapter;
import br.com.hisao.restaurantchallenge.Model.TimeoutableLocationListener;
import br.com.hisao.restaurantchallenge.Model.Votes;
import br.com.hisao.restaurantchallenge.Util.Log;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llLoading;
    private ListView lstRestaurants;
    public static YelpAPI yelpAPI;
    public static ArrayList<Restaurant> restaurantArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        llLoading = (LinearLayout) findViewById(R.id.ll_loading);


        lstRestaurants = (ListView) findViewById(R.id.lst_restaurant);

        YelpAPIFactory apiFactory = new YelpAPIFactory("lWuDsW3aIGmVT3alTW7XmQ", "QTrQHUOpGVZwSfTBEa5hUFzhqCA", "bczWgEjYIFy8PH9NKaZWOIAmk4yq6pVK", "pzKC6jaGTCCtt_XoM46KGsFpzL8");
        yelpAPI = apiFactory.createAPI();


    }

    @Override
    protected void onResume() {
        super.onResume();

        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            Log.d("MainActivity:loadLocation:82 YOU NEED PERMISSON");
            return;
        }
        loadDataIntoUI();
    }

    boolean firstTime = true;

    private void loadLocation(final LocationCallback locationCallback) {

        firstTime = true;
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity:loadLocation:82 YOU NEED PERMISSON");
            return;
        }

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        final LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                Log.d("MainActivity:onLocationChanged:88 ");
                if (firstTime) {
                    Log.d("MainActivity:onLocationChanged:90 ");
                    locationCallback.onDone(location);
                    firstTime = false;
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                Log.d("MainActivity:onProviderEnabled:101 ");
            }

            @Override
            public void onProviderDisabled(String s) {
                Log.d("MainActivity:onProviderDisabled:106 ");
            }
        };

        final LocationListener locationListenerTimeOut = new TimeoutableLocationListener(mLocationManager, 10000, new TimeoutableLocationListener.TimeoutLisener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    Log.e("MainActivity:onStatusChanged:101 ");
                    location = new Location("");
                    location.setLatitude(37.7577);
                    location.setLongitude(-122.4376);

                    runOnUiThread(new Runnable(){

                        @Override
                        public void run(){
                            Toast.makeText(getApplicationContext(), "GPS failed. You are in San Francisco!", Toast.LENGTH_LONG);
                        }
                    });

                }
                firstTime = false;
                locationCallback.onDone(location);
            }
        });

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerTimeOut);
    }

    public void loadDataIntoUI() {

        llLoading.setVisibility(View.VISIBLE);
        lstRestaurants.setVisibility(View.GONE);
        Log.d("MainActivity:loadDataIntoUI:138 ");

        final LoadVotesCallback loadVotesCallback = new LoadVotesCallback() {
            @Override
            public void onDone() {
                Log.d("MainActivity:onDone:117 ");
                lstRestaurants.setVisibility(View.VISIBLE);
                llLoading.setVisibility(View.GONE);
                lstRestaurants.setAdapter(new RestaurantListBaseAdapter(MainActivity.restaurantArrayList, MainActivity.this));
            }

            @Override
            public void onError(String errorString) {
                Log.d("MainActivity:onError:125 " + errorString);
            }
        };

        final RestaurantCallback restaurantCallback = new RestaurantCallback() {
            @Override
            public void onDone(ArrayList<Restaurant> restaurantArrayList) {
                Log.d("MainActivity:onDone:132 ");
                MainActivity.restaurantArrayList = restaurantArrayList;
                Votes.getInstance().loadVotes(loadVotesCallback);
            }

            @Override
            public void onError(String errorString) {
                Log.d("MainActivity:onError:139 " + errorString);
                retryMessage();
            }
        };

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onDone(Location location) {
                Log.d("MainActivity:onDone:134 ");
                LoadRestaurants.loadRestaurants(location.getLatitude(), location.getLongitude(), restaurantCallback);
            }

            @Override
            public void onError(String errorString) {
                Log.d("MainActivity:onError:153 " + errorString);
            }
        };
        loadLocation(locationCallback);

    }

    private void retryMessage() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Ooops")
                .setMessage("Something wrong happened! Let's retry?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        loadDataIntoUI();
                    }
                })
                .setNegativeButton("Close app", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public interface RestaurantCallback {
        void onDone(ArrayList<Restaurant> restaurantArrayList);

        void onError(String errorString);
    }

    public interface LocationCallback {
        void onDone(Location location);

        void onError(String errorString);
    }

    public interface LoadVotesCallback {
        void onDone();

        void onError(String errorString);
    }


}
