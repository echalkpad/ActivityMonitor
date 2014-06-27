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
 * Results Fragment
 * Third page of the app
 * Shows the Confusion Matrix (the results based on the training and testing data)
 */
public class ResultsFragment   extends ListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewContainer = inflater.inflate(R.layout.results_fragment, container, false);
        //MainActivity activ = (MainActivity)getActivity();

        updateInformation();

        return viewContainer;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        MainActivity activ = (MainActivity)getActivity();
        activ.setTitle("Results");

    }

    
    public void updateInformation(){
        DataItemAdapter adapter = new DataItemAdapter(getActivity());
        MainActivity activ = ((MainActivity) getActivity());

        GroupData dataIdle = activ.getTestDataIdle();
        GroupData dataWalk = activ.getTestDataWalk();
        GroupData dataRun = activ.getTestDataRun();
        if(dataIdle.getData().size()>0)
        {
            adapter.add( new DataItem("Idle", dataIdle.getnrofIdle(),dataIdle.getnrofWalks(), dataIdle.getnrofRuns(), dataIdle.getData().size()));
        }

        if(dataWalk.getData().size()>0)
        {
            adapter.add( new DataItem("Walk", dataWalk.getnrofIdle(),dataWalk.getnrofWalks(), dataWalk.getnrofRuns(), dataWalk.getData().size()));
        }
        if(dataRun.getData().size()>0){
            adapter.add( new DataItem("Run", dataRun.getnrofIdle(),dataRun.getnrofWalks(), dataRun.getnrofRuns(), dataRun.getData().size()));
        }

        setListAdapter(adapter);
    }




    /* Class DataItem Adapter
	 * Here we say to the program what the list will contain
	 */
    public class DataItemAdapter extends ArrayAdapter<DataItem> {

        public DataItemAdapter(Context context) {
            super(context, 0);
        }

        public View getView(int position, View convertView, ViewGroup parnt) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.results_row, null);
            }

            TextView dataName = (TextView) convertView.findViewById(R.id.name);
            dataName.setText(getItem(position).dataName);
            //hourView.setTextColor(getResources().getColor(R.color.black));

            int totalNrData = getItem(position).totalNrOfData;

            TextView idleValue = (TextView) convertView.findViewById(R.id.idle);
            int valueI = getItem(position).nrOfIdle;
            if(valueI % totalNrData == 0){
                idleValue.setText(Integer.toString(valueI / totalNrData));
            }else{
                idleValue.setText(Integer.toString(valueI) + "/" + Integer.toString(totalNrData));
            }

            TextView walkValue = (TextView) convertView.findViewById(R.id.walk);
            int valueW = getItem(position).nrOfWalks;
            if(valueW % totalNrData == 0){
                walkValue.setText(Integer.toString(valueW / totalNrData));
            }else{
                walkValue.setText(Integer.toString(valueW) + "/" + Integer.toString(totalNrData));
            }

            TextView runValue = (TextView) convertView.findViewById(R.id.run);
            int valueR = getItem(position).nrOfRuns;
            if(valueI % totalNrData == 0){
                runValue.setText(Integer.toString(valueR / totalNrData));
            }else{
                runValue.setText(Integer.toString(valueR) + "/" + Integer.toString(totalNrData));
            }

            return convertView;
        }



    }



    /* Class DataItem
     */
    private class DataItem {
        public String dataName;
        public int nrOfWalks;
        public int nrOfIdle;
        public int nrOfRuns;
        public int totalNrOfData;

        public DataItem(String dataName, int nrOfIdle,  int nrOfWalks, int nrOfRuns,  int totalNrOfData) {
            this.dataName = dataName;
            this.nrOfWalks = nrOfWalks;
            this.nrOfIdle = nrOfIdle;
            this.nrOfRuns = nrOfRuns;
            this.totalNrOfData = totalNrOfData;
        }
    }
}