package fr.zait.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    public static HttpURLConnection getConnection(Context context, String url) {
        HttpURLConnection hcon = null;
        try {
            hcon = (HttpURLConnection) new URL(url).openConnection();
            hcon.setReadTimeout(30000);
            hcon.setRequestProperty("User-Agent", "Zait");
        }
        catch (Exception e) {
        }
        return hcon;
    }

    public static String readContents(Context context, String url) {
        HttpURLConnection hcon = getConnection(context, url);
        if (hcon == null) {
            return null;
        }
        try {
            StringBuffer sb = new StringBuffer(8192);
            String tmp = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(hcon.getInputStream()));
            while ((tmp = br.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
            br.close();
            return sb.toString();
        }
        catch (Exception e) {
            return null;
        }
    }
}
