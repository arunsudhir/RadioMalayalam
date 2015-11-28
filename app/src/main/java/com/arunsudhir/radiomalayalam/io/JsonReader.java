package com.arunsudhir.radiomalayalam.io;

import com.arunsudhir.radiomalayalam.logging.Logger;
import com.google.common.io.CharStreams;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Class to read response from a URL as a JSON
 */
public class JsonReader {
    private static final Logger LOG = new Logger(JsonReader.class);
    private static final String RESPONSE_ENCODING = "iso-8859-1";

    public static JSONObject getRemoteJsonData(String url) {
        return new JsonReader().getJSONData(url);
    }

    public JSONObject getJSONData(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return getJsonResponse(client, url);
        } catch (Exception e) {
            LOG.error(e, "Failed to handle JSON request to URL '%s'", url);
        }
        return null;
    }

    private JSONObject getJsonResponse(CloseableHttpClient httpClient, String url) throws IOException, JSONException {
        HttpPostHC4 post = new HttpPostHC4(url);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            return new JSONObject(getResponseAsString(response));
        }
    }

    private String getResponseAsString(CloseableHttpResponse response) throws IOException {
        return CharStreams.toString(new InputStreamReader(response.getEntity().getContent(), RESPONSE_ENCODING));
    }
}
