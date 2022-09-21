package com.code.kakaobank.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ApiSupportUtil {
    /**
     * ==============================================================================================
     * QueryString 만들기 Util
     * ----------------------------------------------------------------------------------------------
     */
    public String paramToQueryString(Map<String, Object> paramMap){
        List<NameValuePair> params = new ArrayList<>();
        if(paramMap != null){
            for(Map.Entry<String, Object> paramEntry : paramMap.entrySet()){
                Object value = paramEntry.getValue();
                if(value != null){
                    params.add(new BasicNameValuePair(paramEntry.getKey(), value.toString()));
                }
            }
        }
        return URLEncodedUtils.format(params, "UTF-8");
    }

    /**
     * ==============================================================================================
     * http Util
     * ----------------------------------------------------------------------------------------------
     */
    public String handleResponse(HttpsURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        boolean responseError = false;
        InputStream is;
        if(responseCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
            is = con.getInputStream();
        } else {
            is = con.getErrorStream();
            responseError = true;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if(responseError) {
            System.out.println(responseError);
        }

        return response.toString();
    }
}
