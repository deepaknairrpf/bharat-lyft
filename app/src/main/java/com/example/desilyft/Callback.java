package com.example.desilyft;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sriablaze on 19/01/19.
 */

public interface Callback {
    void handleResponse(HashMap<String, Object> response);
}
