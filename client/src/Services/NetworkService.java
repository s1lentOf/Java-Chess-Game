package Services;

import Constants.Endpoint;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Constants.Environment;
import org.json.JSONObject;

public class NetworkService {
    public NetworkService() {}

    // Main method of POST request, that combines the helper method and act as a brain of POST HTTP method.
    public NetworkResponse POST(JSONObject data, Endpoint endpoint, Environment environment) {
        try {
            HttpURLConnection connection = createConnection(endpoint, environment);

            writeBody(connection, data);

            int statusCode = connection.getResponseCode();
            String responseBody = readResponse(connection);

            connection.disconnect();

            return new NetworkResponse(
                    statusCode,
                    new JSONObject(responseBody)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new NetworkResponse(500, new JSONObject());
        }
    }

    // Helper method to create a new HTTP connection.
    private HttpURLConnection createConnection(Endpoint endpoint, Environment environment) throws Exception {
        URL url = new URL(environment.getBaseURL() + endpoint.getPath(environment));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }

    // Helper method to write a body of the request.
    private void writeBody(HttpURLConnection connection, JSONObject data) throws Exception {
        if (data != null) {
            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.toString().getBytes());
            }
        }
    }

    // Helper method to read a server response efficiently.
    private String readResponse(HttpURLConnection connection) throws Exception {
        InputStream stream;

        // Additional validation for the status code.
        if (connection.getResponseCode() >= 400) {
            stream = connection.getErrorStream();
        } else {
            stream = connection.getInputStream();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();
        return builder.toString();
    }
}
