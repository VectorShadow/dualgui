package util;

import java.awt.*;

public class DynamicSize {

    private static final int FULL_SCREEN_TILE_SIZE = 32;
    private static final int WINDOWED_TILE_SIZE = FULL_SCREEN_TILE_SIZE / 2;
    private static final int FULL_SCREEN_TEXT_WIDTH = 18;//todo - find this value!
    private static final int WINDOWED_TEXT_WIDTH = FULL_SCREEN_TEXT_WIDTH / 2;

    private static DisplayMode getDisplayMode() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    }
    public static int getPixelHeight() {
        return getDisplayMode().getHeight();
    }
    public static int getPixelWidth() {
        return getDisplayMode().getWidth();
    }
    public static int countTileRows() {
        return getPixelHeight() / FULL_SCREEN_TILE_SIZE;
    }
    public static int countTileColumns() {
        return getPixelWidth() / FULL_SCREEN_TILE_SIZE;
    }
}
