package resources;

import resources.glyph.GlyphStringProtocol;
import resources.render.OutputMode;
import resources.render.RenderContext;

public enum DualityMode implements OutputMode {
    LONG_TEXT,
    MESSAGES,
    SHORT_TEXT,
    TILE;

    @Override
    public RenderContext generateContext(boolean fullScreen) {
        switch (this) {
            case LONG_TEXT: case MESSAGES: case SHORT_TEXT:
                return fullScreen ? DualityContext.TEXT_FULLSCREEN : DualityContext.TEXT_WINDOWED;
            case TILE:
                return fullScreen ? DualityContext.TILE_FULLSCREEN : DualityContext.TILE_WINDOWED;
            default: throw new IllegalStateException("Unhandled DualityMode: " + this);
        }
    }

    @Override
    public GlyphStringProtocol getGlyphStringProtocol() {
        switch (this) {
            case LONG_TEXT:
                return new GlyphStringProtocol(4, 4_096, 64, 4);
            case MESSAGES:
                return new GlyphStringProtocol(1, 1_024, 16, 4);
            case SHORT_TEXT:
                return new GlyphStringProtocol(1, 127, 2, 4);
            case TILE:
                return new GlyphStringProtocol(0, 1, 1, -1);
            default: throw new IllegalStateException("Unhandled DualityMode: " + this);
        }
    }
}
