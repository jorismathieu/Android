package test.fr.petshop.adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import test.fr.petshop.R;
import test.fr.petshop.entities.Animal;
import test.fr.petshop.entities.CurrentUser;
import test.fr.petshop.fragments.AddAnimalFragment;
import test.fr.petshop.fragments.EditAnimalFragment;

public class AnimalsAdapter extends BaseAdapter
{
    private ArrayList<Animal>   animalList;
    private Context             context;
    private Activity            activity;
    private FragmentActivity    fragmentActivity;

    public AnimalsAdapter(Activity parentActivity)
    {
        animalList = new ArrayList<Animal>();
        context = parentActivity;
        activity = parentActivity;
        fragmentActivity = (FragmentActivity)activity;
    }

    @Override
    public int getCount()
    {
        return animalList.size();
    }

    @Override
    public Animal getItem(int position)
    {
        return animalList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public ArrayList<Animal>    getAnimalList()
    {
        return animalList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater li = LayoutInflater.from(context);

        View v = li.inflate(R.layout.animal_row, null);

        Button edit = (Button)v.findViewById(R.id.editAnimal);
        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animal animal = getItem(position);
                String coordinates[] = animal.getCoordinates().split("@");
                if (coordinates.length < 2)
                {
                    coordinates = new String[2];
                    coordinates[0] = "0";
                    coordinates[1] = "0";
                }
                EditAnimalFragment editAnimalFragment = EditAnimalFragment.newInstance(animal.getId(), animal.getType(), animal.getName(), animal.getDescription(), animal.getAddress(), coordinates[0], coordinates[1]);

                FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, editAnimalFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        Animal animal = getItem(position);

        TextView name = (TextView)v.findViewById(R.id.animalName);
        name.setFocusable(false);
        name.setEnabled(false);
        name.setText(animal.getName());
        name.setTextColor(Color.BLACK);

        TextView description = (TextView)v.findViewById(R.id.animalDescription);
        description.setFocusable(false);
        description.setEnabled(false);
        description.setText(animal.getDescription());
        description.setTextColor(Color.BLACK);

        Button payment = (Button)v.findViewById(R.id.buyAnimal);
        if (CurrentUser.getmInstance().isConnected() == false)
            payment.setEnabled(false);

        return v;
    }

    public void deleteAnimal(int position)
    {
        this.animalList.remove(position);
        notifyDataSetChanged();
    }

    public void setAnimalList(ArrayList<Animal> fluxList)
    {
        this.animalList = fluxList;
        notifyDataSetChanged();
    }

    public void addAnimal(Animal newFlux)
    {
        this.animalList.add(newFlux);
        notifyDataSetChanged();
    }

    public void delAnimal(int id)
    {
        Iterator<Animal> ite = animalList.iterator();
        while (ite.hasNext())
        {
            Animal flux = ite.next();
            if (flux.getId() == id)
            {
                ite.remove();
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void updateAnimal(Animal animal)
    {
        Iterator<Animal> ite = animalList.iterator();
        while (ite.hasNext())
        {
            Animal temp = ite.next();
            if (temp.getId() == animal.getId())
            {
                temp.setType(animal.getType());
                temp.setName(animal.getName());
                temp.setDescription(animal.getDescription());
                temp.setAdress(animal.getAddress());
                temp.setCoordinates(animal.getCoordinates());
                notifyDataSetChanged();
                return;
            }
        }
    }

}
