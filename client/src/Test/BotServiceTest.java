package Test;

import Constants.Environment;
import Services.BotService;
import Services.NetworkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BotServiceTest {
    @Test
    @DisplayName("Test that best move is retrieved from the real server")
    void testBestMove() {
        NetworkService networkService = new NetworkService();
        BotService botService = new BotService(networkService, Environment.PROD, 1);

        botService.getBestMove(List.of("d2d4"));
    }

    @Test
    @DisplayName("Test that start session connects to the real server")
    void testStartSession() {
        NetworkService networkService = new NetworkService();
        BotService botService = new BotService(networkService, Environment.PROD, 1);
        botService.startSession();
    }

    @Test
    @DisplayName("Test thta connection is stopped after the corresponding method")
    void testStopSession() {
        NetworkService networkService = new NetworkService();
        BotService botService = new BotService(networkService, Environment.PROD, 1);
        botService.endSession();
    }


}
