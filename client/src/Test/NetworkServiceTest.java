package Test;

import Constants.Endpoint;
import Constants.Environment;
import Services.NetworkService;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class NetworkServiceTest {
    private static NetworkService networkService;

    @BeforeAll
    public static void setup() {
        networkService = new NetworkService();
    }

    @Test
    void post_startGame_inTestEnv_shouldReturnParsedJson() {
        JSONObject request = new JSONObject();
        request.put("test", "test");

        JSONObject response = networkService.POST(request, Endpoint.STARTGAME, Environment.TEST);

        assertNotNull(response);
        assertEquals("test", response.getString("test"));
        assertEquals(101, response.getInt("id"));
    }

    @Test
    void post_startGame_inProdEnv_withLevelInBody_shouldReturnEngineStartedMessageAndLevel() {
        JSONObject request = new JSONObject();
        request.put("level", 1);

        JSONObject response = networkService.POST(request, Endpoint.STARTGAME, Environment.PROD);

        assertNotNull(response);
        assertEquals("Engine started and ready.", response.getString("message"));
        assertEquals(1, response.getInt("level"));
    }
}