package core;

import contract.Gui;
import resources.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DualityGUI implements Gui {

    private boolean fullScreen;

    private Zone mainZone;

    private ArrayList<Zone> zones;
    private ArrayList<Zone> visibleZones;

    private JFrame frame;
    private ImagePane imagePane;
    private BufferedImage bufferedImage;

    public DualityGUI() {
        fullScreen = true;
        mainZone = new Zone(
                0.0,
                1.0,
                0.0,
                1.0,
                DualityMode.TILE
        );
        zones = new ArrayList<>();
        visibleZones = new ArrayList<>();
        deriveSizes();
    }

    public boolean isFullScreen() {
        return fullScreen;
    }
    private Point centeredOrigin() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Point(
                ((int)(screenSize.getWidth() - frame.getWidth())) / 2,
                ((int)(screenSize.getHeight() - frame.getHeight())) / 2
                );
    }
    public void deriveSizes() {
        frame = new JFrame();
        imagePane = new ImagePane(fullScreen);
        initializeBufferedImage();
        imagePane.setImage(bufferedImage);
        frame.setContentPane(imagePane);
        frame.setUndecorated(fullScreen);
        frame.setVisible(true);
        frame.pack();
        if (fullScreen) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(Frame.NORMAL);
            frame.setLocation(centeredOrigin());
        }
    }

    @Override
    public void close() {
        //todo - other cleanup here
        frame.dispose();
    }

    @Override
    public void setFullScreen(boolean fs) {
        if (fullScreen == fs) return;
        fullScreen = fs;
        deriveSizes();
    }

    @Override
    public void addZone(
            double verticalOriginPct,
            double verticalSizePct,
            double horizontalOriginPct,
            double horizontalSizePct,
            OutputMode om) {
        Zone z = new Zone(verticalOriginPct, verticalSizePct, horizontalOriginPct, horizontalSizePct, om);
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
    public void setBackground(int zoneID, Glyph g) {
        zones.get(zoneID).setBackground(g);
    }

    @Override
    public void setBorder(int zoneID, Glyph g) {
        zones.get(zoneID).setBorder(g);
    }

    @Override
    public void clear() {
        mainZone.clear();
        for (Zone z : zones) z.clear();
    }

    @Override
    public void clear(int zone) {
        zones.get(zone).clear();
    }

    @Override
    public void print(int row, int col, Glyph g) {
        mainZone.print(row, col, g);
    }

    @Override
    public void print(int row, int col, ArrayList<Glyph> g) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(int zone, int row, int col, Glyph g) {
        zones.get(zone).print(row, col, g);
    }

    @Override
    public void print(int zone, int row, int col, ArrayList<Glyph> g) {
        zones.get(zone).print(row, col, g);
    }

    @Override
    public void redraw() {
        //todo: draw background as tiles
        mainZone.draw(fullScreen, bufferedImage);
        for (Zone z : visibleZones) {
            z.draw(fullScreen, bufferedImage);
        }
        imagePane.setImage(bufferedImage);
        imagePane.repaint();
        initializeBufferedImage();
    }
    private void initializeBufferedImage(){
        bufferedImage = new BufferedImage(imagePane.getWidth(), imagePane.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
}
