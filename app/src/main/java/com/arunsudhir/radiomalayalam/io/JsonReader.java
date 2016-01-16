package com.arunsudhir.radiomalayalam.io;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import com.arunsudhir.radiomalayalam.NoConnectionActivity;
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
    private Activity containingActivity;
    private ProgressDialog pDialog;
    public JsonReader(AsyncTaskPreAndPostExecutor executor)
    {
        this.containingActivity = executor.getContainingActivity();
        this.pDialog = executor.getProgressDialog();
    }

    public JSONObject getRemoteJsonData(String url) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            return getJsonResponse(client, url);
        }
        // indicates that JSON Failed. Load the no internet activity
        catch (Exception e) {
            LOG.error(e, "Failed to handle JSON request to URL '%s'", url);

            //dismiss a progress dialog if it exists
            if(pDialog !=null)
            {
                pDialog.dismiss();
            }

            Intent noInternetIntent = new Intent(containingActivity, NoConnectionActivity.class);
            containingActivity.startActivity(noInternetIntent);
        }
        return null;
    }

    private JSONObject getJsonResponse(CloseableHttpClient httpClient, String url) throws IOException, JSONException {
        HttpPostHC4 post = new HttpPostHC4(url);
        try (CloseableHttpResponse response = httpClient.execute(post)) {
            String json = getResponseAsString(response);
            return new JSONObject(json);
        }
    }

    private String getResponseAsString(CloseableHttpResponse response) throws IOException {
        return CharStreams.toString(new InputStreamReader(response.getEntity().getContent(), RESPONSE_ENCODING));
    }
}
