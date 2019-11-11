package core;

import resources.DualityContext;
import resources.Glyph;
import resources.Renderer;

import javax.swing.*;

public class DFrame extends JFrame {

    private boolean fullScreen;
    private Glyph[][] glyphMap;
    private ImagePane imagePane;

    public DFrame() {
        setFullScreen(false);
        glyphMap = new Glyph[
                        Renderer.countUnits(DualityContext.TILE_FULLSCREEN).height
                ][
                        Renderer.countUnits(DualityContext.TILE_FULLSCREEN).width
                ];
        imagePane = new ImagePane();
        //todo - lots here
    }

    public void setFullScreen(boolean fs) {
        fullScreen = fs;
        //todo - resize the display
    }
    public boolean isFullScreen() {
        return fullScreen;
    }
}
