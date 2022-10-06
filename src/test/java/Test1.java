import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import vavix.rococoa.moultitouch.MTTouch;
import vavix.rococoa.moultitouch.MultitouchManager;


/**
 * Test1.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-29 nsano initial version <br>
 */
public class Test1 {

    /**
     * @param args none
     */
    public static void main(String[] args) throws Exception {

        Test1 app = new Test1();
        app.gui();
        MultitouchManager multitouchManager = MultitouchManager.getInstance();
        multitouchManager.addListener(e -> {
            synchronized (app.touches) {
//System.out.printf("%d%n", e.getTouches().length);
                app.touches.clear();
                app.touches.addAll(Arrays.asList(e.getTouches()));
            }
            app.panel.repaint();
        });

//        multitouchManager.stop();
    }

    JPanel panel;
    final List<MTTouch> touches = new ArrayList<>();

    void gui() {
        JFrame frame = new JFrame();

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                synchronized (touches) {
                    int w = getWidth();
                    int h = getHeight();
                    g.setColor(Color.white);
                    g.drawRect(0, 0, w, h);
                    for (MTTouch touch : touches) {
                        int x = (int) (w * touch.normalizedPosition.position.x);
                        int y = (int) (h * (1 - touch.normalizedPosition.position.y));
                        int r = 2 + (int) Math.pow(touch.density, 5);
                        g.setColor(touch.handId == 1 ? Color.red : Color.blue);
                        g.fillArc(x - r , y - r, r * 2, r * 2, 0, 360);
                        g.setColor(Color.black);
                        g.drawString(String.valueOf(touch.fingerId), x + 2 * r , y + 2 * r );
//System.out.printf("%d, %d, %d\t%s%n", touch.identifier, x, y, touch);
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(1600, 1200));
        panel.setLayout(new BorderLayout());

        frame.setContentPane(panel);
        frame.setTitle("MagicTouch");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}