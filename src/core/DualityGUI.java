package core;

import contract.Gui;
import resources.Glyph;

import java.util.ArrayList;

public class DualityGUI implements Gui {

    private DFrame dFrame;

    public DualityGUI() {
        dFrame = new DFrame();
    }

    @Override
    public void close() {
        //todo - other cleanup here
        dFrame.dispose();
    }

    @Override
    public void setFullScreen(boolean fullScreen) {

    }

    @Override
    public void addZone(int rowOrigin, int colOrigin, int numRows, int numCols, OutputMode mode) {

    }

    @Override
    public void hideZone(int zoneID) {

    }

    @Override
    public void removeZone(int zoneID) {

    }

    @Override
    public void showZone(int zoneID) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void clear(int zone) {

    }

    @Override
    public void print(int row, int col, Glyph g) {

    }

    @Override
    public void print(int row, int col, ArrayList<Glyph> g) {

    }

    @Override
    public void print(int zone, int row, int col, Glyph g) {

    }

    @Override
    public void print(int zone, int row, int col, ArrayList<Glyph> g) {

    }

    @Override
    public void redraw() {

    }

    @Override
    public void redraw(int zone) {

    }

    @Override
    public void redraw(int zone, int row, int col) {

    }
}
