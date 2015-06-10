package com.example.anna.shedule.server.utils;

import com.example.anna.shedule.server.cookies.CookieManager;
import com.example.anna.shedule.server.cookies.CustomCookieStore;

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
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class MessageTransfer {

    private static DefaultHttpClient httpClient;

    public static ResponseWithStatusCode get(String url) {
        HttpGet request = new HttpGet(url);
        return execute(request);
    }

    public static ResponseWithStatusCode post(String url, Object data) {
        HttpPost request = new HttpPost(url);
        setRequestBodyAsJson(request, data);
        return execute(request);
    }

    public static ResponseWithStatusCode put(String url, Object data) {
        HttpPut request = new HttpPut(url);
        setRequestBodyAsJson(request, data);
        return execute(request);
    }

    public static ResponseWithStatusCode delete(String url, Object data) {
        HttpDeleteWithBody request = new HttpDeleteWithBody(url);
        setRequestBodyAsJson(request, data);
        return execute(request);
    }

    public static ResponseWithStatusCode delete(String url) {
        HttpDeleteWithBody request = new HttpDeleteWithBody(url);
        return execute(request);
    }


    private static void setRequestBodyAsJson(HttpEntityEnclosingRequestBase request, Object data) {
        HttpEntity entity = new ByteArrayEntity(JsonParser.toJson(data));
        request.setHeader("Content-Type", "application/json");
        request.setEntity(entity);
    }

    private static synchronized ResponseWithStatusCode execute(HttpUriRequest request) {
        CustomResponseHandler responseHandler = new CustomResponseHandler();
        try {
            HttpClient httpclient = getHttpClient();
            ResponseWithStatusCode response = httpclient.execute(request, responseHandler);
            saveCookies();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseWithStatusCode(ResponseWithStatusCode.NO_INTERNET_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseWithStatusCode(ResponseWithStatusCode.FATAL_ERROR);
        }
    }

    private synchronized static void saveCookies() {
        CustomCookieStore cookieStore = (CustomCookieStore) httpClient.getCookieStore();
        CookieManager.saveCookiesToFile(cookieStore);
    }

    private static DefaultHttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
            CookieStore cookieStore = CookieManager.getCookieFromFile();
            httpClient.setCookieStore(cookieStore);
        }
        return httpClient;
    }

    public synchronized static void clearCookies() {
        CookieManager.clearCookies(httpClient);
    }

    private static class CustomResponseHandler implements ResponseHandler<ResponseWithStatusCode> {

        @SuppressWarnings("unchecked")
        public ResponseWithStatusCode handleResponse(HttpResponse httpResponse) throws IOException {
            int code = httpResponse.getStatusLine().getStatusCode();
            InputStream content = httpResponse.getEntity().getContent();
            return new ResponseWithStatusCode(code, content);
        }
    }

    private static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";
        public String getMethod() { return METHOD_NAME; }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }
    }
}
