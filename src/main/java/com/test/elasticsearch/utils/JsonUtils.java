package com.test.elasticsearch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * Common Util for all operations related to JSON
 */
public class JsonUtils {
    private static Gson gson;
    private static ObjectMapper objMapper;
    static {
        gson = new Gson();
        objMapper = new ObjectMapper();
    }


    /**
     * Used to convert object to Json string
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /**
     * Used to convert Json String to Object based on the convertTo param
     *
     * @param json
     * @param convertTo
     * @return
     */
    public static Object fromJson(String json, Class convertTo) {
        return gson.fromJson(json, convertTo);
    }

    /**
     * Method to compare two same object based on their value by converting into Json String
     *
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean objectEquals(Object obj1, Object obj2){ return toJson(obj1).equals(toJson(obj2));}


    public static String JsonToString(Object object){
        try {
            return objMapper.writeValueAsString(object);
        } catch (Exception ex) {
            return "";
        }

    }
}

