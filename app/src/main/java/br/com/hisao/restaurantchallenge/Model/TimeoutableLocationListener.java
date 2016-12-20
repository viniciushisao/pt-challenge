package br.com.hisao.restaurantchallenge.Model;

/**
 * Created by viniciushisao
 */

import java.util.Timer;
import java.util.TimerTask;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;


public class TimeoutableLocationListener implements LocationListener {
    protected Timer timerTimeout = new Timer();
    protected LocationManager locaMan = null;
    protected TimeoutLisener timeoutLisener;


    public TimeoutableLocationListener(LocationManager locaMan, long timeOutMS,
                                       final TimeoutLisener timeoutListener) {
        this.locaMan = locaMan;
        this.timeoutLisener = timeoutListener;
        this.timerTimeout.schedule(new TimerTask() {

            @Override
            public void run() {
                if (timeoutListener != null) {
                    timeoutListener.onLocationChanged(null);
                }
                stopLocationUpdateAndTimer();
            }
        }, timeOutMS);
    }


    @Override
    public void onLocationChanged(Location location) {

        if (this.timeoutLisener != null) {
            this.timeoutLisener.onLocationChanged(location);
        }
        stopLocationUpdateAndTimer();
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    private void stopLocationUpdateAndTimer() {
        if (locaMan != null)
            locaMan.removeUpdates(this);
        if (timerTimeout != null) {
            timerTimeout.cancel();
            timerTimeout.purge();
            timerTimeout = null;
        }
    }

    public interface TimeoutLisener {
        void onLocationChanged(Location location);
    }
}
