package test.fr.petshop.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import test.fr.petshop.R;


public class AnimalDetailFragment extends Fragment
{
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "name";
    private static final String ARG_PARAM3 = "description";
    private static final String ARG_PARAM4 = "address";
    private static final String ARG_PARAM5 = "latitude";
    private static final String ARG_PARAM6 = "longitude";

    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private String mParam6;

    private ActivityListener mListener;
    private MapView mapView;
    private GoogleMap map;

    public static AnimalDetailFragment newInstance(String param1, String param2, String param3, String param4, String param5, String param6)
    {
        AnimalDetailFragment fragment = new AnimalDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM5, param5);
        args.putString(ARG_PARAM6, param6);
        fragment.setArguments(args);
        return fragment;
    }

    public AnimalDetailFragment()
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
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_animal_detail, container, false);

        TextView type = (TextView)v.findViewById(R.id.type);
        TextView name = (TextView)v.findViewById(R.id.name);
        TextView description = (TextView)v.findViewById(R.id.description);
        TextView address = (TextView)v.findViewById(R.id.address);

        type.setText(mParam1);
        name.setText(mParam2);
        description.setText(mParam3);
        address.setText(mParam4);


        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls

        MapsInitializer.initialize(this.getActivity());


        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(Float.valueOf(mParam5), Float.valueOf(mParam6)), 10);
        map.animateCamera(cameraUpdate);

        return v;
    }

    @Override
    public void onResume()
    {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
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
    }

}
