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

    // STUB: A method that handles sending POST requests to the server.
    public static JSONObject POST(JSONObject data, Endpoint endpoint, Environment environment) {
        try {
            URL url = new URL(environment.getBaseURL() + endpoint.getPath(environment));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(data.toString().getBytes());
            }

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            connection.disconnect();

            return new JSONObject(stringBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
