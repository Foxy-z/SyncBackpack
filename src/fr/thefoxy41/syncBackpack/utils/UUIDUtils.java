package fr.thefoxy41.syncBackpack.utils;

import java.util.UUID;

public class UUIDUtils {

    /**
     * Generate random uuid
     * @return String
     */
    public static String randomToString() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Generate random uuid of certain size
     * @param size int
     * @return String
     */
    public static String randomToString(int size) {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().subSequence(0, size).toString();
    }
}
