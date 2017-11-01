package com.digicaps.openholo.webhook;

import org.json.JSONException;
import org.json.JSONObject;

public class GithubWrapper {

    public String getCloneUrlFormJson(String body) throws JSONException {
        String url = new String();

        JSONObject webhook = new JSONObject(body);
        JSONObject repository = webhook.getJSONObject("repository");
        url = repository.getString("url");

        return url;
    }

    public String getEmailOfPuhserFromJson(String body) throws JSONException {
        JSONObject webhook = new JSONObject(body);
        JSONObject pusher = webhook.getJSONObject("pusher");
        return pusher.getString("email");
    }

    public String getNameOfPuhserFromJson(String body) throws JSONException {
        JSONObject webhook = new JSONObject(body);
        JSONObject pusher = webhook.getJSONObject("pusher");
        return pusher.getString("name");
    }

}
