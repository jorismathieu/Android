package fr.zait.utils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtils
{
    public static String getDateFromTimestamp(long timestamp) {
        DateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTimeInMillis(timestamp * 1000);

        String result = objFormatter.format(objCalendar.getTime());
        return result;
    }
}
