package Constants;

public enum Endpoint {
    STARTGAME ("/start"),
    STOPGAME ("/stop"),
    REQUESTMOSE ("/request");

    private final String url;

    Endpoint(String url) {
        this.url = url;
    }

    public String getEndpointURL() { return url; }
}
