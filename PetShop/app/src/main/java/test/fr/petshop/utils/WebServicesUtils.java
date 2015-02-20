package test.fr.petshop.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebServicesUtils
{
    public static final String  url = "http://192.168.0.12/PetShop/";
    public static final int     timeoutConnection = 3000;
    public static final int     timeoutSocket = 3000;

    public static final int     errorCode = -1;
    public static final String  connectionErrorMessage = "Erreur lors de la connection au serveur";
    public static final String  responseErrorMessage = "Erreur lors de la récupération des informations";

    public static final String  loginScript = "login.php";


    public static JSONObject stringToJSON(String result) throws Exception
    {
        return new JSONObject(result);
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
