package core;

import contract.Gui;
import resources.Glyph;
import resources.OutputMode;
import resources.Zone;

import java.util.ArrayList;

public class DualityGUI implements Gui {

    private ArrayList<Zone> zones;
    private ArrayList<Zone> visibleZones;

    private DFrame dFrame;

    public DualityGUI() {
        zones = new ArrayList<>();
        dFrame = new DFrame();
    }

    @Override
    public void close() {
        //todo - other cleanup here
        dFrame.dispose();
    }

    @Override
    public void setFullScreen(boolean fullScreen) {
        dFrame.setFullScreen(fullScreen);
    }

    @Override
    public void addZone(int rowOrigin, int colOrigin, int numRows, int numCols, OutputMode om) {
        Zone z = new Zone(rowOrigin, colOrigin, numRows, numCols, om);
        zones.add(z);
        visibleZones.add(z);
    }

    @Override
    public void hideZone(int zoneID) {
        visibleZones.remove(zones.get(zoneID));
    }

    @Override
    public void removeZone(int zoneID) {
        zones.remove(zoneID);
    }

    @Override
    public void showZone(int zoneID) {
        visibleZones.add(zones.get(zoneID));
    }

    @Override
    public void clear() {
        //todo - stub
    }

    @Override
    public void clear(int zone) {
        //todo - stub
    }

    @Override
    public void print(int row, int col, Glyph g) {
        //todo: treat this as the whole screen, and orient it to the center - other zones fill in around
    }

    @Override
    public void print(int row, int col, ArrayList<Glyph> g) {
        //do nothing here?
    }

    @Override
    public void print(int zone, int row, int col, Glyph g) {
        //todo - stub
    }

    @Override
    public void print(int zone, int row, int col, ArrayList<Glyph> g) {
        //todo - stub
    }

    @Override
    public void redraw() {
        //todo: draw background as tiles
    }
}
