package com.backbase.expert.extensions.sushi.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for getting a HasMap of parameters from a query string.
 *
 * @author dirk
 * Date: 11-7-13
 */
public class QueryStringUtil {

    public static final String ENCODING_UTF_8 = "UTF-8";

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    public static String mapToQueryString(Map<String, String> queryString) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> e : queryString.entrySet()){
            if(sb.length() > 0){
                sb.append('&');
            }
            sb.append(URLEncoder.encode(e.getKey(), ENCODING_UTF_8)).append('=').append(URLEncoder.encode(e.getValue(), "UTF-8"));
        }
        return sb.toString();
    }

    public static void printParameters(URL url) {
        String query = url.getQuery();
        Map<String, String> map = getQueryMap(query);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println("Name=" + key);
            System.out.println("Value=" + map.get(key));
        }
    }
}
