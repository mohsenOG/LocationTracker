package eu.wonderfulme.locationtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {}

    static String getFormattedTime (long timeInMilliseconds) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date resultDate = new Date(timeInMilliseconds);
        return simpleDateFormat.format(resultDate);
    }
}
