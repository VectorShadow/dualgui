package core;

import contract.Channel;
import contract.Gui;
import contract.Zone;
import contract.input.InputDialog;
import contract.menu.Menu;
import contract.menu.MenuOption;
import resources.*;
import resources.chroma.ChromaSet;
import resources.glyph.Glyph;
import resources.glyph.GlyphString;
import resources.render.OutputMode;
import resources.render.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DualityGUI implements Gui {

    //Remember the size of the frame and pane. On startup, default to half the size of the monitor.
    private static Dimension lastWindowSize = getDefaultWindowSize();

    private boolean fullScreenMode;

    private ArrayList<Channel> channels;
    private int currentChannelIndex;

    private Canvas canvas;
    private OutputWindow outputWindow;

    private boolean isRedrawing = false;

    public DualityGUI() {
        fullScreenMode = true;
        channels = new ArrayList<>();
        addChannel(DualityMode.TILE);
        currentChannelIndex = 0;
        Dimension d = Renderer.countPixels();
        canvas = new Canvas(d.height, d.width, 0);
        generateWindow();
    }

    public static Dimension getMonitorDimension() {
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        return new Dimension(dm.getWidth(), dm.getHeight());
    }

    public static Dimension getLastWindowSize() {
        return lastWindowSize;
    }

    private static Dimension getDefaultWindowSize() {
        return new Dimension(
                (int)(getMonitorDimension().width / Math.sqrt(2.0)),
                (int)(getMonitorDimension().height / Math.sqrt(2.0))
        );
    }

    private void requireCanvas() {
        if (canvas == null)
            throw new IllegalStateException("Size undefined - use .setSize() first");
    }

    private void requireWindow() {
        if (outputWindow == null)
            throw new IllegalStateException("OutputWindow not found - use .generateWindow() first");
    }

    /**
     * Clean up the old output window if extant, then generate a new one based on the current fullscreen mode.
     */
    void generateWindow() {
        if (outputWindow != null) {
            if (fullScreenMode)
                lastWindowSize = outputWindow.getSize(); //remember the current windowed mode size
            outputWindow.dispose();
        }
        outputWindow = new OutputWindow(fullScreenMode);
    }

    @Override
    public void close() {
        //todo - other cleanup here?
        outputWindow.dispose();
    }

    @Override
    public void setFullScreenMode(boolean fs) {
        if (fullScreenMode == fs) return;
        fullScreenMode = fs;
        generateWindow();
        redraw();
    }

    @Override
    public void toggleFullScreen() {
        setFullScreenMode(!fullScreenMode);
    }

    @Override
    public int addChannel(OutputMode om) {
        channels.add(new Channel(om));
        return channels.size() - 1;
    }

    @Override
    public int addZone(
            int channelID,
            double verticalOriginPct,
            double verticalSizePct,
            double horizontalOriginPct,
            double horizontalSizePct,
            OutputMode om) {
        Zone z = new Zone(verticalOriginPct, verticalSizePct, horizontalOriginPct, horizontalSizePct, om);
        return channels.get(channelID).addZone(z);
    }

    @Override
    public void removeZone(int channelID, int zoneID) {
        channels.get(channelID).removeZone(zoneID);
    }

    @Override
    public void setBackground(int channelID, Glyph g) {
        channels.get(channelID).setBackground(g);
    }

    @Override
    public void setBackground(int channelID, int zoneID, Glyph g) {
        channels.get(channelID).setBackground(zoneID, g);
    }

    @Override
    public void setBorder(int channelID, Glyph g) {
        channels.get(channelID).setBorder(g);
    }

    @Override
    public void setBorder(int channelID, int zoneID, Glyph g) {
        channels.get(channelID).setBorder(zoneID, g);
    }

    @Override
    public void addKeyListener(KeyListener kl) {
        requireWindow();
        outputWindow.addKeyListener(kl);
    }

    @Override
    public void addWindowListener(WindowListener wl) {
        requireWindow();
        outputWindow.addWindowListener(wl);
    }

    @Override
    public int activeChannel() {
        return currentChannelIndex;
    }

    @Override
    public void changeChannel(int newChannelID) {
        currentChannelIndex = newChannelID;
    }

    @Override
    public void clear() {
        channels.get(currentChannelIndex).clear();
    }

    @Override
    public void clear(int zone) {
        channels.get(currentChannelIndex).clear(zone);
    }

    @Override
    public void print(int row, int col, Glyph g) {
        channels.get(currentChannelIndex).print(row, col, g);
    }

    @Override
    public Point print(int row, int col, GlyphString gs) {
        if (channels.get(currentChannelIndex).mainOutputMode() == DualityMode.TILE)
            throw new UnsupportedOperationException();
        return channels.get(currentChannelIndex).print(row, col, gs);
    }

    @Override
    public void print(int zone, int row, int col, Glyph g) {
        channels.get(currentChannelIndex).print(zone, row, col, g);
    }

    @Override
    public Point print(int zone, int row, int col, GlyphString gs) {
        return channels.get(currentChannelIndex).print(zone, row, col, gs);
    }

    @Override
    public Point printCentered(int row, GlyphString gs) {
        if (channels.get(currentChannelIndex).mainOutputMode() == DualityMode.TILE)
            throw new UnsupportedOperationException();
        return channels.get(currentChannelIndex).printCentered(row, gs);
    }

    @Override
    public Point printCentered(int zone, int row, GlyphString gs) {
        return channels.get(currentChannelIndex).print(zone, row, gs);
    }

    @Override
    public void redraw() {
        if (isRedrawing) return; //semaphore
        isRedrawing = true;
        requireCanvas();
        requireWindow();
        canvas.clear();
        channels.get(currentChannelIndex).draw(true, canvas.getImage());
        outputWindow.refresh(canvas.getImage());
        isRedrawing = false;
    }

    @Override
    public void setIcon(String pathToIconImage) {
        outputWindow.setIconImage(new ImageIcon(pathToIconImage).getImage());
    }

    @Override
    public void setTitle(String title) {
        outputWindow.setTitle(title);
    }

    public static String printMenuHotkey(int optionIndex) {
        String optionPreface = "(";
        char hotkeySymbol;
        switch (optionIndex) {
            case 0: hotkeySymbol = '1'; break;
            case 1: hotkeySymbol = '2'; break;
            case 2: hotkeySymbol = '3'; break;
            case 3: hotkeySymbol = '4'; break;
            case 4: hotkeySymbol = '5'; break;
            case 5: hotkeySymbol = '6'; break;
            case 6: hotkeySymbol = '7'; break;
            case 7: hotkeySymbol = '8'; break;
            case 8: hotkeySymbol = '9'; break;
            case 9: hotkeySymbol = 'a'; break;
            case 10: hotkeySymbol = 'b'; break;
            case 11: hotkeySymbol = 'c'; break;
            case 12: hotkeySymbol = 'd'; break;
            case 13: hotkeySymbol = 'e'; break;
            case 14: hotkeySymbol = 'f'; break;
            default: hotkeySymbol = ' ';
        }
        return optionPreface + hotkeySymbol + ")";
    }

    @Override
    public void printMenu(int row, Menu menu, ChromaSet cs) {
        if (channels.get(currentChannelIndex).mainOutputMode() == DualityMode.TILE)
            throw new UnsupportedOperationException();
        clear();
        int r = row;
        GlyphString title = new GlyphString(menu.getTitle(), cs.getBackground(), cs.getForeground());
        GlyphString optionName;
        boolean selected;
        printCentered( r, title);
        r += 2;
        int index;
        for (MenuOption menuOption : menu) {
            index = r - (row + 2);
            selected = index == menu.getSelectedOptionIndex();
            optionName = new GlyphString(
                    printMenuHotkey(index) + menuOption.getName(),
                    cs.getBackground(),
                    selected ? cs.getHighlight() : menuOption.isEnabled() ? cs.getForeground() : cs.subdueForeground()
            );
            printCentered(r++, optionName);
        }
    }

    @Override
    public void printMenu(int zone, int row, Menu menu, ChromaSet cs) {
        clear(zone);
        int r = row;
        GlyphString title = new GlyphString(menu.getTitle(), cs.getBackground(), cs.getForeground());
        GlyphString optionName;
        boolean selected;
        printCentered(zone, r, title);
        r += 2;
        int index;
        for (MenuOption menuOption : menu) {
            index = r - (row + 2);
            selected = index == menu.getSelectedOptionIndex();
            optionName = new GlyphString(
                    printMenuHotkey(index) + menuOption.getName(),
                    selected ? cs.getHighlight() : cs.getForeground(),
                    selected ? cs.getBackground() : menuOption.isEnabled() ? cs.getForeground() : cs.subdueForeground()
            );
            printCentered(zone, r++, optionName);
        }
    }

    @Override
    public void printDialog(InputDialog dialog) {
        if (channels.get(currentChannelIndex).mainOutputMode() != DualityMode.SHORT_TEXT)
            throw new UnsupportedOperationException();
        clear();
        int row = rowAtPercent(dialog.getRowStartPercent());
        printCentered(row, dialog.glyphStringTitle());
        row += 2;
        for (GlyphString field : dialog) printCentered(row++, field);
    }

    @Override
    public void printDialog(int zone, InputDialog dialog) {
        clear(zone);
        int row = rowAtPercent(zone, dialog.getRowStartPercent());
        printCentered(zone, row, dialog.glyphStringTitle());
        row += 2;
        for (GlyphString field : dialog) printCentered(zone, row++, field);
    }

    @Override
    public int countRows() {
        return channels.get(currentChannelIndex).countRows();
    }

    @Override
    public int countColumns() {
        return channels.get(currentChannelIndex).countColumns();
    }

    @Override
    public int countRows(int zone) {
        return channels.get(currentChannelIndex).countRows(zone);
    }

    @Override
    public int countColumns(int zone) {
        return channels.get(currentChannelIndex).countColumns(zone);
    }

    @Override
    public int rowAtPercent(double percent) {
        return channels.get(currentChannelIndex).rowAtPercent(percent);
    }
    @Override
    public int colAtPercent(double percent) {
        return channels.get(currentChannelIndex).colAtPercent(percent);
    }

    @Override
    public int rowAtPercent(int zone, double percent) {
        return channels.get(currentChannelIndex).rowAtPercent(zone, percent);
    }

    @Override
    public int colAtPercent(int zone, double percent) {
        return channels.get(currentChannelIndex).colAtPercent(zone, percent);
    }

    @Override
    public int countChannels() {
        return channels.size();
    }

    @Override
    public int countZones(int channelId) {
        return channels.get(channelId).countZones();
    }

    @Override
    public int maxCol() {
        return channels.get(currentChannelIndex).maxCol();
    }

    @Override
    public int minCol() {
        return channels.get(currentChannelIndex).minCol();
    }

    @Override
    public int maxRow() {
        return channels.get(currentChannelIndex).maxRow();
    }

    @Override
    public int minRow() {
        return channels.get(currentChannelIndex).minRow();
    }
    @Override
    public int maxCol(int zoneID) {
        return channels.get(currentChannelIndex).maxCol(zoneID);
    }

    @Override
    public int minCol(int zoneID) {
        return channels.get(currentChannelIndex).minCol(zoneID);
    }

    @Override
    public int maxRow(int zoneID) {
        return channels.get(currentChannelIndex).maxRow(zoneID);
    }

    @Override
    public int minRow(int zoneID) {
        return channels.get(currentChannelIndex).minRow(zoneID);
    }

    @Override
    public int from(int channelID, int zoneID, boolean row, boolean before) {
        return channels.get(channelID).from(zoneID, row, before);
    }
}
