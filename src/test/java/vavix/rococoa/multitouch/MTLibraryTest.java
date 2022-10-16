/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa.multitouch;

import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.rococoa.ID;


/**
 * MTLibraryTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-03 nsano initial version <br>
 */
class MTLibraryTest {

    static {
        MTLibrary.INSTANCE.toString();
    }

    @Test
    void test1() throws Exception {
        System.err.println("MTDeviceIsAvailable: " + MTLibrary.INSTANCE.MTDeviceIsAvailable());
    }

    @Test
    @Disabled("doesn't work")
    void test2() throws Exception {
        List<?> devices = MTLibrary.getMultiTouchDevices();
        System.err.println("getMultiTouchDevices: " + devices.size());
    }

    @Test
    void test3() throws Exception {
        ID device = MTLibrary.INSTANCE.MTDeviceCreateDefault();
        System.err.println("MTDeviceCreateDefault: " + device);
    }

    /** @see "https://stackoverflow.com/a/6951347" */
    static MTLibrary.MTFrameCallbackFunction callback = (device, touches, touchesCount, timestamp, frame) -> {
        if (touchesCount == 0) {
            return;
        }

        MTTouch touch_ = new MTTouch(touches);
        MTTouch[] touches_ = (MTTouch[]) touch_.toArray(touchesCount);

        for (MTTouch touch : touches_) {
            System.err.printf("%d, %3.1f, %3.1f%n", touch.identifier, touch.absolutePosition.position.x, touch.absolutePosition.position.y);
        }
    };

    @Test
    @EnabledIfSystemProperty(named = "vavi.test", matches = "ide")
    void test4() throws Exception {
        ID device = MTLibrary.INSTANCE.MTDeviceCreateDefault();
        System.err.println("MTDeviceCreateDefault: " + device);

        MTLibrary.INSTANCE.MTRegisterContactFrameCallback(device, callback);
        MTLibrary.INSTANCE.MTDeviceStart(device, 0);
        while (true) Thread.yield();
    }
}
