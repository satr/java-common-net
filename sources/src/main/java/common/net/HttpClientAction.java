package common.net;
// Copyright © 2019, github.com/satr, MIT License

import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public abstract class HttpClientAction {
    public abstract HttpUriRequest getHttpRequest() throws IOException;
    public abstract void processRespond(int statusCode, String respondBody) throws IOException;
}
