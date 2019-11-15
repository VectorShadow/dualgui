package resources;

import resources.glyph.GlyphStringProtocol;
import resources.render.OutputMode;
import resources.render.RenderContext;

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

    @Override
    public GlyphStringProtocol getGlyphStringProtocol() {
        switch (this) {
            case TEXT:
                return new GlyphStringProtocol(1, 127, 2, 4);
            case TILE:
                return new GlyphStringProtocol(0, 1, 1, -1);
            default: throw new IllegalStateException("Unhandled DualityMode: " + this);
        }
    }
}
