package qualia.fr.petshop.requests;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import qualia.fr.petshop.activities.LoginActivity;
import qualia.fr.petshop.activities.MyPetsActivity;
import qualia.fr.petshop.entities.CurrentUser;
import qualia.fr.petshop.utils.DebugUtils;
import qualia.fr.petshop.utils.WebServicesUtils;

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
            throw new ClassCastException(parentActivity.toString() + " must implement OnFragmentInteractionListener");
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
            JSONObject result = WebServicesUtils.stringToJSON(getResponse());
            success = result.getInt("success");
            if (success == 1)
            {
                CurrentUser.getmInstance().setIdUser(result.getInt("id_user"));
                CurrentUser.getmInstance().setEmail(email);
                return result.getString("message");
            }
        }
        catch (JSONException e)
        {
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
            Intent intent = new Intent(LoginActivity.this, MyPetsActivity.class);
            startActivity(intent);
        }
        else
        {
            if (message.equals(WebServicesUtils.connectionErrorMessage) || message.equals(WebServicesUtils.responseErrorMessage))
                DebugUtils.logToast(LoginActivity.this, message);
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
    }

}
