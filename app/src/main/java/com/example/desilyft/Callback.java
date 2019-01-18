package com.example.desilyft;

import org.json.JSONObject;

/**
 * Created by sriablaze on 19/01/19.
 */

public interface Callback {
    void handleResponse(JSONObject response);
}
