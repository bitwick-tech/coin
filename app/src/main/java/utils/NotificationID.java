package utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by neerajlajpal on 11/02/18.
 */

public class NotificationID {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
