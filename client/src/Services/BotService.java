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
        JSONObject response = networkService.POST(payload, Endpoint.REQUESTMOVE, environment);
        return decodeInputData(extractMove(response));

    }

    public void startSession() {
        JSONObject payload = new JSONObject();
        payload.put("level", level);
        JSONObject resp = networkService.POST(payload, Endpoint.STARTGAME, environment);
        System.out.println("[startSession] sent=" + payload + " resp=" + resp);
    }

    public void endSession() {
        networkService.POST(new JSONObject(),Endpoint.STOPGAME, environment);
    }

    private JSONObject buildPayload(List<String> moves) {
        JSONObject payload = new JSONObject();
        payload.put("move",moves.getLast());

        return payload;
    }

    // this method extracts the field from jsonbject witha key "move"
    private String extractMove(JSONObject json) {
//        if (!json.getBoolean("message")) {
//            throw new RuntimeException("Engine error");
//        }
        String raw = json.getString("bestmove");        // "bestmove e2e4 ponder e7e5"
//        String[] parts = raw.split("\\s+");
//        if (parts.length < 2) {
//            throw new IllegalArgumentException("Unexpected bestmove line: " + raw);
//        }
        return raw;
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
