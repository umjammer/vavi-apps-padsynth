/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import vavi.apps.padsynth.MidiUtil;
import vavix.rococoa.gesture.GestureLibrary;


/**
 * Test2.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-03 nsano initial version <br>
 */
class Test2 {

    private static Logger logger = Logger.getLogger(Test2.class.getName());

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void test1() throws Exception {
        main(new String[0]);
        while (true) Thread.yield();
    }

    public static void main(String[] args) throws Exception {
        GestureLibrary.getInstance();

        JFrame frame = new JFrame();
        frame.setTitle("GestureLibrary");
        JComponent comp = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(comp, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(640, 480));

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    @Test
    void test2() throws Exception {
        System.err.println("---- input ----");
        Arrays.stream(MidiUtil.getInputDevices()).forEach(System.err::println);
        System.err.println("---- output ----");
        Arrays.stream(MidiUtil.getOutputDevices()).forEach(System.err::println);
    }
}
