package core;

import contract.Gui;
import resources.*;
import resources.Renderer;

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
                0,
                0,
                Renderer.countPixels().height,
                Renderer.countPixels().width,
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
        bufferedImage = mainZone.draw(fullScreen);
        for (Zone z : zones) {
            //todo - draw all other zones on top of this image
        }
        imagePane.setImage(bufferedImage);
        imagePane.repaint();
        initializeBufferedImage();
    }
    private void initializeBufferedImage(){
        bufferedImage = new BufferedImage(imagePane.getWidth(), imagePane.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
}
