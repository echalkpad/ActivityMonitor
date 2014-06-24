package com.smartapps.accel;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;


/**
 * 
 */
public class ScanFragment   extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewContainer = inflater.inflate(R.layout.rfscanner_fragment, container, false);
        //MainActivity activ = (MainActivity)getActivity();



        return viewContainer;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activ = (MainActivity)getActivity();
        activ.setTitle("RF Scanner");

    }
}
