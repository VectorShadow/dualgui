package core;

import resources.render.Renderer;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePane extends JPanel {
    private BufferedImage image;
    public ImagePane(boolean fullScreen){
        Dimension d = Renderer.countPixels();
        if (!fullScreen) d.setSize(d.width / 2, d.height / 2);
        setMinimumSize(d);
        setPreferredSize(d);
        setMaximumSize(d);
        setSize(d);
        setOpaque(true);
        setVisible(true);
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {//updates the displayed image.
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(image, 0, 0, null);
    }
    public void setImage(BufferedImage bi) {
        image = bi;
    }
    public BufferedImage getImage() {
        return image;
    }
}
