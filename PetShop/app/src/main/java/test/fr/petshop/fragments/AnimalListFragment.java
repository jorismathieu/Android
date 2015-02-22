package test.fr.petshop.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import test.fr.petshop.R;
import test.fr.petshop.adapters.AnimalsAdapter;
import test.fr.petshop.dao.AnimalDAO;
import test.fr.petshop.entities.Animal;
import test.fr.petshop.entities.CurrentUser;
import test.fr.petshop.requests.GetDataRequest;
import test.fr.petshop.requests.SendAnimalDataRequest;


public class AnimalListFragment extends Fragment implements AbsListView.OnItemClickListener, SendAnimalDataRequest.FragmentListener, GetDataRequest.FragmentListener
{
    private static final String emptyText = "Pas d'animaux pour le moment";
    private static final String ARG_PARAM1 = "userId";
    private int userId;

    private ActivityListener                mListener;

    private AbsListView                     mListView;
    private AnimalsAdapter                  mAdapter;
    private AnimalDAO                       mAnimalDAO;
    private SendAnimalDataRequest           mSendDataAnimalsRequest = null;
    private GetDataRequest                  mGetDataRequest = null;

    public static AnimalListFragment newInstance(int param1)
    {
        AnimalListFragment fragment = new AnimalListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AnimalListFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            userId = getArguments().getInt(ARG_PARAM1);
        }
        mAnimalDAO = new AnimalDAO(getActivity());
        mAdapter = new AnimalsAdapter(getActivity());
        mAdapter.setAnimalList(mAnimalDAO.read(null));
        setHasOptionsMenu(true);
    }

    private AnimalDAO getmAnimalDAO()
    {
        return mAnimalDAO;
    }

    private AnimalsAdapter getAnimalsAdapter()
    {
        return mAdapter;
    }
    private Fragment        getFragment()
    {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        mListView = (AbsListView) view.findViewById(R.id.animal_list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            Integer         position;

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
            {
                AlertDialog.Builder builder = new
                AlertDialog.Builder(getActivity());

                position = pos;
                builder.setMessage("Voulez-vous supprimer le flux '" + getAnimalsAdapter().getItem(position).getName() + "' ?")
                        .setTitle("Confirmation")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                getmAnimalDAO().delete(getAnimalsAdapter().getItem(position));
                                getAnimalsAdapter().deleteAnimal(position);
                                mAdapter.delAnimal(mAdapter.getItem(position).getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String type = mAdapter.getItem(position).getType();
                String name = mAdapter.getItem(position).getName();
                String description = mAdapter.getItem(position).getDescription();
                String address = mAdapter.getItem(position).getAddress();
                String coordinates[] = mAdapter.getItem(position).getCoordinates().split("@");
                if (coordinates.length < 2)
                {
                    coordinates = new String[2];
                    coordinates[0] = "0";
                    coordinates[1] = "0";
                }

                String latitude = coordinates[0];
                String longitude = coordinates[1];

                AnimalDetailFragment animalDetailFragment = AnimalDetailFragment.newInstance(type, name, description, address, latitude, longitude);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, animalDetailFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        setEmptyText(emptyText);
        return view;
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
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater)
    {
        getActivity().getMenuInflater().inflate(R.menu.menu_my_pets, menu);
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu)
    {
        if (CurrentUser.getmInstance().isConnected() == false)
        {
            menu.getItem(1).setEnabled(false);
            menu.getItem(2).setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_add)
        {
            AddAnimalFragment addAnimalFragment = new AddAnimalFragment();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.fragment_container, addAnimalFragment);
            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        }
        else if (id == R.id.action_synchronize)
        {
            if (mSendDataAnimalsRequest == null)
            {
                mListener.showProgress(true);
                mSendDataAnimalsRequest = new SendAnimalDataRequest(getFragment(), getAnimalsAdapter().getAnimalList());
                mSendDataAnimalsRequest.execute((String) null);
            }
            return true;
        }
        else if (id == R.id.action_get)
        {
            if (mGetDataRequest == null)
            {
                mListener.showProgress(true);
                mGetDataRequest = new GetDataRequest(getFragment(), CurrentUser.getmInstance().getIdUser(), mAdapter);
                mGetDataRequest.execute((String) null);
            }
        }
        return true;
    }

    public void addAnimalInList(Animal animal)
    {
        mAdapter.addAnimal(animal);
    }

    public void editAnimalInList(Animal animal)
    {
        mAdapter.updateAnimal(animal);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

    }

    public void setEmptyText(CharSequence emptyText)
    {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView)
        {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onCancelAnimalDataRequest()
    {
        mSendDataAnimalsRequest = null;
        mListener.showProgress(false);
    }

    @Override
    public void onCancelGetDataRequest()
    {
        mGetDataRequest = null;
        mListener.showProgress(false);
    }

    @Override
    public void onConnectionError(String message)
    {
        mListener.displayMessage(message);
    }

    public interface ActivityListener
    {
        public void showProgress(boolean enable);
        public void displayMessage(String message);
    }

}
