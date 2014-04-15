package edu.rutgers.css.Rutgers;

import org.jdeferred.DoneCallback;
import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.androidquery.AQuery;

public class BusStops extends Fragment {

	private AQuery aq;
	private ListView mList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		aq = new AQuery(this.getActivity());
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_busstops, parent, false);
		
		mList = (ListView) v.findViewById(R.id.list);
		Nextbus.getStops("nb").then(new DoneCallback<JSONArray>() {
			
			@Override
			public void onDone(JSONArray data) {
				JSONArrayAdapter adapter = new JSONArrayAdapter(getActivity(), data, R.layout.title_row);
				
				mList.setAdapter(adapter);
			}
		});
		
		return v;
	}
	
}
