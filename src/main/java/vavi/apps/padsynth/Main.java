package vavi.apps.padsynth;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.media.sound.ModelAbstractOscillator;
import vavi.sound.midi.MidiUtil;
import vavi.util.Debug;
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

        Main app = new Main();
        app.gui();

        Runtime.getRuntime().addShutdownHook(new Thread(app::close));
    }

    void close() {
        noteOff();

        multitouchManager.stop();

        synthesizer.close();
    }

    void noteOn() {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_ON, 0, 60, 127);
            receiver.send(message, -1);
Debug.println("note on");
            noteOn = true;
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    void noteOff() {
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(ShortMessage.NOTE_OFF, 0, 60, 0);
            receiver.send(message, -1);
Debug.println("note off");
            noteOn = false;
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    Main() throws Exception {
        MyOscillator.app = this; // hideous

        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.loadAllInstruments(new MyOscillator());

        receiver = synthesizer.getReceiver();
        MidiUtil.volume(receiver, 0.3f);

        multitouchManager = MultitouchManager.getInstance();
        multitouchManager.addListener(e -> {
            synchronized (touches) {
//System.out.printf("%d%n", e.getTouches().length);
                touches.clear();
                touches.addAll(Arrays.asList(e.getTouches()));
            }
            panel.repaint();
        });
    }

    MultitouchManager multitouchManager;

    boolean noteOn;
    int padX;
    int padY;
    Synthesizer synthesizer;
    Receiver receiver;

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
                        if (!noteOn) {
                            noteOn();
                        }
                        padX = x;
                        padY = y;
                        break;
                    }
                    if (touches.size() == 0) {
                        if (noteOn) {
                            noteOff();
                        }
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(1600, 1200));
        panel.setLayout(new BorderLayout());

        frame.setContentPane(panel);
        frame.setTitle("PadSynth");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static class MyOscillator extends ModelAbstractOscillator {
        double ix = 0;
        double last_ix_step = -1;
        static Main app; // default constructor is needed by inside
        public int read(float[][] buffers, int offset, int len) throws IOException {

            // Grab channel 0 buffer from buffers
            float[] buffer = buffers[0];

            //
            float gain = 1 - (((float) app.padY / app.panel.getHeight()) * 100) / 100;
Debug.println("gain: " + gain);

            // getPitch() returns midi cent (midi cent is midi note number x 100)
            // midi note number = 0 ~ 127, center c is 60
//            float pitch = getPitch();
            float pitch = (((float) app.padX / app.panel.getWidth()) * 36 + 42) * 100;
Debug.println("pitch: " + pitch);

            // Calculate ix step so sin oscillirator is tuned so 6900 cents is 440 hz
            double target_ix_step =
                    Math.exp((pitch - 6900) * (Math.log(2.0) / 1200.0))
                            * (440 / getSampleRate()) * (Math.PI * 2);
            double ix_step = last_ix_step;
            if (ix_step == -1) ix_step = target_ix_step;
            double ix_step_step = (target_ix_step - ix_step) / len;

            // Simple FM synthesizer implementation
            int endOffset = offset + len;
            for (int i = offset; i < endOffset; i++) {
                buffer[i] = (float) Math.sin(ix + Math.sin(ix * 3)) * gain;
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