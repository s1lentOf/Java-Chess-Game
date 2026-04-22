package Constants;

public enum Environment {
    TEST("https://jsonplaceholder.typicode.com"), // Test URL.
    PROD("https://test-api.com"); // Our real base URL for production.

    private final String baseURL;

    Environment(String baseURL) {
        this.baseURL = baseURL;
    }

    public String getBaseURL() {
        return baseURL;
    }
}