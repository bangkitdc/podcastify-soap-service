package com.podcastify.utils;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String url;
    private Map<String, Object> params = new HashMap<>();
    private String method = "GET";
    private Map<String, String> headers = new HashMap<>();
    private String response;
    private int statusCode;

    public Request(String url){
        this.url = url;
    }

    public String getResponse(){
        return this.response;
    }

    public int getStatusCode(HttpURLConnection conn) throws IOException {
        return this.statusCode;
    }

    public void setMethod(String method){
        this.method = method;
    }

    public void addParam(String key, Object value){
        this.params.put(key, value);
    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

    private byte[] buildPostData() throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        for(Map.Entry<String,Object> param : this.params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        return postData.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String getStringResponse(HttpURLConnection conn) throws IOException {
        this.statusCode = conn.getResponseCode();

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();
        this.response = response;
        return response;
    }

    public String send() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(this.url).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod(this.method);

        for(Map.Entry<String, String> header : this.headers.entrySet()) {
            conn.setRequestProperty(header.getKey(), header.getValue());
        }

        if(this.method.equals("POST")) {
            byte[] postDataBytes = this.buildPostData();
            conn.getOutputStream().write(postDataBytes);
        }

        return this.getStringResponse(conn);
    }
}
