package test.fr.petshop.requests;

import android.app.Activity;
import android.os.Debug;

import org.json.JSONObject;

import test.fr.petshop.entities.CurrentUser;
import test.fr.petshop.utils.DebugUtils;
import test.fr.petshop.utils.WebServicesUtils;

public class LoginRequest extends MyPetRequest
{
    private String                      email;
    private String                      passwd;
    private LoginRequestListener        listenerActivty;

    private int             success = 0;

    public LoginRequest(Activity parentActivity, String email, String passwd)
    {
        this.scriptName = WebServicesUtils.loginScript;
        this.email = email;
        this.passwd = passwd;
        try
        {
            this.listenerActivty = (LoginRequestListener)parentActivity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(parentActivity.toString() + " must implement LoginRequestListener");
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        AddParam("Email", email);
        AddParam("Passwd", passwd);

        try
        {
            createRequest(RequestMethod.POST);
        }
        catch (Exception e)
        {
            return WebServicesUtils.connectionErrorMessage;
        }
        try
        {
            DebugUtils.log(getResponse());
            JSONObject result = WebServicesUtils.stringToJSON(getResponse());
            if (result != null)
            {
                success = result.getInt("success");
                if (success == 1)
                {
                    CurrentUser.getmInstance().setIdUser(result.getInt("id_user"));
                    CurrentUser.getmInstance().setEmail(email);
                    CurrentUser.getmInstance().setConnected(true);
                }
                return result.getString("message");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return WebServicesUtils.responseErrorMessage;
        }
        return null;
    }


    @Override
    protected void onPostExecute(final String message)
    {
        this.listenerActivty.onCancelLoginRequest();

        if (success == 1)
        {
            this.listenerActivty.onLaunchMyPetsActivity();
        }
        else
        {
            if (message.equals(WebServicesUtils.connectionErrorMessage) || message.equals(WebServicesUtils.responseErrorMessage))
                this.listenerActivty.onConnectionError(message);
            else
            {
                 this.listenerActivty.onPasswdErrorLoginRequest();
            }
        }
    }

    @Override
    protected void onCancelled()
    {
        this.listenerActivty.onCancelLoginRequest();
    }

    public interface LoginRequestListener
    {
        public void onPasswdErrorLoginRequest();
        public void onCancelLoginRequest();
        public void onLaunchMyPetsActivity();
        public void onConnectionError(String message);
    }

}
