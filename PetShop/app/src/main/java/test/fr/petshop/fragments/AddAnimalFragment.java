package test.fr.petshop.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import test.fr.petshop.R;
import test.fr.petshop.adapters.AnimalsAdapter;
import test.fr.petshop.dao.AnimalDAO;
import test.fr.petshop.entities.Animal;
import test.fr.petshop.requests.GetDataRequest;


public class AddAnimalFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ActivityListener        mListener;
    private Button                  mAdd;

    private AnimalDAO               animalDAO;

    private EditText                type;
    private EditText                name;
    private EditText                description;
    private EditText                address;
    private EditText                latitude;
    private EditText                longitude;


    public static AddAnimalFragment newInstance(String param1, String param2)
    {
        AddAnimalFragment fragment = new AddAnimalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddAnimalFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_add_animal, container, false);
        mAdd = (Button)v.findViewById(R.id.addAnimal);
        animalDAO = new AnimalDAO(getActivity());

        type = (EditText)v.findViewById(R.id.type);
        name = (EditText)v.findViewById(R.id.name);
        description = (EditText)v.findViewById(R.id.description);
        address = (EditText)v.findViewById(R.id.address);
        latitude = (EditText)v.findViewById(R.id.latitude);
        longitude = (EditText)v.findViewById(R.id.longitude);

        mAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int id = animalDAO.getMaxID() + 1;
                Animal newAnimal = new Animal(id, name.getText().toString(), description.getText().toString(), type.getText().toString(),
                        address.getText().toString(), latitude.getText().toString() + "@" + longitude.getText().toString());
                animalDAO.create(newAnimal);
                mListener.addAnimalInList(newAnimal);
                getFragmentManager().popBackStackImmediate();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (ActivityListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() + " must implement ActivityListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface ActivityListener
    {
        public void displayMessage(String message);
        public void addAnimalInList(Animal animal);
    }

}
