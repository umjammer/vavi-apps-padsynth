/*
 * https://github.com/mcourteaux/MultiTouch-Gestures-Java
 */

package com.martijncourteaux.multitouchgestures.event;

import javax.swing.JComponent;


/**
 * @author martijn
 */
public class RotateGestureEvent extends GestureEvent {

    private final double rotation;

    public RotateGestureEvent(JComponent source, double mouseX, double mouseY, double absMouseX, double absMouseY, Phase phase, double rotation) {
        super(source, mouseX, mouseY, absMouseX, absMouseY, phase);
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }
}
