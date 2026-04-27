package Services;

import ChessPieces.IndexPosition;
import Constants.Endpoint;
import Constants.Environment;
import org.json.JSONObject;
import java.util.List;

public class BotService {
    private NetworkService networkService;
    private Environment environment;
    private final int level;

    public BotService(NetworkService networkService, Environment environment, int level) {
        this.networkService = networkService;
        this.environment = environment;
        this.level = level;
    }

    public EngineMove getBestMove(List<String> moves) {
        JSONObject payload = buildPayload(moves);
        NetworkResponse response = networkService.POST(payload, Endpoint.REQUESTMOVE, environment);

        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            return decodeInputData(extractMove(response));
        } else {
            System.out.println(response.getBody().getString("message"));
            return null;
        }
    }

    public void startSession() {
        JSONObject payload = new JSONObject();
        payload.put("level", level);
        NetworkResponse response = networkService.POST(payload, Endpoint.STARTGAME, environment);

        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            System.out.println("Engine has been successfully started.");
        } else {
            System.out.println(response.getBody().getString("message"));
        }
    }

    public void endSession() {
        NetworkResponse response = networkService.POST(null, Endpoint.STOPGAME, environment);

        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            System.out.println("Engine has been successfully stopped.");
        } else {
            System.out.println(response.getBody().getString("message"));
        }
    }

    private JSONObject buildPayload(List<String> moves) {
        JSONObject payload = new JSONObject();
        if (moves.isEmpty()) {
            payload.put("move", "");
            return payload;
        }
        payload.put("move", moves.getLast());
        return payload;
    }

    // this method extracts the field from json object with a key "bestmove"
    private String extractMove(NetworkResponse response) {
        return response.getBody().getString("bestmove");
    }

    private EngineMove decodeInputData(String uci) {
        if (uci == null || uci.equals("(none)") || uci.equals("0000")) {
            return null;
        }
        if (uci.length() != 4 && uci.length() != 5) {
            throw new IllegalArgumentException("Invalid UCI move: " + uci);
        }
        int fromCol = uci.charAt(0) - 'a';
        int fromRow = uci.charAt(1) - '1';
        int toCol = uci.charAt(2) - 'a';
        int toRow = uci.charAt(3) - '1';

        Character promotion = uci.length() == 5 ? uci.charAt(4) : null;

        return new EngineMove(
                new IndexPosition(fromRow, fromCol),
                new IndexPosition(toRow, toCol),
                promotion
        );
    }


}
