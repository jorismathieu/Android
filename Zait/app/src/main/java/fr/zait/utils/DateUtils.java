package fr.zait.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils {
    public static String getDateFromTimestamp(long timestamp) {
        DateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTimeInMillis(timestamp * 1000);

        String result = objFormatter.format(objCalendar.getTime());
        return result;
    }

    public static boolean isDuringNight() {
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        if (currentHour >= 22 || currentHour <= 7) {
            return true;
        }
        return false;
    }
}
