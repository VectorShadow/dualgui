package resources;

public enum DualityMode implements OutputMode {
    TEXT,
    TILE;

    @Override
    public RenderContext generateContext(boolean fullScreen) {
        switch (this) {
            case TEXT:
                return fullScreen ? DualityContext.TEXT_FULLSCREEN : DualityContext.TEXT_WINDOWED;
            case TILE:
                return fullScreen ? DualityContext.TILE_FULLSCREEN : DualityContext.TILE_WINDOWED;
            default: throw new IllegalStateException("Unhandled DualityMode: " + this);
        }
    }
}
