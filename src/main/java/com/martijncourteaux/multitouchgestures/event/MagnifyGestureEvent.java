/*
 * https://github.com/mcourteaux/MultiTouch-Gestures-Java
 */

package com.martijncourteaux.multitouchgestures.event;

import javax.swing.JComponent;


/**
 * @author martijn
 */
public class MagnifyGestureEvent extends GestureEvent {

    private final double magnification;

    public MagnifyGestureEvent(JComponent source, double mouseX, double mouseY, double absMouseX, double absMouseY, Phase phase, double magnification) {
        super(source, mouseX, mouseY, absMouseX, absMouseY, phase);
        this.magnification = magnification;
    }

    public double getMagnification() {
        return magnification;
    }
}
