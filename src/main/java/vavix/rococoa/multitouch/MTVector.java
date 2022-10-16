/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa.multitouch;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;


/**
 * MTVector.
 *
 * size 16
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-04 nsano initial version <br>
 */
public class MTVector extends Structure {

    public MTPoint position;
    public MTPoint velocity;

    @Override
    public String toString() {
        return "MTVector{" +
                "position=" + position +
                ", velocity=" + velocity +
                '}';
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("position", "velocity");
    }
}
