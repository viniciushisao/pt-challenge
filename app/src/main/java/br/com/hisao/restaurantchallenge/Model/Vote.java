package br.com.hisao.restaurantchallenge.Model;

import java.util.Calendar;

import br.com.hisao.restaurantchallenge.Util.Log;

public class Vote {
    long timeOfVoting;
    String idRestaurant;

    public Vote(String idRestaurant) {
        this.idRestaurant = idRestaurant;
        this.timeOfVoting = System.currentTimeMillis();
    }

    public boolean isValid(){

        Calendar c = Calendar.getInstance();

        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DATE) - 1, 13, c.get(Calendar.MINUTE) );

        long yesterday = c.get(Calendar.MILLISECOND);

        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DATE), 13, c.get(Calendar.MINUTE) );

        long today = c.get(Calendar.MILLISECOND);

        if (yesterday < this.timeOfVoting && this.timeOfVoting < today){
            return true;
        }
        return true;
    }

    public Vote(String completeDate, boolean isCompleteDate) {

        int i = 1;
        for (; i < completeDate.length(); i++){
            if (!isLong(completeDate.substring(0, i))){
                i--;
                break;

            }
        }
        String time = completeDate.substring(0, i);

        idRestaurant = completeDate.substring(i, completeDate.length());
        try {
            timeOfVoting = Long.parseLong(time);
        } catch (Exception e) {
            Log.d("Vote:Vote:19 " + e.getMessage());
        }
    }

    private boolean isLong(String s){

        try {
            Long.parseLong(s);
            return true;
        } catch (Exception e) {

        }
        return false;

    }

    @Override
    public String toString() {
        return Long.toString(timeOfVoting) + idRestaurant;

    }
}
