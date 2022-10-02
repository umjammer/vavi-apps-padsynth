/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa.moultitouch;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;


/**
 * MTTouch.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-04 nsano initial version <br>
 */
public class MTTouch extends Structure {

    public int frame;
    public double timestamp;
    public int identifier;
    // TODO JNA enum mapping
//    public MTTouchState state;
    public int state;
    public int fingerId;
    /** always 1 (maybe) */
    public int handId;
    /** mainly used .position */
    public MTVector normalizedPosition;
    /** total sensed values (maybe) */
    public float size;
    public int field9;
    public float angle;
    public float majorAxis;
    public float minorAxis;
    public MTVector absolutePosition;
    public int field14;
    public int field15;
    /** area of surface a finger contacted to (maybe) */
    public float density;

    @Override
    public String toString() {
        return "MTTouch{" +
                "frame=" + frame +
                ", timestamp=" + timestamp +
                ", identifier=" + identifier +
                ", state=" + state +
                ", fingerId=" + fingerId +
                ", handId=" + handId +
                ", normalizedPosition=" + normalizedPosition +
                ", size=" + size +
                ", field9=" + field9 +
                ", angle=" + angle +
                ", majorAxis=" + majorAxis +
                ", minorAxis=" + minorAxis +
                ", absolutePosition=" + absolutePosition +
                ", field14=" + field14 +
                ", field15=" + field15 +
                ", density=" + density +
                '}';
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("frame", "timestamp", "identifier", "state",
                "fingerId", "handId", "normalizedPosition", "size", "field9", "angle",
                "majorAxis", "minorAxis", "absolutePosition", "field14", "field15",
                "density");
    }

    // TODO works partly, w/ bug
//    public final static TypeMapper TYPE_MAPPER = new MTLibrary.MyTypeMapper();

    public MTTouch(Pointer pointer) {
        super(pointer);
        read(); // important!
    }
}
