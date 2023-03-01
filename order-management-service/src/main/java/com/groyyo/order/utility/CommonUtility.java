package com.groyyo.order.utility;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

@Slf4j
public class CommonUtility {
    public static JSONObject response() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "success");
        return jsonObject;
    }
}
