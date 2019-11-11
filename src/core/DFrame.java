//package core;
//
//import resources.DualityContext;
//import resources.Glyph;
//import resources.Renderer;
//
//import javax.swing.*;
//
//public class DFrame {
//
//    private boolean fullScreen;
//    private Glyph[][] glyphMap;
//    private ImagePane imagePane;
//    private JFrame frame;
//
//    public DFrame() {
//        glyphMap = new Glyph[
//                        Renderer.countUnits(DualityContext.TILE_FULLSCREEN).height
//                ][
//                        Renderer.countUnits(DualityContext.TILE_FULLSCREEN).width
//                ];
//    }
//
//    public void setFullScreen(boolean fs) {
//        fullScreen = fs;
//        imagePane = new ImagePane(fullScreen);
//        //todo - resize the display
//    }
//    public boolean isFullScreen() {
//        return fullScreen;
//    }
//    def center: (Int, Int) = {
//        val monitor: Dimension = Toolkit.getDefaultToolkit.getScreenSize
//                ((monitor.getWidth/2 - scr.getWidth/2).toInt, (monitor.getHeight/2 - scr.getHeight/2).toInt)
//    }
//    public void deriveSizes() {
//        frame = new JFrame();
//        if (res.equals(Resolution.FULL_SCREEN)){
//            scr.setExtendedState(Frame.MAXIMIZED_BOTH)
//            scr.setUndecorated(true)
//        }
//        else{
//            scr.setExtendedState(Frame.NORMAL)
//            scr.setUndecorated(false)
//        }
//        pane = new RenderPanel(res)
//        pane.bI = new BufferedImage(pane.getWidth, pane.getHeight, BufferedImage.TYPE_INT_ARGB)
//        scr.setContentPane(pane)
//        scr.setVisible(true)
//        scr.pack()
//        val loc: (Int, Int) = center
//        scr.setLocation(loc._1, loc._2)
//    }
//}
