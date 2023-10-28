package com.podcastify.constant;

import com.podcastify.model.ResponseModel;

import java.sql.SQLException;

public class Response {
    public static final int HTTP_STATUS_OK = 200;
    public static final int HTTP_STATUS_CREATED = 201;
    public static final int HTTP_STATUS_ACCEPTED = 202;
    public static final int HTTP_STATUS_MOVED_PERMANENTLY = 301;
    public static final int HTTP_STATUS_FOUND = 302;
    public static final int HTTP_STATUS_BAD_REQUEST = 400;
    public static final int HTTP_STATUS_UNAUTHORIZED = 401;
    public static final int HTTP_STATUS_FORBIDDEN = 403;
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_STATUS_NOT_ACCEPTABLE = 406;
    public static final int HTTP_STATUS_PAYLOAD_TOO_LARGE = 413;
    public static final int HTTP_STATUS_UNSUPPORTED_MEDIA_TYPE = 415;
    public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;

    public static ResponseModel createResponse(int statusCode, String message) {
        ResponseModel response = new ResponseModel();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        return response;
    }

    public static ResponseModel createResponse(Exception e) {
        if (e instanceof SecurityException)
            return Response.createResponse(Response.HTTP_STATUS_UNAUTHORIZED, e.getMessage());
        if (e instanceof SQLException)
            return Response.createResponse(Response.HTTP_STATUS_BAD_REQUEST, e.getMessage());
        else {
            String errMessage = e.getMessage() == null ? "internal server error" : e.getMessage();
            return Response.createResponse(Response.HTTP_STATUS_INTERNAL_SERVER_ERROR, errMessage);
        }
    }

    public static ResponseModel createResponse(int statusCode, String message, Object data) {
        ResponseModel response = new ResponseModel();
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
