package com.github.satr.common.net;
// Copyright © 2019, github.com/satr, MIT License

import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

/**
 * Provides the http-request parameters and process a respond of the request.
 */
public interface HttpClientAction {
    /**
     * Provides a http-request parameters.
     * */
    HttpUriRequest getHttpRequest() throws IOException;

    /**
     * Process a respond in the http-request.
     * */
    void processRespond(int statusCode, String respondBody) throws IOException;
}
