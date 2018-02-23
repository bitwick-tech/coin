package utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.common.base.Joiner;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import tech.smartcrypto.neeraj.coin.Coin;
import tech.smartcrypto.neeraj.coin.R;
import tech.smartcrypto.neeraj.coin.fragments.WatchlistFragment;

/**
 * Created by neerajlajpal on 05/02/18.
 */

public class ServerInteractionHandler {
    public static final String url = "http://www.smartcrypto.tech/coins/";
    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result);
    }

    // ctx must be application context
    public static void getCoinsDataFromServer(Set<Coin> coinSet, Context ctx) {
        // Get a RequestQueue
        RequestQueue queue = SingletonVolleyRequestQueue.getInstance(ctx).
                getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UtilFunctions.saveCoinsToWatchlistDB(UtilFunctions.convertJSONObjectToCoinsArray(response), ctx);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        SingletonVolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsObjRequest);
    }

    public static void getCoinsDataFromServerForAlertDB(Set<String> coinIdSet, Context ctx) {
        // Get a RequestQueue
        RequestQueue queue = SingletonVolleyRequestQueue.getInstance(ctx).
                getRequestQueue();
        String queryString = "?q=" + Joiner.on(",").join(coinIdSet);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url + queryString, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UtilFunctions.updateCoinsInAlertsDB(UtilFunctions.convertJSONObjectToCoinsArray(response), ctx);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        SingletonVolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsObjRequest);


    }



    public static void getResponseFromServer(int method, String url, JSONObject jsonValue, final VolleyCallback callback, Context mCtx) {
        RequestQueue queue = SingletonVolleyRequestQueue.getInstance(mCtx).getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, jsonValue, new Response.Listener < JSONObject > () {

            @Override
            public void onResponse(JSONObject Response) {
                callback.onSuccessResponse(Response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                //e.printStackTrace();
                //Toast.makeText(mCtx, e + "error", Toast.LENGTH_LONG).show();
            }
        })
        {
            // set headers
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError {
                Map < String, String > params = new HashMap<>();
                //params.put("Authorization: Basic", TOKEN);
                return params;
            }
        };
        SingletonVolleyRequestQueue.getInstance(mCtx).addToRequestQueue(jsonObjectRequest);
    }

    public static void getFrequencyDataFromServer(Context ctx) {
        // Get a RequestQueue
        RequestQueue queue = SingletonVolleyRequestQueue.getInstance(ctx).
                getRequestQueue();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, ctx.getResources().getString(R.string.frequencyApiUrl), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Map<String,String> h = new HashMap<>(2);
                        if(response.optString("a") == null) {
                            PriceTrackerAlarmTrigger.ALARM_TIME_FREQUENCY = Integer.parseInt(UtilFunctions.getDataFromSharedPref(ctx, "a_f"));
                            WatchlistFragment.updateInterval = Integer.parseInt(UtilFunctions.getDataFromSharedPref(ctx,"w_f"));
                            return;
                        }
                        else {
                            PriceTrackerAlarmTrigger.ALARM_TIME_FREQUENCY = response.optInt("a");
                            WatchlistFragment.updateInterval = response.optInt("w");
                        }
                        h.put("w_f", response.optString("w"));
                        h.put("a_f", response.optString("a"));
                        UtilFunctions.addDataToSharedPref(h, ctx);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        SingletonVolleyRequestQueue.getInstance(ctx).addToRequestQueue(jsObjRequest);
    }
}



