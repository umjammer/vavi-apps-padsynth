/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavix.rococoa.gesture;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.jna.Pointer;
import org.rococoa.cocoa.CFIndex;
import org.rococoa.cocoa.appkit.AppKitLibrary;
import org.rococoa.cocoa.appkit.NSEvent;
import org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary;
import org.rococoa.cocoa.foundation.NSPoint;
import vavi.util.Debug;

import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGEventMaskForAllEvents;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGEventTapOptionListenOnly;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGHIDEventTap;
import static org.rococoa.cocoa.coregraphics.CoreGraphicsLibrary.kCGHeadInsertEventTap;
import static org.rococoa.cocoa.foundation.FoundationKitFunctions.kCFRunLoopCommonModes;
import static org.rococoa.cocoa.foundation.FoundationKitFunctions.library;


/**
 * GestureLibrary.
 *
 * TODO doesn't work, callback never be called
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-10 nsano initial version <br>
 */
public class GestureLibrary {

    private GestureLibrary() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
Debug.println("shutdownHook");
            stop();
        }));
        es.submit(this::start);
    }

    private static GestureLibrary instance = new GestureLibrary();

    public static GestureLibrary getInstance() {
        return instance;
    }

    private Pointer /*CFMachPortRef*/ eventTap;
    private Pointer /*CFRunLoopRef*/ runLoop;
    private ExecutorService es = Executors.newSingleThreadExecutor();

    enum NSEventPhase {
        None(0),
        Began(0x1 << 0),
        Stationary(0x1 << 1),
        Changed(0x1 << 2),
        Ended(0x1 << 3),
        Cancelled(0x1 << 4),
        MayBegin(0x1 << 5);
        final int v;

        NSEventPhase(int v) {
            this.v = v;
        }

        static NSEventPhase valueOf(int t) {
            return Arrays.stream(values()).filter(e -> e.v == t).findFirst().get();
        }
    }

    private static final long eventMask =
            NSEvent.NSEventMaskGesture |
            NSEvent.NSEventMaskMagnify |
            NSEvent.NSEventMaskSwipe |
            NSEvent.NSEventMaskRotate |
            NSEvent.NSEventMaskScrollWheel;

    private Pointer /*CGEventRef*/ eventTapCallback(Pointer/*CGEventTapProxy*/ proxy, int/*CGEventType*/ type, Pointer/*CGEventRef*/ eventRef, Pointer refcon) {

Debug.printf("here0");
        if (type <= 0) {
Debug.printf("here1");
            return eventRef;
        }

        // convert the CGEventRef to an NSEvent
        NSEvent event = NSEvent.CLASS.eventWithCGEvent(eventRef);

        // filter out events which do not match the mask
        if ((eventMask & AppKitLibrary.INSTANCE.NSEventMaskFromType(event.type())) == 0) {
Debug.printf("here2");
            return event.CGEvent();
        }

        NSPoint m = event.mouseLocation();
        NSEventPhase phase = NSEventPhase.valueOf(event.phase());

Debug.printf("%d, %s%n", event.type(), phase);

        switch (event.type()) {
        case NSEvent.NSEventTypeMagnify: {
            // (double) m.x, (double) m.y, event.magnification, phase);
            break;
        }
        case NSEvent.NSEventTypeSwipe:
            // Debug.printf("Swipe: X = %10.6f; Y = %10.6f", event.deltaX, event.deltaY);
            break;
        case NSEvent.NSEventTypeRotate: {
            // (double) m.x, (double) m.y, event.rotation, phase);
            break;
        }
        case NSEvent.NSEventTypeScrollWheel: {
            // m.x, (double) m.y, event.scrollingDeltaX, event.scrollingDeltaY, phase);
            break;
        }
        default:
            break;
        }

        return event.CGEvent();
    }

    private void start() {
Debug.printf("eventMask: %d, %d, %d, %16x", kCGHIDEventTap, kCGHeadInsertEventTap, kCGEventTapOptionListenOnly, kCGEventMaskForAllEvents);
        eventTap = CoreGraphicsLibrary.library.CGEventTapCreate(
                kCGHIDEventTap,
                kCGHeadInsertEventTap,
                kCGEventTapOptionListenOnly,
                kCGEventMaskForAllEvents,
                this::eventTapCallback,
                null);
Debug.println("eventTap: " + eventTap);
        Pointer runLoopSource = library.CFMachPortCreateRunLoopSource(null, eventTap, CFIndex.valueOf(0));
Debug.println("runLoopSource: " + runLoopSource);
        library.CFRunLoopAddSource(library.CFRunLoopGetCurrent(), runLoopSource, kCFRunLoopCommonModes);
        CoreGraphicsLibrary.library.CGEventTapEnable(eventTap, true);
        runLoop = library.CFRunLoopGetCurrent();
Debug.println("runLoop: " + runLoop);
        library.CFRunLoopRun();
Debug.println("[rococoa] Starting Gesture Listener.");
    }

    private void stop() {
        library.CFRunLoopStop(runLoop);
        CoreGraphicsLibrary.library.CGEventTapEnable(eventTap, false);
System.err.println("[rococoa] Stopping Gesture Listener.");
    }
}
