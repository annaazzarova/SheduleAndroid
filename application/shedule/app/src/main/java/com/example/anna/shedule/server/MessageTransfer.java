package com.example.anna.shedule.server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class MessageTransfer {

    public static final String SERVER_ULR = "45.55.200.41:3000";

    private static DefaultHttpClient httpClient;


    public <T> ServerResponse<T> get(Class<T> cs, String url) {
        HttpGet request = new HttpGet(url);
        return execute(request, cs);
    }

    public <T> ServerResponse<T> post(Class<T> cs, String url, Object data) {
        HttpPost request = new HttpPost(url);
        setRequestBodyAsJson(request, data);
        return execute(request, cs);
    }

    public <T> ServerResponse<T> put(Class<T> cs, String url, Object data) {
        HttpPut request = new HttpPut(url);
        setRequestBodyAsJson(request, data);
        return execute(request, cs);
    }

    public <T> ServerResponse<T> delete(Class<T> cs, String url, Object data) {
        HttpDeleteWithBody request = new HttpDeleteWithBody(url);
        setRequestBodyAsJson(request, data);
        return execute(request, cs);
    }

    private void setRequestBodyAsJson(HttpEntityEnclosingRequestBase request, Object data) {
        HttpEntity entity = new ByteArrayEntity(JsonParser.toJson(data));
        request.setEntity(entity);
    }

    private <T> ServerResponse<T> execute(HttpUriRequest request, Class<T> cs) {
        ResponseHandler<ServerResponse<T>> responseHandler = new JsonResponseHandler<T>(cs);
        try {
            HttpClient httpclient = getHttpClient();
            return httpclient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            return new ServerResponse<T>(ServerResponse.NO_CONNECTION_ERROR);
        }
    }

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            CookieStore cookieStore = new BasicCookieStore();
            httpClient.setCookieStore(cookieStore);
        }
        return httpClient;
    }

    private class JsonResponseHandler<T> implements ResponseHandler<ServerResponse<T>> {

        private Class<T> cs;

        public JsonResponseHandler(Class<T> cs){
            this.cs = cs;
        }

        @SuppressWarnings("unchecked")
        public ServerResponse<T> handleResponse(HttpResponse httpResponse) throws IOException {
            ServerResponse<T> response = null;
            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream content = httpResponse.getEntity().getContent();
                response = JsonParser.fromJson(content, ServerResponse.class, cs);
            }

            if (response == null) {
                code = (code == 200)
                        ? ServerResponse.PARSE_RESPONSE_ERROR
                        : ServerResponse.NO_CONNECTION_ERROR;
                response = new ServerResponse<T>(code);
            }

            return response;
        }

    }

    private class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";
        public String getMethod() { return METHOD_NAME; }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }

    }
}
