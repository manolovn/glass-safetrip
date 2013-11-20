package com.google.glass.glass_safetrip.bus;

import org.alljoyn.bus.BusException;
import org.alljoyn.bus.annotation.BusInterface;
import org.alljoyn.bus.annotation.BusMethod;

/**
 * glass-safetrip
 * com.google.glass.glass_safetrip.bus
 *
 * @autor manolo
 */
@BusInterface(name = "org.alljoyn.bus.samples.simple.SimpleInterface")
public interface SimpleInterface {

    /*
     * The BusMethod annotation signifies that this function should be used as part of the AllJoyn
     * interface.  The runtime is smart enough to figure out what the input and output of the method
     * is based on the input/output arguments of the Ping method.
     *
     * All methods that use the BusMethod annotation can throw a BusException and should indicate
     * this fact.
     */
    @BusMethod
    String Ping(String inStr) throws BusException;

}
