package utils;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by neerajlajpal on 24/02/18.
 */

public interface VolleyCallback {
    void onSuccessResponse(JSONObject result);
    void onErrorResponse(VolleyError result);
}
