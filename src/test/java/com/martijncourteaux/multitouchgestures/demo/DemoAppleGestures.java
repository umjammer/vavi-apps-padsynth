/*
 * https://github.com/mcourteaux/MultiTouch-Gestures-Java
 */

package com.martijncourteaux.multitouchgestures.demo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.apple.eawt.event.GestureUtilities;
import com.apple.eawt.event.MagnificationEvent;
import com.apple.eawt.event.RotationEvent;
import com.apple.eawt.event.SwipeEvent;
import vavi.util.Debug;


/**
 * using apple eawt
 *
 * TODO swipe doesn't work, not implemented ???
 *
 * @author martijn
 */
public class DemoAppleGestures {

    private static double a = 0, l = 50;
    private static double x, y;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.setTitle("Apple Gestures Demo");
        final JComponent comp = new JComponent() {

            @Override
            protected void paintComponent(Graphics gg) {
                super.paintComponent(gg);
                Graphics2D g = (Graphics2D) gg;

                Line2D.Double line = new Line2D.Double(getWidth() * 0.5 + x, getHeight() * 0.5 + y, getWidth() * 0.5 + Math.cos(a) * l + x, getHeight() * 0.5 + Math.sin(a) * l + y);
                g.setColor(Color.red);
                g.setStroke(new BasicStroke(5.0f));
                g.draw(line);
            }

        };
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(comp, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(640, 480));

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        comp.addMouseWheelListener(e -> {
            if (e.isShiftDown()) {
                x -= 3.0f * e.getPreciseWheelRotation();
            } else {
                y -= 3.0f * e.getPreciseWheelRotation();
            }
            comp.repaint();
        });

        GestureUtilities.addGestureListenerTo(comp, new com.apple.eawt.event.GestureAdapter() {

            @Override
            public void magnify(MagnificationEvent me) {
                l *= 1.0 + me.getMagnification();
                comp.repaint();
            }

            @Override
            public void rotate(RotationEvent re) {
                a -= Math.toRadians(re.getRotation());
                comp.repaint();
            }

            @Override
            public void swipedDown(SwipeEvent se) {
Debug.println("swipeDown");
            }

            @Override
            public void swipedLeft(SwipeEvent se) {
Debug.println("swipedLeft");
            }

            @Override
            public void swipedRight(SwipeEvent se) {
Debug.println("swipedRight");
            }

            @Override
            public void swipedUp(SwipeEvent se) {
Debug.println("swipedUp");
            }
        });
    }
}
