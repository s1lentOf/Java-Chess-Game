package Constants;

public enum Endpoint {
    STARTGAME ("/posts", "/start"),
    STOPGAME ("/posts","/stop"),
    REQUESTMOVE ( "/posts","/request");

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