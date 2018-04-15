package com.example.manvijay.sports_app;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearSnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.support.v7.widget.SnapHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.android.volley.VolleyError;

import com.android.volley.Response;

import com.android.volley.Request;

import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import org.json.JSONObject;

import org.json.JSONException;
import junit.framework.Test;

import java.lang.reflect.Array;
import java.util.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Current_Matches.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Current_Matches#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Current_Matches extends Fragment {
   
    RecyclerView mrecycler1,mrecycler2,mrecycler3;      //Recycler View variables
    RecyclerView.LayoutManager mlayout1,mlayout2,mlayout3; 
    RecyclerView.Adapter madapter1,madapter2,madapter3;
    ArrayList<String> list11,list12,list13,image11,image12;
    ArrayList<String> list21,list22,list23,image21,image22;
    ArrayList<String> list31,list32,list33,image31,image32; // array list to store data to be displayed in current matches

       RequestQueue req1;
   // api URL's for calling scores from different leagues
    String urlepl = "http://api.football-data.org/v1/competitions/445/fixtures/";  // this for epl 
    String urlliga = "http://api.football-data.org/v1/competitions/455/fixtures/"; // this is for laliga
    String urlbundes = "http://api.football-data.org/v1/competitions/452/fixtures/";  // this is for bundesliga

    public static Current_Matches newInstance() {
        Current_Matches fragment = new Current_Matches();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        InputStream inputStream = getResources().openRawResource(R.raw.crest);
        CSVFile csvFile = new CSVFile(inputStream);
        final List<String[]> mylist = csvFile.read();

        Toast.makeText(getActivity().getApplicationContext(), "Loading Matches..",
                Toast.LENGTH_SHORT).show();
       
       //initializes rootView with fragment_current_mathces.xml layout
        View rootView = inflater.inflate(R.layout.fragment_current__matches, container, false);  
        mrecycler1 = rootView.findViewById(R.id.recycle1); 
        mrecycler2 = rootView.findViewById(R.id.recycle2);
        mrecycler3 = rootView.findViewById(R.id.recycle3);
        SnapHelper snapHelper1 = new LinearSnapHelper();
        SnapHelper snapHelper2 = new LinearSnapHelper();
        SnapHelper snapHelper3 = new LinearSnapHelper();
        snapHelper1.attachToRecyclerView(mrecycler1); // Configures the snap helper and attaches itself to the recycler view -- now items will snap to the center
        snapHelper2.attachToRecyclerView(mrecycler2);
        snapHelper3.attachToRecyclerView(mrecycler3);
       
        list11 = new ArrayList<>();
        list12 = new ArrayList<>();
        list13 = new ArrayList<>();
        image11 = new ArrayList<>();
        image12 = new ArrayList<>();

        list21 = new ArrayList<>();
        list22 = new ArrayList<>();
        list23 = new ArrayList<>();
        image21 = new ArrayList<>();
        image22 = new ArrayList<>();

        list31 = new ArrayList<>();
        list32 = new ArrayList<>();
        list33 = new ArrayList<>();
        image31 = new ArrayList<>();
        image32 = new ArrayList<>();

        req1 = Volley.newRequestQueue(getActivity());

        mrecycler1.setHasFixedSize(true);
        mrecycler2.setHasFixedSize(true);
        mrecycler3.setHasFixedSize(true);
        mlayout1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mlayout2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mlayout3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
       
         // api call for epl mathces
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET,  urlepl, null, new Response.Listener<JSONObject>(){

            @Override

            public void onResponse(JSONObject response) {

                // Check the length of our response (to see if we getting fixtures)

                if (response.length() > 0) {

                    // We did receive fixtures so let's loop through them all.
                    JSONArray jsonarr = null;
                    Integer count = null;
                    try {
                        jsonarr = response.getJSONArray("fixtures");
                        count = response.getInt("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Date curr = Calendar.getInstance().getTime();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        curr = mdformat.parse(mdformat.format(curr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(curr);
                    c.add(Calendar.DATE, 9);
                    Date currfut = c.getTime();
                    c.add(Calendar.DATE, -16);
                    curr = c.getTime();

                    for (int i = 0; i <= count; i++) {

                        try {

                            // For each new fixture add the value to the our layout
                            JSONObject temp = jsonarr.getJSONObject(i);
                            String tempdate = temp.get("date").toString();
                            tempdate = tempdate.substring(0,10);
                            Date temp2 = mdformat.parse(tempdate);

                            if((temp.get("status").toString().equals("IN_PLAY")) && temp2.after(curr) &&  temp2.before(currfut) ) {
                                JSONObject result = temp.getJSONObject("result");
                                list11.add(temp.get("homeTeamName").toString() + "\n" + result.get("goalsHomeTeam").toString());
                                list12.add(temp.get("awayTeamName").toString() + "\n" + result.get("goalsAwayTeam").toString());
                                list13.add("● LIVE");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(temp.get("homeTeamName").toString()))
                                        image11.add(tem[1].toString()+","+tem[2].toString());
                                    if(tem[0].equals(temp.get("awayTeamName").toString()))
                                        image12.add(tem[1]+","+tem[2]);

                                }
                            }


                        } catch (JSONException e) {

                            // If there is an error then output this to the logs.

                            Log.e("Volley", "Invalid JSON Object.");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i <= count; i++) {

                        try {

                            // For each new fixture add the value to the our layout
                            JSONObject temp = jsonarr.getJSONObject(i);
                            String tempdate = temp.get("date").toString();
                            tempdate = tempdate.substring(0,10);
                            Date temp2 = mdformat.parse(tempdate);

                            if( (temp.get("status").toString().equals("FINISHED")) && temp2.after(curr) &&  temp2.before(currfut) ) {
                                JSONObject result = temp.getJSONObject("result");
                                //adding values to list1 for display
                                list11.add(temp.get("homeTeamName").toString() + "\n" + result.get("goalsHomeTeam").toString());
                                list12.add(temp.get("awayTeamName").toString() + "\n" + result.get("goalsAwayTeam").toString());
                                list13.add( temp.get("date").toString().substring(0,10) );
                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(temp.get("homeTeamName").toString()))
                                        image11.add(tem[1].toString()+ ","+ tem[2].toString());

                                    if(tem[0].equals(temp.get("awayTeamName").toString()))
                                        image12.add(tem[1]+","+tem[2]);

                                }

                            }

                            if( (temp.get("status").toString().equals("SCHEDULED") || temp.get("status").toString().equals("TIMED")) && temp2.after(curr) &&  temp2.before(currfut) ) {

                                list11.add(temp.get("homeTeamName").toString());
                                list12.add(temp.get("awayTeamName").toString());
                                list13.add(temp.get("date").toString().substring(0,10));

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(temp.get("homeTeamName").toString()))
                                        image11.add(tem[1].toString()+","+tem[2].toString());
                                    if(tem[0].equals(temp.get("awayTeamName").toString()))
                                        image12.add(tem[1]+","+tem[2]);

                                }


                            }

                        } catch (JSONException e) {

                            // If there is an error then output this to the logs.

                            Log.e("Volley", "Invalid JSON Object.");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    // call the MainAdapter class and the pass the array list to handle displaying
                    madapter1 = new MainAdapter(list11,list12,list13,image11,image12);
                    mrecycler1.setLayoutManager(mlayout1);
                    mrecycler1.setAdapter(madapter1);

                } else {

                    // We did not receive any fixtures

                    //list1.add("Too many requests, try after some time");
                    Toast.makeText(getActivity().getApplicationContext(), "Offline Turn On Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        },

                new Response.ErrorListener() {

                    @Override

                    public void onErrorResponse(VolleyError error) {

                        // If there a HTTP error then display a pop-up error message.

                        //list1.add("Too many requests, try after some time");
                        Toast.makeText(getActivity().getApplicationContext(), "Too many Request or Offline",
                                Toast.LENGTH_SHORT).show();
                        Log.e("Volley", error.toString());

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Auth-Token", " c1719e0814b54b13a2e1725eb778ed13");
                return params;
            }
        };
         //api call for laliga matches
        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest (Request.Method.GET,  urlliga, null, new Response.Listener<JSONObject>(){

            @Override

            public void onResponse(JSONObject response) {

                

                if (response.length() > 0) {

                    
                    JSONArray jsonarr = null;
                    Integer count = null;
                    try {
                        jsonarr = response.getJSONArray("fixtures");
                        count = response.getInt("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Date curr = Calendar.getInstance().getTime();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        curr = mdformat.parse(mdformat.format(curr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(curr);
                    c.add(Calendar.DATE, 9);
                    Date currfut = c.getTime();
                    c.add(Calendar.DATE, -16);
                    curr = c.getTime();

                    for (int i = 0; i <= count; i++) {

                        try {

                            
                            JSONObject temp = jsonarr.getJSONObject(i);
                            String tempdate = temp.get("date").toString();
                            tempdate = tempdate.substring(0,10);
                            Date temp2 = mdformat.parse(tempdate);

                            if( (temp.get("status").toString().equals("IN_PLAY")) && temp2.after(curr) &&  temp2.before(currfut) ) {
                                JSONObject result = temp.getJSONObject("result");

                                list21.add(temp.get("homeTeamName").toString() + "\n" + result.get("goalsHomeTeam").toString());
                                list22.add(temp.get("awayTeamName").toString() + "\n" + result.get("goalsAwayTeam").toString());
                                list23.add("● LIVE");

                                String home = temp.get("homeTeamName").toString();
                                home = home.replaceAll("é","e");
                                home = home.replaceAll("á","a");
                                home = home.replaceAll("ú","u");

                                String away = temp.get("awayTeamName").toString();
                                away = away.replaceAll("é","e");
                                away = away.replaceAll("á","a");
                                away = away.replaceAll("ú","u");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(home))
                                        image21.add(tem[1].toString()+","+tem[2].toString());
                                    if(check.equals(away))
                                        image22.add(tem[1]+","+tem[2]);
                                }

                            }

                        } catch (JSONException e) {

                            // If there is an error then output this to the logs.

                            Log.e("Volley", "Invalid JSON Object.");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i <= count; i++) {

                        try {


                            JSONObject temp = jsonarr.getJSONObject(i);
                            String tempdate = temp.get("date").toString();
                            tempdate = tempdate.substring(0,10);
                            Date temp2 = mdformat.parse(tempdate);


                            if( (temp.get("status").toString().equals("FINISHED")) && temp2.after(curr) &&  temp2.before(currfut) ) {
                                JSONObject result = temp.getJSONObject("result");

                                list21.add(temp.get("homeTeamName").toString() + "\n" + result.get("goalsHomeTeam").toString());
                                list22.add(temp.get("awayTeamName").toString() + "\n" + result.get("goalsAwayTeam").toString());
                                list23.add(temp.get("date").toString().substring(0,10));

                                String home = temp.get("homeTeamName").toString();
                                home = home.replaceAll("é","e");
                                home = home.replaceAll("á","a");
                                home = home.replaceAll("ú","u");

                                String away = temp.get("awayTeamName").toString();
                                away = away.replaceAll("é","e");
                                away = away.replaceAll("á","a");
                                away = away.replaceAll("ú","u");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(home))
                                        image21.add(tem[1].toString()+","+tem[2].toString());
                                    if(check.equals(away))
                                        image22.add(tem[1]+","+tem[2]);
                                }

                            }

                            if( (temp.get("status").toString().equals("SCHEDULED") || temp.get("status").toString().equals("TIMED")) && temp2.after(curr) &&  temp2.before(currfut) ) {

                                list21.add(temp.get("homeTeamName").toString());
                                list22.add(temp.get("awayTeamName").toString());
                                list23.add(temp.get("date").toString().substring(0,10));

                                String home1 = temp.get("homeTeamName").toString();
                                home1 = home1.replaceAll("é","e");
                                home1 = home1.replaceAll("á","a");
                                home1 = home1.replaceAll("ú","u");

                                String away1 = temp.get("awayTeamName").toString();
                                away1 = away1.replaceAll("é","e");
                                away1 = away1.replaceAll("á","a");
                                away1 = away1.replaceAll("ú","u");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(home1))
                                        image21.add(tem[1].toString()+","+tem[2].toString());
                                    if(check.equals(away1))
                                        image22.add(tem[1]+","+tem[2]);

                                }

                            }

                        } catch (JSONException e) {

                            // If there is an error then output this to the logs.

                            Log.e("Volley", "Invalid JSON Object.");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    // Call the MainAdapter class to pass the list2 array to display
                    madapter2 = new MainAdapter(list21,list22,list23,image21,image22);
                    mrecycler2.setLayoutManager(mlayout2);
                    mrecycler2.setAdapter(madapter2);

                } else {

                    
                    //list2.add("Too many requests, try after some time");
                    Toast.makeText(getActivity().getApplicationContext(), "Offline Turn On Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        },

                new Response.ErrorListener() {

                    @Override

                    public void onErrorResponse(VolleyError error) {

                        
                       // list2.add("Too many requests, try after some time");
                        Toast.makeText(getActivity().getApplicationContext(), "Too many Request or Offline",
                                Toast.LENGTH_SHORT).show();

                        Log.e("Volley", error.toString());

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Auth-Token", " c1719e0814b54b13a2e1725eb778ed13");
                return params;
            }
        };
            //api call for bundseliga matches
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest (Request.Method.GET,  urlbundes, null, new Response.Listener<JSONObject>(){

            @Override

            public void onResponse(JSONObject response) {

                

                if (response.length() > 0) {

                    
                    JSONArray jsonarr = null;
                    Integer count = null;
                    try {
                        jsonarr = response.getJSONArray("fixtures");
                        count = response.getInt("count");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Date curr = Calendar.getInstance().getTime();
                    SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        curr = mdformat.parse(mdformat.format(curr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar c = Calendar.getInstance();
                    c.setTime(curr);
                    c.add(Calendar.DATE, 9);
                    Date currfut = c.getTime();
                    c.add(Calendar.DATE, -16);
                    curr = c.getTime();


                    for (int i = 0; i <= count; i++) {

                        try {


                            JSONObject temp = jsonarr.getJSONObject(i);
                            String tempdate = temp.get("date").toString();
                            tempdate = tempdate.substring(0,10);
                            Date temp2 = mdformat.parse(tempdate);

                            if( (temp.get("status").toString().equals("IN_PLAY")) && temp2.after(curr) &&  temp2.before(currfut) ) {
                                JSONObject result = temp.getJSONObject("result");

                                list31.add(temp.get("homeTeamName").toString() + "\n" + result.get("goalsHomeTeam").toString());
                                list32.add(temp.get("awayTeamName").toString() + "\n" + result.get("goalsAwayTeam").toString());
                                list33.add("● LIVE");

                                String home = temp.get("homeTeamName").toString();
                                home = home.replaceAll("ü","u");
                                home = home.replaceAll("ö","o");

                                String away = temp.get("awayTeamName").toString();
                                away = away.replaceAll("ü","u");
                                away = away.replaceAll("ö","o");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(home))
                                        image31.add(tem[1].toString()+","+tem[2].toString());
                                    if(check.equals(away))
                                        image32.add(tem[1]+","+tem[2]);

                                }

                            }

                        } catch (JSONException e) {

                            // If there is an error then output this to the logs.

                            Log.e("Volley", "Invalid JSON Object.");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i <= count; i++) {

                        try {


                            JSONObject temp = jsonarr.getJSONObject(i);
                            String tempdate = temp.get("date").toString();
                            tempdate = tempdate.substring(0,10);
                            Date temp2 = mdformat.parse(tempdate);


                            if( (temp.get("status").toString().equals("FINISHED")) && temp2.after(curr) &&  temp2.before(currfut) ) {
                                JSONObject result = temp.getJSONObject("result");

                                list31.add(temp.get("homeTeamName").toString() + "\n" + result.get("goalsHomeTeam").toString());
                                list32.add(temp.get("awayTeamName").toString() + "\n" + result.get("goalsAwayTeam").toString());
                                list33.add(temp.get("date").toString().substring(0,10));

                                String home = temp.get("homeTeamName").toString();
                                home = home.replaceAll("ü","u");
                                home = home.replaceAll("ö","o");

                                String away = temp.get("awayTeamName").toString();
                                away = away.replaceAll("ü","u");
                                away = away.replaceAll("ö","o");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(home))
                                        image31.add(tem[1].toString()+","+tem[2].toString());
                                    if(check.equals(away))
                                        image32.add(tem[1]+","+tem[2]);

                                }


                            }

                            if( (temp.get("status").toString().equals("SCHEDULED") || temp.get("status").toString().equals("TIMED")) && temp2.after(curr) &&  temp2.before(currfut) ) {

                                list31.add(temp.get("homeTeamName").toString());
                                list32.add(temp.get("awayTeamName").toString());
                                list33.add(temp.get("date").toString().substring(0,10));

                                String home1 = temp.get("homeTeamName").toString();
                                home1 = home1.replaceAll("ü","u");
                                home1 = home1.replaceAll("ö","o");

                                String away1 = temp.get("awayTeamName").toString();
                                away1 = away1.replaceAll("ü","u");
                                away1 = away1.replaceAll("ö","o");

                                for (int k =0;k<mylist.size();++k)
                                {
                                    String[] tem = mylist.get(k);
                                    String check = tem[0].toString();
                                    if(check.equals(home1))
                                        image31.add(tem[1].toString()+","+tem[2].toString());
                                    if(check.equals(away1))
                                        image32.add(tem[1]+","+tem[2]);

                                }

                            }

                        } catch (JSONException e) {

                            // If there is an error then output this to the logs.

                            Log.e("Volley", "Invalid JSON Object.");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    // call the MainAdapter to pass arraylist list3 to display the matches
                    madapter3 = new MainAdapter(list31,list32,list33,image31,image32);
                    mrecycler3.setLayoutManager(mlayout3);
                    mrecycler3.setAdapter(madapter3);

                } else {

                    
                    //list3.add("Too many requests, try after some time");
                    Toast.makeText(getActivity().getApplicationContext(), "Offline Turn On Connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        },

                new Response.ErrorListener() {

                    @Override

                    public void onErrorResponse(VolleyError error) {

                        
                       // list3.add("Too many requests, try after some time");
                        Toast.makeText(getActivity().getApplicationContext(), "Too many Request or Offline",
                                Toast.LENGTH_SHORT).show();

                        Log.e("Volley", error.toString());

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Auth-Token", " c1719e0814b54b13a2e1725eb778ed13");  // our api key for making that api call
                return params;
            }
        };
         // adding all the api calls to HTTP request queue
        req1.add(jsonObjectRequest);
        req1.add(jsonObjectRequest1);
        req1.add(jsonObjectRequest2);
        return rootView;
    }
;
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
