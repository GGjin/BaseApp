package com.gg.baseapp.utils;


import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GG on 2017/5/24.
 */

public class JsonUtils {

    public static String getJsonArrayStringFromStringList(List<String> stringList) {
        return getJsonArrayFromStringList(stringList).toString();
    }

    public static JSONArray getJsonArrayFromStringList(List<String> stringList) {
        JSONArray array = new JSONArray();
        for (String str : stringList) {
            array.put(str);
        }
        return array;
    }

    public static void cleanDirtyJsonObject(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            cleanDirtyJsonObject(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static void cleanDirtyJsonObject(JSONObject json) {
        List<String> names = new ArrayList<>();
        try {
            if (json == null)
                return;
            for (int i = 0; i < json.names().length(); i++) {
                names.add(json.names().getString(i));
            }
            for (String name : names) {
                if ("[]".equals(json.optString(name)) || "null".equals(json.optString(name)) || "{}".equals(json.optString(name)) || "[{}]".equals(json.optString(name)) || "[[]]".equals(json.optString(name))) {
                    json.remove(name);
                } else {
                    Object object = json.get(name);
                    if (object instanceof JSONArray) {
                        cleanDirtyJsonArray((JSONArray) object);
                    } else if (object instanceof JSONObject) {
                        cleanDirtyJsonObject((JSONObject) object);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void cleanDirtyJsonArray(JSONArray array) {
        try {
            for (int i = 0; i < array.length(); i++) {
                Object object = array.get(i);
                if (object instanceof JSONObject) {
                    cleanDirtyJsonObject((JSONObject) object);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void cleanDirtyJsonReader(JsonReader reader) {

    }
}
