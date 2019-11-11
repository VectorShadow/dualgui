package core;

import resources.DynamicSize;
import resources.Glyph;
import resources.Renderer;

import javax.swing.*;

public class DFrame extends JFrame {

    private boolean fullScreen;
    private Glyph[][] glyphMap;
    private ImagePane imagePane;

    public DFrame() {
        setFullScreen(false);
        glyphMap = new Glyph[DynamicSize.countTileRows()][DynamicSize.countTileColumns()];
        imagePane = new ImagePane();
        //todo - lots here
    }

    public void setFullScreen(boolean fs) {
        fullScreen = fs;
        Renderer.initialize(fullScreen);
        //todo - resize the display
    }

}
