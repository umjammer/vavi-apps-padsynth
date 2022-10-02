/*
 * https://github.com/mcourteaux/MultiTouch-Gestures-Java
 */

package com.martijncourteaux.multitouchgestures;

import com.martijncourteaux.multitouchgestures.event.MagnifyGestureEvent;
import com.martijncourteaux.multitouchgestures.event.RotateGestureEvent;
import com.martijncourteaux.multitouchgestures.event.ScrollGestureEvent;


/**
 * @author martijn
 */
public interface GestureListener {
    default void magnify(MagnifyGestureEvent e) {}

    default void rotate(RotateGestureEvent e) {}

    default void scroll(ScrollGestureEvent e) {}
}
