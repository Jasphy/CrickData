package com.web.sample.web;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorResponse {
	
	private String path;
    private String errorId;
    private String errorMessage;
    private String timestamp;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    public ErrorResponse(String errorId, String errorMessage, String path) {
        this.errorId = errorId;
        this.errorMessage = errorMessage;
        this.path = path;
        this.timestamp = dateFormat.format(new Date());
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "path='" + path + '\'' +
                ", errorId='" + errorId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }


	

}
