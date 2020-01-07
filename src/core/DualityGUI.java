package core;

import contract.Gui;
import contract.Zone;
import resources.*;
import resources.glyph.Glyph;
import resources.glyph.image.GlyphString;
import resources.render.OutputMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
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

    private String iconImagePath;
    private String frameTitle;

    private KeyListener keyListener;

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
        if (keyListener != null) frame.addKeyListener(keyListener);
        if (fullScreen) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(Frame.NORMAL);
            frame.setLocation(centeredOrigin());
            frame.setIconImage(new ImageIcon(iconImagePath).getImage());
            frame.setTitle(frameTitle);
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
        close();
        deriveSizes();
    }

    @Override
    public void toggleFullScreen() {
        setFullScreen(!fullScreen);
    }

    @Override
    public int addZone(
            double verticalOriginPct,
            double verticalSizePct,
            double horizontalOriginPct,
            double horizontalSizePct,
            OutputMode om) {
        Zone z = new Zone(verticalOriginPct, verticalSizePct, horizontalOriginPct, horizontalSizePct, om);
        zones.add(z);
        visibleZones.add(z);
        return zones.size();
    }

    @Override
    public void hideZone(int zoneID) {
        visibleZones.remove(zones.get(zoneID));
    }

    @Override
    public void removeZone(int zoneID) {
        hideZone(zoneID); //ensure we remove from visibleZones as well!
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
    public void addKeyListener(KeyListener kl) {
        keyListener = kl;
        frame.addKeyListener(keyListener);
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
    public void print(int row, int col, GlyphString gs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(int zone, int row, int col, Glyph g) {
        zones.get(zone).print(row, col, g);
    }

    @Override
    public void print(int zone, int row, int col, GlyphString gs) {
        zones.get(zone).print(row, col, gs);
    }

    @Override
    public void printCentered(int row, ArrayList<Glyph> g) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printCentered(int zone, int row, GlyphString gs) {
        Zone z = zones.get(zone);
        int c = z.zoneCols();
        int l = gs.size();
        //don't print more than 1 line - cut the input short instead
        if (l >= c - 2) print(zone, row, 1, gs.subGlyphString(0, c - 2));
        else print(zone, row, c / 2 - l / 2, gs);
    }

    @Override
    public void redraw() {
        //todo: draw background as tiles
        mainZone.draw(fullScreen, true, bufferedImage);
        for (Zone z : visibleZones) {
            z.draw(fullScreen, false, bufferedImage);
        }
        imagePane.setImage(bufferedImage);
        imagePane.repaint();
        initializeBufferedImage();
    }

    @Override
    public void setIcon(String pathToIconImage) {
        iconImagePath = pathToIconImage;
        frame.setIconImage(new ImageIcon(iconImagePath).getImage());
    }

    @Override
    public void setTitle(String title) {
        frameTitle = title;
        frame.setTitle(frameTitle);
    }

    @Override
    public int countRows() {
        return mainZone.zoneRows();
    }

    @Override
    public int countColumns() {
        return mainZone.zoneCols();
    }

    @Override
    public int countRows(int zone) {
        return zones.get(zone).zoneRows();
    }

    @Override
    public int countColumns(int zone) {
        return zones.get(zone).zoneCols();
    }

    private void checkPercent(double percent) {
        if (percent < 0.0 || percent > 1.0) throw new IllegalArgumentException("Percent " + percent + " out of bounds.");
    }
    @Override
    public int rowAtPercent(double percent) {
        checkPercent(percent);
        return (int)(percent * countRows());
    }
    @Override
    public int colAtPercent(double percent) {
        checkPercent(percent);
        return (int)(percent * countColumns());
    }

    @Override
    public int rowAtPercent(int zone, double percent) {
        return (int)(percent * countRows(zone));
    }

    @Override
    public int colAtPercent(int zone, double percent) {
        checkPercent(percent);
        return (int)(percent * countColumns(zone));
    }

    private void initializeBufferedImage(){
        bufferedImage = new BufferedImage(imagePane.getWidth(), imagePane.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
}
