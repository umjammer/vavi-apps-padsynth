package vavi.apps.padsynth;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.sun.media.sound.ModelAbstractOscillator;
import vavix.rococoa.moultitouch.MTTouch;
import vavix.rococoa.moultitouch.MultitouchManager;


/**
 * Main.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-08-29 nsano initial version <br>
 */
public class Main {

    /**
     * @param args none
     */
    public static void main(String[] args) throws Exception {

        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.loadAllInstruments(new MyOscillator());
        Sequencer sequencer = MidiSystem.getSequencer(false);
        sequencer.open();
        sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        sequencer.start();



        Main app = new Main();
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

        System.out.println();
System.out.println("Is active, press enter to stop");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();
System.out.println("Stop...");

        multitouchManager.stop();



        sequencer.stop();
        sequencer.close();
        synthesizer.close();
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

        JPanel tool = new JPanel();
        tool.setLayout(new GridLayout(3, 2));

        JToolBar bar = new JToolBar();


        frame.setContentPane(panel);
        frame.setTitle("PadSynth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    static class MyOscillator extends ModelAbstractOscillator {
        double ix = 0;
        double last_ix_step = -1;

        public int read(float[][] buffers, int offset, int len) throws IOException {

            // Grab channel 0 buffer from buffers
            float[] buffer = buffers[0];

            // Calculate ix step so sin oscillirator is tuned so 6900 cents is 440 hz
            double target_ix_step =
                    Math.exp((getPitch() - 6900) * (Math.log(2.0) / 1200.0))
                            * (440 / getSampleRate()) * (Math.PI * 2);
            double ix_step = last_ix_step;
            if (ix_step == -1) ix_step = target_ix_step;
            double ix_step_step = (target_ix_step - ix_step) / len;

            // Simple FM synthesizer implementation
            int endoffset = offset + len;
            for (int i = offset; i < endoffset; i++) {
                buffer[i] = (float) Math.sin(ix + Math.sin(ix * 3));
                ix += ix_step;
                // ix_step_step is used for
                // smooth pitch changes
                ix_step += ix_step_step;
            }

            last_ix_step = target_ix_step;

            return len;
        }
    }
}