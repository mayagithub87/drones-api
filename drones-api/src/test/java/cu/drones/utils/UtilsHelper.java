package cu.drones.utils;

import java.util.Random;

public class UtilsHelper {

    public static final String DRONE_OK_SERIAL_NUMBER = "Drone-1-MIDDLE-WEIGHT";

    public static final String DRONE_OK_SERIAL_NUMBER_2 = "Drone-2-LIGHT-WEIGHT";

    public static final String[] MEDICATION_NAMES = {"Advil-50mg_box", "Ibuprofen-25mg_box", "Aspirin-15mg_box"};

    public static final String[] MEDICATION_CODES = {"ADV_01", "IBUPROFEN_04514", "ASPIRIN_2410"};

    public static byte[] generateByteArray() {
        Random rd = new Random();
        byte[] bytes = new byte[7];
        rd.nextBytes(bytes);
        return bytes;
    }


}
