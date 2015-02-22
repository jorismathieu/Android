package test.fr.petshop.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import test.fr.petshop.R;
import test.fr.petshop.dao.AnimalDAO;
import test.fr.petshop.entities.Animal;


public class EditAnimalFragment extends Fragment
{
    private static final String ARG_PARAM0 = "id";
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "name";
    private static final String ARG_PARAM3 = "description";
    private static final String ARG_PARAM4 = "address";
    private static final String ARG_PARAM5 = "latitude";
    private static final String ARG_PARAM6 = "longitude";

    private int mParam0;
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;

    private EditText                type;
    private EditText                name;
    private EditText                description;
    private EditText                address;
    private EditText                latitude;
    private EditText                longitude;
    private Button                  mEdit;

    private AnimalDAO               animalDAO;
    private Activity                parentActivity;


    private ActivityListener mListener;

    public static EditAnimalFragment newInstance(int param0, String param1, String param2, String param3, String param4, String param5, String param6)
    {
        EditAnimalFragment fragment = new EditAnimalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM0, param0);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    public EditAnimalFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam0 = getArguments().getInt(ARG_PARAM0);
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
        animalDAO = new AnimalDAO(parentActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_edit_animal, container, false);

        mEdit = (Button)v.findViewById(R.id.editAnimal);

        type = (EditText)v.findViewById(R.id.type);
        name = (EditText)v.findViewById(R.id.name);
        description = (EditText)v.findViewById(R.id.description);
        address = (EditText)v.findViewById(R.id.address);
        latitude = (EditText)v.findViewById(R.id.latitude);
        longitude = (EditText)v.findViewById(R.id.longitude);

        type.setText(mParam1);
        name.setText(mParam2);
        description.setText(mParam3);
        address.setText(mParam4);
        latitude.setText(mParam5);
        longitude.setText(mParam6);

        mEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mParam1 = type.getText().toString();
                mParam2 = name.getText().toString();
                mParam3 = description.getText().toString();
                mParam4 = address.getText().toString();
                mParam5 = latitude.getText().toString();
                mParam6 = longitude.getText().toString();

                Animal editAnimal = new Animal(mParam0, mParam2, mParam3, mParam1, mParam4, mParam5 + "@" + mParam6);
                animalDAO.update(editAnimal);
                mListener.editAnimalInList(editAnimal);
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
            parentActivity = activity;
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
        public void editAnimalInList(Animal animal);
    }

}
