package Test;

import Constants.Endpoint;
import Constants.Environment;
import Services.NetworkResponse;
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

        NetworkResponse response = networkService.POST(request, Endpoint.STARTGAME, Environment.TEST);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode());
        assertEquals("test", response.getBody().getString("test"));
        assertEquals(101, response.getBody().getInt("id"));
    }

    @Test
    void post_startGame_inProdEnv_engineNotStartedPreviously_withLevelInBody_shouldReturnEngineStartedMessageAndLevel() {
        JSONObject request = new JSONObject();
        request.put("level", 1);

        NetworkResponse response = networkService.POST(request, Endpoint.STARTGAME, Environment.PROD);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Engine started and ready.", response.getBody().getString("message"));
        assertEquals(1, response.getBody().getInt("level"));
    }

    @Test
    void post_startGame_inProdEnv_engineAlreadyStarted_withLevelInBody_shouldReturnErrorMessage() {
        JSONObject request = new JSONObject();
        request.put("level", 1);

        NetworkResponse response = networkService.POST(request, Endpoint.STARTGAME, Environment.PROD);

        assertNotNull(response);
        assertEquals(409, response.getStatusCode());
        assertEquals("A game is already running.", response.getBody().getString("message"));
    }

    @Test
    void post_startGame_inProdEnv_engineAlreadyStarted_withoutLevelInBody_shouldReturnEngineStartedMessageAndMaximumLevel() {
        JSONObject request = new JSONObject();

        NetworkResponse response = networkService.POST(request, Endpoint.STARTGAME, Environment.PROD);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Engine started and ready.", response.getBody().getString("message"));
        assertEquals(20, response.getBody().getInt("level"));
    }

    @Test
    void post_stopGame_inProdEnv_engineAlreadyStarted_shouldReturnEngineStoppedMessage() {
        JSONObject request = new JSONObject();

        NetworkResponse response = networkService.POST(request, Endpoint.STOPGAME, Environment.PROD);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals("Engine stopped.", response.getBody().getString("message"));
    }

    @Test
    void post_stopGame_inProdEnv_engineWasNotRunning_shouldReturnErrorMessage() {
        JSONObject request = new JSONObject();

        NetworkResponse response = networkService.POST(request, Endpoint.STOPGAME, Environment.PROD);

        assertNotNull(response);
        assertEquals(409, response.getStatusCode());
        assertEquals("No game is running.", response.getBody().getString("message"));
    }

    @Test
    void post_requestMove_inProdEnv_engineWasNotRunning_shouldReturnErrorMessage() {
        JSONObject request = new JSONObject();

        NetworkResponse response = networkService.POST(request, Endpoint.REQUESTMOVE, Environment.PROD);

        assertNotNull(response);
        assertEquals(409, response.getStatusCode());
        assertEquals("No game is running.", response.getBody().getString("message"));
    }

    @Test
    void post_requestMove_inProdEnv_engineIsRunning_shouldReturnBestMove() {
        JSONObject request = new JSONObject();
        request.put("move", "e2e4"); // Last player's move.

        NetworkResponse response = networkService.POST(request, Endpoint.REQUESTMOVE, Environment.PROD);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getBody().getString("bestmove"));
    }
}