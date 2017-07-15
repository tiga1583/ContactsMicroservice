package com.tilomicroservice.controllers.com.tilomicroservice.controllers;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorResponse {

    private String path;
    private String requestId;
    private String errorId;
    private String errorMessage;
    private String timestamp;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    public ErrorResponse() {
    }

    public ErrorResponse(String errorId, String errorMessage, String path) {
        this.errorId = errorId;
        this.errorMessage = errorMessage;
        this.path = path;
       // this.requestId = (null == TracingUtil.getTraceId()) ? "" : TracingUtil.getTraceId();
        this.timestamp = dateFormat.format(new Date());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "path='" + path + '\'' +
                ", requestId='" + requestId + '\'' +
                ", errorId='" + errorId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public String toErrorString() {
        return "{" +
                "\"path\":\"" + path + "\"" +
                ", \"requestId\":\"" + requestId + "\"" +
                ", \"errorId\":\"" + errorId + "\"" +
                ", \"errorMessage\":\"" + errorMessage + "\"" +
                ", \"timestamp\":\"" + timestamp + "\"" +
                "}";
    }
}
