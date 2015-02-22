package test.fr.petshop.requests;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v4.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import test.fr.petshop.adapters.AnimalsAdapter;
import test.fr.petshop.dao.AnimalDAO;
import test.fr.petshop.database.AnimalHelper;
import test.fr.petshop.entities.Animal;
import test.fr.petshop.utils.DebugUtils;
import test.fr.petshop.utils.WebServicesUtils;

public class GetDataRequest extends MyPetRequest
{
    private FragmentListener                            fragmentListener;
    private Integer                                     userId;
    private int                                         success = 0;

    private ArrayList<Animal>                           animalList;
    private JSONArray                                   animals;
    private AnimalsAdapter                              adapter;
    private AnimalDAO                                   animalDAO;

    private JSONArray                                   purchases;


    public GetDataRequest(Fragment parentFragment, Integer userId, AnimalsAdapter adapter)
    {
        this.scriptName = WebServicesUtils.getScript;
        this.userId = userId;
        this.adapter = adapter;
        this.animalDAO = new AnimalDAO(parentFragment.getActivity());

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
        if (userId != null)
            AddParam("userId", String.valueOf(userId));

        try
        {
            createRequest(MyPetRequest.RequestMethod.POST);
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
                    animals = result.getJSONArray("animalList");
                    //purchases = result.getString("purchases");
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
        this.fragmentListener.onCancelGetDataRequest();

        if (success == 1)
        {
            // Parcourir animals, stocker dans liste puis mettre a jour ladapter
            animalList = new ArrayList<Animal>();
            try
            {
                animalDAO.dropTable();
                for (int i = 0; i < animals.length(); i++)
                {
                    JSONObject jsonAnimal = animals.getJSONObject(i);
                    Animal newAnimal = new Animal(jsonAnimal.getInt("id"), jsonAnimal.getString("name"), jsonAnimal.getString("description"), jsonAnimal.getString("type"), jsonAnimal.getString("address"), jsonAnimal.getString("coordinates"));
                    animalList.add(newAnimal);
                    animalDAO.create(newAnimal);
                }
                adapter.setAnimalList(animalList);

            }
            catch (JSONException e)
            {
                this.fragmentListener.onConnectionError(message);
            }

        }
        else
        {
            if (message.equals(WebServicesUtils.connectionErrorMessage) || message.equals(WebServicesUtils.responseErrorMessage))
                this.fragmentListener.onConnectionError(message);
        }
    }

    @Override
    protected void onCancelled()
    {
        this.fragmentListener.onCancelGetDataRequest();
    }

    public interface FragmentListener
    {
        public void onCancelGetDataRequest();
        public void onConnectionError(String message);
    }
}
