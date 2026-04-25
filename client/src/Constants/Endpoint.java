package Constants;

public enum Endpoint {
    STARTGAME ("/posts", "/api/start"),
    STOPGAME ("/posts","/api/stop"),
    REQUESTMOVE ( "/posts","/api/request");

    private final String testPath;
    private final String prodPath;

    Endpoint(String testPath, String prodPath) {
        this.testPath = testPath;
        this.prodPath = prodPath;
    }

    public String getPath(Environment env) {
        return env == Environment.TEST ? testPath : prodPath;
    }
}