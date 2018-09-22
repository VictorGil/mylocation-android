package net.devaction.mylocation.db;

/**
 * @author Víctor Gil
 *
 * since September 2018
 */
public class Schema{
    public static final String ON = OnOrOff.ON.name();
    public static final String OFF = OnOrOff.OFF.name();;

    public static final class LocationServiceStateTable{
        public static final String NAME = "locationServiceState";
        public static final class Cols {
            public static final String STATE = "state"; //only two possible values: "ON" or "OFF"
         }
    }
}

