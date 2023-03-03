package com.groyyo.order.management.utility;

import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtility {

	public static JSONObject response() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "success");
		return jsonObject;
	}
}
