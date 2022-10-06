/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.apps.padsynth;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


/**
 * MidiUtil.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-10-02 nsano initial version <br>
 */
public class MidiUtil {

    private MidiUtil() {
    }

    public static MidiDevice.Info[] getInputDevices() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        List<MidiDevice.Info> result = new ArrayList<>();
        for (MidiDevice.Info info : infos) {
            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info);
//System.err.println("I: " + device.getDeviceInfo() + ", " + device.getMaxTransmitters());
                if (device.getMaxTransmitters() == 0) {
                    continue;
                }
                result.add(info);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
//Debug.println("I: " + result.size());
        return result.toArray(new MidiDevice.Info[0]);
    }

    public static MidiDevice.Info[] getOutputDevices() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        List<MidiDevice.Info> result = new ArrayList<>();
        for (MidiDevice.Info info : infos) {
            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info);
//System.err.println("O: " + device.getDeviceInfo() + ", " + device.getMaxReceivers());
                if (device.getMaxReceivers() == 0) {
                    continue;
                }
                result.add(info);
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
//Debug.println("O: " + result.size());
        return result.toArray(new MidiDevice.Info[0]);
    }

}
