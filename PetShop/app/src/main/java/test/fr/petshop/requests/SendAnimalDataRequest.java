package test.fr.petshop.requests;

import android.app.Activity;
import android.support.v4.app.Fragment;

import org.json.JSONObject;

import java.util.ArrayList;

import test.fr.petshop.entities.Animal;
import test.fr.petshop.utils.DebugUtils;
import test.fr.petshop.utils.WebServicesUtils;

public class SendAnimalDataRequest extends MyPetRequest
{
    private FragmentListener                            fragmentListener;
    private ArrayList<Animal>                           animals;

    private int             success = 0;

    public SendAnimalDataRequest(Fragment parentFragment, ArrayList<Animal> animals)
    {
        this.scriptName = WebServicesUtils.animalsScript;
        this.animals = animals;

        try
        {
            this.fragmentListener = (FragmentListener)parentFragment;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(parentFragment.toString() + " must implement FragmentListener");
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        String animalsParam = new String();
        for (Animal animal : animals)
        {
            animalsParam += animal.getType() + "|" + animal.getName() + "|" + animal.getDescription() + "|" +  animal.getAddress() + "|" +  animal.getCoordinates() + "#";
        }

        AddParam("animals", animalsParam);

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
        this.fragmentListener.onCancelAnimalDataRequest();

        if (success != 1)
        {
            if (message.equals(WebServicesUtils.connectionErrorMessage) || message.equals(WebServicesUtils.responseErrorMessage))
                this.fragmentListener.onConnectionError(message);
        }
    }

    @Override
    protected void onCancelled()
    {
        this.fragmentListener.onCancelAnimalDataRequest();
    }

    public interface FragmentListener
    {
        public void onCancelAnimalDataRequest();
        public void onConnectionError(String message);
    }
}
