/*
 * Copyright 2007, 2008, 2009 Duncan McGregor
 *
 * This file is part of Rococoa, a library to allow Java to talk to Cocoa.
 *
 * Rococoa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Rococoa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Rococoa.  If not, see <http://www.gnu.org/licenses/>.
 */

package vavix.rococoa.moultitouch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.sun.jna.Callback;
import com.sun.jna.FromNativeContext;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ToNativeContext;
import com.sun.jna.TypeConverter;
import org.rococoa.ID;
import org.rococoa.cocoa.foundation.NSArray;
import org.rococoa.internal.RococoaTypeMapper;
import vavix.rococoa.JnaEnum;


/**
 * MTLibrary.
 *
 * TODO enum converter doesn't work
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (umjammer)
 * @version 0.00 2022/09/03 umjammer initial version <br>
 * @see "https://github.com/mhuusko5/M5MultitouchSupport/blob/master/M5MultitouchSupport/M5MTDefinesInternal.h"
 */
public interface MTLibrary extends Library {

    /** @see "http://technofovea.com/blog/archives/815" */
    class EnumConverter implements TypeConverter {

        private static final Logger logger = Logger.getLogger(EnumConverter.class.getName());

        public Object fromNative(Object input, FromNativeContext context) {
            Integer i = (Integer) input;
            Class<?> targetClass = context.getTargetType();
            if (!JnaEnum.class.isAssignableFrom(targetClass)) {
                return null;
            }
            Object[] enums = targetClass.getEnumConstants();
            if (enums.length == 0) {
                logger.severe("Could not convert desired enum type (), no valid values are defined. " + targetClass.getName());
                return null;
            }
            // In order to avoid nasty reflective junk and to avoid needing
            // to know about every subclass of JnaEnum, we retrieve the first
            // element of the enum and make IT do the conversion for us.

            JnaEnum<?> instance = (JnaEnum<?>) enums[0];
            return instance.getForValue(i);
        }

        public Object toNative(Object input, ToNativeContext context) {
            JnaEnum<?> j = (JnaEnum<?>) input;
            if (j == null) return null;
            return j.getIntValue();
        }

        public Class<?> nativeType() {
            return Integer.class;
        }
    }

    /** @see "http://technofovea.com/blog/archives/815" */
    class MyTypeMapper extends RococoaTypeMapper {

        MyTypeMapper() {

            // The EnumConverter is set to fire when instances of
            // our interface, JnaEnum, are seen.
            addTypeConverter(JnaEnum.class, new EnumConverter());
        }
    }

    MTLibrary INSTANCE = Native.load(
                "Cocoa", MTLibrary.class, Collections.singletonMap(Library.OPTION_TYPE_MAPPER, new MyTypeMapper()));

    /** @see "https://stackoverflow.com/a/6951347" */
    interface MTFrameCallbackFunction extends Callback {
        void invoke(ID /*MTDeviceRef*/ device, Pointer /*MTTouch[]*/ touches, int numTouches, double timestamp, int frame);
    }

    boolean MTDeviceIsAvailable();

    /**
     * doesn't work
     * TODO obj-c __bridge
     */
    NSArray /* CFMutableArray */ MTDeviceCreateList();

    /**
     * doesn't work
     * TODO obj-c __bridge
     */
    static List<Object> getMultiTouchDevices() {
        List<Object> result = new ArrayList<>();
        NSArray array = INSTANCE.MTDeviceCreateList();
        for (int i = 0; i < array.count(); i++) {
            result.add(array.objectAtIndex(i));
        }
        return result;
    }

    ID MTDeviceCreateDefault();

    void MTRegisterContactFrameCallback(ID mtDevice, MTFrameCallbackFunction mtEventHandler);

    void MTUnregisterContactFrameCallback(ID mtDevice, MTFrameCallbackFunction mtEventHandler);

    void MTDeviceStart(ID mtDevice, int a);

    void MTDeviceStop(ID mtDevice);

    void MTDeviceRelease(ID mtDevice);
}
