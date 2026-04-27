package Test;

import ChessPieces.IndexPosition;
import Constants.Endpoint;
import Constants.Environment;
import Services.BotService;
import Services.EngineMove;
import Services.NetworkResponse;
import Services.NetworkService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BotServiceTest {
    @Test
    @DisplayName("Test that best move is retrieved")
    void testBestMove() {
        NetworkService stubNetwork = new NetworkService() {
            @Override
            public NetworkResponse POST(JSONObject data, Endpoint endpoint, Environment environment) {
                JSONObject body = new JSONObject();
                body.put("bestmove", "e2e4");
                return new NetworkResponse(200, body);
            }
        };

        BotService botService = new BotService(stubNetwork, Environment.PROD, 1);

        EngineMove move = botService.getBestMove(List.of("d2d4"));

        assertNotNull(move);
        IndexPosition from = move.getFrom();
        IndexPosition to = move.getTo();
        assertEquals(1, from.getRow());
        assertEquals(4, from.getCol());
        assertEquals(3, to.getRow());
        assertEquals(4, to.getCol());
        assertNull(move.getPromotion());
    }


}
