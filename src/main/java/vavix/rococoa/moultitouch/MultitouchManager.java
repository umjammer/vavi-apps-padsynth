/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa.moultitouch;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import org.rococoa.ID;
import vavi.util.Debug;


/**
 * MultitouchManager.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-04 nsano initial version <br>
 */
public class MultitouchManager {

    /** */
    private static final MultitouchManager instance = new MultitouchManager();

    /** */
    private MultitouchManager() {
        start();
    }

    /** */
    public static MultitouchManager getInstance() {
        return instance;
    }

    /** */
    private ID device;

    /** */
    private final List<MultitouchEventListener> listeners = new ArrayList<>();

    /** */
    private final MTLibrary.MTFrameCallbackFunction callback = (device, touches, touchesCount, timestamp, frame) -> {
        synchronized (listeners) {
            MTTouch[] touches_;
            if (touchesCount == 0) {
                touches_ = new MTTouch[0];
            } else {
                MTTouch touch_ = new MTTouch(touches);
                touches_ = (MTTouch[]) touch_.toArray(touchesCount);
            }

            for (MultitouchEventListener listener : listeners) {
                listener.touched(new MultitouchEvent(this, touches_));
            }
        }
    };

    /** */
    private void start() {
        device = MTLibrary.INSTANCE.MTDeviceCreateDefault();
Debug.println("MTDeviceCreateDefault: " + device);

        MTLibrary.INSTANCE.MTRegisterContactFrameCallback(device, callback);
        MTLibrary.INSTANCE.MTDeviceStart(device, 0);
    }

    /** */
    public void stop() {
        MTLibrary.INSTANCE.MTUnregisterContactFrameCallback(device, callback);
        MTLibrary.INSTANCE.MTDeviceStop(device);
        MTLibrary.INSTANCE.MTDeviceRelease(device);
Debug.println("MTDeviceRelease: " + device);
    }

    /** */
    public static class MultitouchEvent extends EventObject {
        private final MTTouch[] touches;
        /** */
        public MultitouchEvent(Object source, MTTouch[] touches) {
            super(source);
            this.touches = touches;
        }
        public MTTouch[] getTouches() {
            return touches;
        }
    }

    /** */
    public interface MultitouchEventListener extends EventListener {
        void touched(MultitouchEvent event);
    }

    /** */
    public void addListener(MultitouchEventListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /** */
    public void removeListener(MultitouchEventListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }
}
