package test.fr.petshop.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import test.fr.petshop.R;
import test.fr.petshop.entities.Animal;
import test.fr.petshop.entities.CurrentUser;
import test.fr.petshop.fragments.AddAnimalFragment;
import test.fr.petshop.fragments.AnimalDetailFragment;
import test.fr.petshop.fragments.AnimalListFragment;
import test.fr.petshop.fragments.EditAnimalFragment;
import test.fr.petshop.utils.DebugUtils;

public class AnimalsActivity extends ActionBarActivity implements AnimalListFragment.ActivityListener,
        AddAnimalFragment.ActivityListener, EditAnimalFragment.ActivityListener, AnimalDetailFragment.ActivityListener
{

    private View mProgressView;
    private View mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pets);

        if (findViewById(R.id.fragment_container) != null)
        {
            if (savedInstanceState != null) {
                return;
            }

            AnimalListFragment animalListFragment = AnimalListFragment.newInstance(CurrentUser.getmInstance().getIdUser());

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, animalListFragment, "AnimalListFragment").commit();
        }
        mProgressView = findViewById(R.id.animal_progress);
        mFragmentContainer = (View)findViewById(R.id.fragment_container);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void displayProgress(final boolean show)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
        {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mFragmentContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            mFragmentContainer.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFragmentContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter()
            {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
        else
        {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFragmentContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayMessage(String message)
    {
        DebugUtils.logToast(AnimalsActivity.this, message);
    }

    @Override
    public void addAnimalInList(Animal animal)
    {
        AnimalListFragment animalListFragment = (AnimalListFragment)getSupportFragmentManager().findFragmentByTag("AnimalListFragment");
        animalListFragment.addAnimalInList(animal);
    }

    @Override
    public void editAnimalInList(Animal animal)
    {
        AnimalListFragment animalListFragment = (AnimalListFragment)getSupportFragmentManager().findFragmentByTag("AnimalListFragment");
        animalListFragment.editAnimalInList(animal);
    }

    @Override
    public void showProgress(boolean enable)
    {
        displayProgress(enable);
    }
}
