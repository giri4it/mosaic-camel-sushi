package com.backbase.expert.extensions.sushi;

import org.apache.http.HttpEntity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bartv on 22/10/14.
 */
public class RemoteResponse implements Serializable {


    private final byte[] response;
    private final String id;
    private Map<String, Object> headers;
    private final String encoding;

    public RemoteResponse(String id, byte[] response, String encoding, Map<String, Object> headers) {
        this.id = id;
        this.response = response;
        this.encoding = encoding;
        this.headers = headers;
    }

    public byte[] getResponse() {
        return response;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }


    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    @Override
    public String toString() {
        return "RemoteResponse{" +
                "id=" + id +
                ", response=" + response.length +
                ", headers=" + headers +
                ", encoding='" + encoding + '\'' +
                '}';
    }
}
