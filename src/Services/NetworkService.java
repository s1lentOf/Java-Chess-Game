package Services;

import Constants.Endpoint;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class NetworkService {
    // TODO: Enter the correct base url after we get one from cloudflare.
    private static final String baseURL = "baseURL/";

    // STUB: A method that handles sending POST requests to the server.
    public static JSONObject POST(JSONObject data, Endpoint endpoint) {
        return null;
    }
}
