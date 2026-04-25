package Constants;

public enum Sound {
    MOVE("/sounds/move.wav"),
    CAPTURE("/sounds/capture.wav"),
    CHECK("/sounds/check.wav");

    private final String resourcePath;

    Sound(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }
}