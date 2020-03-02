package resources;

import resources.render.OutputMode;
import resources.render.RenderContext;
import resources.render.Renderer;

import java.awt.*;

public class DualityContext extends RenderContext {

    public static final int BASE_TILE_SIZE = 32;
    public static final int BASE_TEXT_WIDTH = 18;

    public static final RenderContext TILE_FULLSCREEN = new DualityContext(0);
    public static final RenderContext TILE_WINDOWED = new DualityContext(1);
    public static final RenderContext TEXT_FULLSCREEN = new DualityContext(2);
    public static final RenderContext TEXT_WINDOWED = new DualityContext(3);

    private DualityContext(int id) {
        super(id);
    }

    @Override
    public Dimension imageSize() {
        switch (ID){
            case 0:
                return new Dimension(BASE_TILE_SIZE, BASE_TILE_SIZE);
            case 1:
                return new Dimension(BASE_TILE_SIZE / 2, BASE_TILE_SIZE / 2);
            case 2:
                return new Dimension(BASE_TEXT_WIDTH, BASE_TILE_SIZE);
            case 3:
                return new Dimension(BASE_TEXT_WIDTH / 2, BASE_TILE_SIZE / 2);
            default: throw new IllegalStateException("Unhandled DualityContext ID: " + ID);
        }
    }

    @Override
    public Font imageFont() {
        switch (ID) {
            case 0:
                return new Font(Font.DIALOG, Font.PLAIN, (int)((double)Renderer.countUnits(outputMode()).height * 0.75));
            case 1:
                return new Font(Font.DIALOG, Font.PLAIN, (int)((double)Renderer.countUnits(outputMode()).height * 0.375));
            case 2:
                return new Font(Font.MONOSPACED, Font.PLAIN, (int)((double)Renderer.countUnits(outputMode()).height * 0.75));
            case 3:
                return new Font(Font.MONOSPACED, Font.PLAIN, (int)((double)Renderer.countUnits(outputMode()).height * 0.375));
            default: throw new IllegalStateException("Unhandled DualityContext ID: " + ID);
        }
    }

    @Override
    public OutputMode outputMode() {
        switch (ID) {
            case 0: case 1:
                return DualityMode.TILE;
            case 2: case 3:
                return DualityMode.SHORT_TEXT;
            default: throw new IllegalStateException("Unhandled DualityContext ID: " + ID);
        }
    }

    @Override
    public boolean isFullScreen() {
        switch (ID) {
            case 0: case 2: return true;
            case 1: case 3: return false;
            default: throw new IllegalStateException("Unhandled DualityContext ID: " + ID);
        }
    }
}
