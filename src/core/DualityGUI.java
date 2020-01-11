package core;

import contract.Channel;
import contract.Gui;
import contract.Zone;
import contract.input.InputDialog;
import contract.menu.Menu;
import contract.menu.MenuOption;
import resources.*;
import resources.chroma.Chroma;
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

    private ArrayList<Channel> channels;
    private int currentChannelIndex;

    private JFrame frame;
    private ImagePane imagePane;
    private BufferedImage bufferedImage;

    private String iconImagePath;
    private String frameTitle;

    private KeyListener keyListener;

    public DualityGUI() {
        fullScreen = true;
        channels = new ArrayList<>();
        addChannel(DualityMode.TILE);
        currentChannelIndex = 0;
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
    public void setBackground(int channelID, int zoneID, Glyph g) {
        channels.get(channelID).setBackground(zoneID, g);
    }

    @Override
    public void setBorder(int channelID, int zoneID, Glyph g) {
        channels.get(channelID).setBorder(zoneID, g);
    }

    @Override
    public void addKeyListener(KeyListener kl) {
        keyListener = kl;
        frame.addKeyListener(keyListener);
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
    public void print(int row, int col, GlyphString gs) {
        if (channels.get(currentChannelIndex).mainOutputMode() != DualityMode.TEXT)
            throw new UnsupportedOperationException();
        channels.get(currentChannelIndex).print(row, col, gs);
    }

    @Override
    public void print(int zone, int row, int col, Glyph g) {
        channels.get(currentChannelIndex).print(zone, row, col, g);
    }

    @Override
    public void print(int zone, int row, int col, GlyphString gs) {
        channels.get(currentChannelIndex).print(zone, row, col, gs);
    }

    @Override
    public void printCentered(int row, GlyphString gs) {
        if (channels.get(currentChannelIndex).mainOutputMode() != DualityMode.TEXT)
            throw new UnsupportedOperationException();
        channels.get(currentChannelIndex).printCentered(row, gs);
    }

    @Override
    public void printCentered(int zone, int row, GlyphString gs) {
        channels.get(currentChannelIndex).print(zone, row, gs);
    }

    @Override
    public void redraw() {
        channels.get(currentChannelIndex).draw(fullScreen, bufferedImage);
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
    public void printMenu(int row, Menu menu, Color background, Color foreground) {
        if (channels.get(currentChannelIndex).mainOutputMode() != DualityMode.TEXT)
            throw new UnsupportedOperationException();
        clear();
        int r = row;
        GlyphString title = new GlyphString(menu.getTitle(), background, foreground);
        GlyphString optionName;
        boolean selected;
        printCentered( r, title);
        r += 2;
        for (MenuOption menuOption : menu) {
            selected = r - (row + 2) == menu.getSelectedOptionIndex();
            optionName = new GlyphString(
                    menuOption.getName(),
                    selected ? foreground : background,
                    selected ? background : menuOption.isEnabled() ? foreground : Chroma.dark(foreground)
            );
            printCentered(r++, optionName);
        }
    }

    @Override
    public void printMenu(int zone, int row, Menu menu, Color background, Color foreground) {
        clear(zone);
        int r = row;
        GlyphString title = new GlyphString(menu.getTitle(), background, foreground);
        GlyphString optionName;
        boolean selected;
        printCentered(zone, r, title);
        r += 2;
        for (MenuOption menuOption : menu) {
            selected = r - (row + 2) == menu.getSelectedOptionIndex();
            optionName = new GlyphString(
                    menuOption.getName(),
                    selected ? foreground : background,
                    selected ? background : menuOption.isEnabled() ? foreground : Chroma.dark(foreground)
            );
            printCentered(zone, r++, optionName);
        }
    }

    @Override
    public void printDialog(InputDialog dialog) {
        if (channels.get(currentChannelIndex).mainOutputMode() != DualityMode.TEXT)
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

    private void initializeBufferedImage(){
        bufferedImage = new BufferedImage(imagePane.getWidth(), imagePane.getHeight(), BufferedImage.TYPE_INT_ARGB);
    }
}
