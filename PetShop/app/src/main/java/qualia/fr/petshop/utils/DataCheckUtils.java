package qualia.fr.petshop.utils;

import java.util.regex.Pattern;

public class DataCheckUtils
{
    public static boolean isValidEmail(String email)
    {
        return Pattern.compile(email).matcher("^[_a-z0-9-]+(\\\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\\\.[a-z0-9-]+)+$").matches();
    }

    public static boolean isPassword(String password)
    {
        if (password.length() < 4)
            return false;
        return true;
    }
}
