/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa.moultitouch;

import vavix.rococoa.JnaEnum;


/**
 * MTTouchState.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-04 nsano initial version <br>
 * @see "http://technofovea.com/blog/archives/815"
 */
public enum MTTouchState implements JnaEnum<MTTouchState> {
    MTTouchStateNotTracking,
    MTTouchStateStartInRange,
    MTTouchStateHoverInRange,
    MTTouchStateMakeTouch,
    MTTouchStateTouching,
    MTTouchStateBreakTouch,
    MTTouchStateLingerInRange,
    MTTouchStateOutOfRange;

    @Override
    public int getIntValue() {
        return this.ordinal();
    }

    @Override
    public MTTouchState getForValue(int i) {
        for (MTTouchState o : values()) {
            if (o.getIntValue() == i) {
                return o;
            }
        }
        return null;
    }
}
