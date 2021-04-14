package core;

import java.awt.image.BufferedImage;

/**
 * The base layer to which all graphics are drawn.
 */
public class Canvas {
    final int BASE_RGB_VALUE;
    final int HEIGHT;
    final int WIDTH;

    private final BufferedImage IMAGE;

    Canvas(int h, int w, int rgb) {
        BASE_RGB_VALUE = rgb;
        HEIGHT = h;
        WIDTH = w;
        IMAGE = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public int getBaseRGBValue() {
        return BASE_RGB_VALUE;
    }

    public void paint(int pixelRow, int pixelCol, int rgbValue) {
        //debug:
//        if (pixelRow < 0 || pixelRow >= HEIGHT)
//            throw new IllegalArgumentException("pixelRow (" + pixelRow + ") out of bounds: 0 <= [value] < " + HEIGHT);
//        if (pixelCol < 0 || pixelCol >= WIDTH)
//            throw new IllegalArgumentException("pixelCol (" + pixelCol + ") out of bounds: 0 <= [value] < " + WIDTH);
        IMAGE.setRGB(pixelCol, pixelRow, rgbValue);
    }
    void clear(){
        clear(BASE_RGB_VALUE);
    }
    void clear(int rgbValue) {
        for (int h = 0; h < HEIGHT; ++h) {
            for (int w = 0; w < WIDTH; ++w) {
                paint(h, w, rgbValue);
            }
        }
    }

    public BufferedImage getImage() {
        return IMAGE;
    }
}
