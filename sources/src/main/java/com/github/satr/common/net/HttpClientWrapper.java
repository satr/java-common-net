package com.github.satr.common.net;
// Copyright © 2019, github.com/satr, MIT License

import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

/**
 * The interface of a wrapper around a HTTP client with boilerplate code for request execution.
 */
public interface HttpClientWrapper {
    /**
     * Execute a http-request.
     * @httpClientAction    The {@link HttpClientAction} provides {@link HttpUriRequest}
     *                      and process a respond in the {@link HttpClientAction#processRespond} method.
     */
    void executeRequest(HttpClientAction httpClientAction) throws IOException;
}
