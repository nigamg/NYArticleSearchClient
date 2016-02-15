package com.gn.demo.nyarticlesearchclient.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.gn.demo.nyarticlesearchclient.R;
import com.gn.demo.nyarticlesearchclient.model.SearchFilter;
import com.gn.demo.nyarticlesearchclient.view.DatePickerFragment;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFilterFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static SearchFilter sF;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SearchFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SearchFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFilterFragment newInstance(String param1, SearchFilter searchFilter) {
        SearchFilterFragment fragment = new SearchFilterFragment();

        sF = searchFilter;
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_search_filter, container, false);

        final EditText beginDate = (EditText) v.findViewById(R.id.beginDate);
        Button saveFilter = (Button) v.findViewById(R.id.filterBtn);


        getDialog().setTitle("Refine Your Search");

        beginDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(getChildFragmentManager(), "datePicker");
            }
        });

        saveFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vv) {
                Log.i("DEBUG", " saving");

                EditText sort = (EditText) v.findViewById(R.id.sortOrder);

                CheckBox artsCk = (CheckBox) v.findViewById(R.id.artsCk);
                CheckBox sportCk = (CheckBox) v.findViewById(R.id.sportsCk);
                CheckBox fashionCk = (CheckBox) v.findViewById(R.id.fashionCk);

                if(beginDate.getText().toString() != "Select Date"){
                    String date = beginDate.getText().toString();
                    Log.i("DEBUG", "++++++before split, date++++++"+date);
                    if(date.indexOf('.') != -1){
                        String [] dateArr = date.split("\\.");

                        String d = "";
                        for(int i = dateArr.length-1 ; i>= 0 ;i--){
                            Log.i("DEBUG", "----"+dateArr[i]);
                            d = d + dateArr[i];
                        }
                        Log.i("DEBUG", "++++++date++++++"+d);
                        sF.setBeginDate(d);
                    }
                }

                if(sort.getText().toString() != "Oldest"){
                    sF.setSortOrder("oldest");
                   // Log.i("DEBUG", "++++++++++++"+sort.getText().toString());
                }

                if(artsCk.isChecked()){
                    sF.setDeskValues(artsCk.getText().toString());
                }


                if(sportCk.isChecked()){
                    sF.setDeskValues(sF.getDeskValues()+"," + sportCk.getText().toString());
                }

                if(fashionCk.isChecked()){
                    sF.setDeskValues(sF.getDeskValues()+"," + fashionCk.getText().toString());
                }

                Toast.makeText(getActivity(), "Search Preference Saved!", Toast.LENGTH_SHORT).show();

                getDialog().dismiss();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
