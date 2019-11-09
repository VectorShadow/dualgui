package core;

import resources.Glyph;
import util.DynamicSize;

import javax.swing.*;

public class DFrame extends JFrame {

    private boolean fullScreen = false;
    private Glyph[][] glyphMap;
    private ImagePane imagePane;

    public DFrame() {
        glyphMap = new Glyph[DynamicSize.countTileRows()][DynamicSize.countTileColumns()];
        imagePane = new ImagePane();
        //todo - lots here
    }

    public void setFullScreen(boolean fs) {
        fullScreen = fs;
        //todo - resize the display
    }

}
