/*
 * https://github.com/mcourteaux/MultiTouch-Gestures-Java
 */

package com.martijncourteaux.multitouchgestures;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.SwingUtilities;

import com.martijncourteaux.multitouchgestures.event.GestureEvent.Phase;


/**
 * @author martijn
 */
class EventDispatch {

    private static final String LIBNAME = "macpad";

    private static final boolean supported;
    private static Thread gestureEventThread;

    static {
        boolean supp = false;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac os x")) {
            System.loadLibrary(LIBNAME);
            supp = true;
        } else {
            System.err.println("[MULTITOUCH GESTURES] Only Mac OS X is supported at the moment.");
        }

        if (supp) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.err.println("In shutdownHook. Stopping Event Tap.");
                EventDispatch.stop();
            }));
        }

        supported = supp;
    }

    public static native void init();

    private static native void start();

    public static native void stop();

    public static void startInSeparateThread() {
        if (!supported) {
            return;
        }

        if (gestureEventThread != null) {
            if (gestureEventThread.isAlive()) {
                return;
            }
        }

        gestureEventThread = new Thread(() -> {
            init();
            start();
        }, "Gesture Event Thread");
        gestureEventThread.start();
    }

    public static void dispatchMagnifyGesture(final double mouseX, final double mouseY, final double magnification, final int phase) {
        SwingUtilities.invokeLater(() -> {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            MultiTouchGestureUtilities.dispatchMagnifyGesture(mouseX, d.height - mouseY, magnification, Phase.getByCode(phase));
        });
    }

    public static void dispatchRotateGesture(final double mouseX, final double mouseY, final double rotation, final int phase) {
        SwingUtilities.invokeLater(() -> {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            MultiTouchGestureUtilities.dispatchRotateGesture(mouseX, d.height - mouseY, -Math.toRadians(rotation), Phase.getByCode(phase));
        });
    }

    public static void dispatchScrollWheelEvent(final double mouseX, final double mouseY, final double deltaX, final double deltaY, final int phase) {
        SwingUtilities.invokeLater(() -> {
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            MultiTouchGestureUtilities.dispatchScrollGesture(mouseX, d.height - mouseY, deltaX, deltaY, Phase.getByCode(phase));
        });
    }
}
