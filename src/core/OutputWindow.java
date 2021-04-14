package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * The primary display window for the GUI.
 */
public class OutputWindow extends JFrame {

    private OutputPanel outputPanel = null;


    //todo - methods for setting all static fields:
    private static String iconImagePath = null;
    private static String frameTitle = "";

    private static KeyListener keyListener = null;
    private static MouseListener mouseListener = null;
    private static WindowListener windowListener = null;

    /**
     * Generate a new OutputWindow based on the provided fullScreenMode.
     */
    public OutputWindow(boolean fullScreenMode) {
        addComponentListener(
                new ComponentAdapter() {
                    public void componentResized(ComponentEvent e) {
                        /**
                         * When we resize this Window, we want to update the displayed image.
                         * In particular, we need to know the new contentPane size and resize the outputPanel to match.
                         * This will invoke the outputPanel's overridden setSize method which rescales the image
                         * and repaints the component.
                         */
                        Dimension target = getContentPane().getSize();
                        outputPanel.setPreferredSize(target);
                        outputPanel.setSize(target);
                        setContentPane(outputPanel);
                    }
                }
                );
        outputPanel = new OutputPanel(fullScreenMode);
        setContentPane(outputPanel);
        setUndecorated(fullScreenMode);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        if (keyListener != null) addKeyListener(keyListener);
        if (mouseListener != null) addMouseListener(mouseListener);
        if (windowListener != null) addWindowListener(windowListener);
        if (fullScreenMode) {
            setExtendedState(Frame.MAXIMIZED_BOTH);
        } else {
            setExtendedState(Frame.NORMAL);
            setLocation(centeredOrigin());
            setTitle(frameTitle);
        }
    }

    /**
     * Save added Listeners as static fields so they can be automatically reapplied to new windows during
     * fullscreen/windowed transitions.
     */
    @Override
    public synchronized void addKeyListener(KeyListener l) {
        super.addKeyListener(l);
        keyListener = l;
    }

    @Override
    public void addMouseListener(MouseListener l) {
        outputPanel.addMouseListener(l);
        mouseListener = l;
    }

    @Override
    public void addWindowListener(WindowListener l) {
        super.addWindowListener(l);
        windowListener = l;
    }

    public Dimension getPanelSize() {
        return outputPanel.getSize();
    }

    public void refresh(BufferedImage bufferedImage) {
        outputPanel.setImage(bufferedImage);
        outputPanel.repaint();
    }

    public void setFrameTitle(String title) {
        frameTitle = title;
    }

    private Point centeredOrigin() {
        Dimension monitorDimension = DualityGUI.getMonitorDimension();
        return new Point(
                ((int)(monitorDimension.getWidth() - getWidth())) / 2,
                ((int)(monitorDimension.getHeight() - getHeight())) / 2
        );
    }
}
