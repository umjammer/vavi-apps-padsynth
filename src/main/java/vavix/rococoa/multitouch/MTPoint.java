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
 * MTPoint.
 *
 * size 8
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-04 nsano initial version <br>
 */
public class MTPoint extends Structure {
    public float x;
    public float y;

    @Override
    public String toString() {
        return "MTPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("x", "y");
    }
}
