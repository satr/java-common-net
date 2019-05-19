package com.github.satr.common.net;
// Copyright © 2019, github.com/satr, MIT License

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class ApacheHttpClientWrapper {
    private final PatternLayout patternLayout = new PatternLayout("%d [%t] %-5p %c - %m%n");
    private Optional<Logger> optionalLogger = Optional.empty();

    public void executeRequest(HttpClientAction httpClientAction) throws IOException {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClientBuilder.create().build();

            HttpUriRequest httpRequest = httpClientAction.getHttpRequest();
            logDebug("[Request]:" + httpRequest.toString());
            logDebug("[Request body]:" + getBodyIfExists(httpRequest));

            CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest);
            logDebug("[Respond]:" + closeableHttpResponse.toString());

            int statusCode = closeableHttpResponse.getStatusLine().getStatusCode();
            logDebug("[Respond code]:" + statusCode);
            HttpEntity entity = closeableHttpResponse.getEntity();
            String respondBody = entity != null ? EntityUtils.toString(entity) : null;
            logDebug("[Respond body]:" + respondBody);
            closeableHttpResponse.close();

            httpClientAction.processRespond(statusCode, respondBody);
        } finally {
            if(httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logError(e);
                }
            }
        }
    }

    private String getBodyIfExists(HttpUriRequest httpRequest) throws IOException {
        HttpEntityEnclosingRequestBase requestWithBody = (HttpEntityEnclosingRequestBase)httpRequest;
        return requestWithBody == null ? "N/A" : EntityUtils.toString(requestWithBody.getEntity());
    }

    private void logError(Throwable e) {
        optionalLogger.ifPresent(l -> l.error(e));
    }

    private void logDebug(String message) {
        optionalLogger.ifPresent(l -> l.debug(message));
    }

    public void logToConsole() {
        initLogger();
        optionalLogger.ifPresent(l -> l.addAppender(new ConsoleAppender(patternLayout)));
    }

    public void logToFile(String filePath) {
        initLogger();
        optionalLogger.ifPresent(l -> {
            try {
                l.addAppender(new FileAppender(patternLayout, filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initLogger() {
        if (optionalLogger.isPresent()) {
            return;
        }
        Properties log4jProp = new Properties();
        log4jProp.setProperty("log4j.rootLogger", "DEBUG");
        log4jProp.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Log4JLogger");
        PropertyConfigurator.configure(log4jProp);
        optionalLogger = Optional.of(Logger.getLogger(ApacheHttpClientWrapper.class));
    }
}
