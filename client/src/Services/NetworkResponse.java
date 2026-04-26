package Services;

import org.json.JSONObject;

// A helper class, that stores information about each response from the server.
public class NetworkResponse {
    private final int statusCode;
    private final JSONObject body;

    public NetworkResponse(int statusCode, JSONObject body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JSONObject getBody() {
        return body;
    }
}