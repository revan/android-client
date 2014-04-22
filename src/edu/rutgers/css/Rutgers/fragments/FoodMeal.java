package edu.rutgers.css.Rutgers.fragments;

import java.util.ArrayList;
import java.util.List;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidquery.callback.AjaxStatus;

import edu.rutgers.css.Rutgers.R;
import edu.rutgers.css.Rutgers.api.Dining;
import edu.rutgers.css.Rutgers.api.Request;

/* Dining JSON structure
location_name = "Brower Commons"
meals (array) ->
		meal_name = "Breakfast"
		meal_avail - boolean
		genres (array) ->
			genre_name = "Breakfast Entrees"
			items (array) ->
					"Oatmeal"
					"Pancake
 */

/**
 * Meal display fragment
 * Displays all food items available for a specific meal at a specific dining location.
 */
public class FoodMeal extends Fragment {

	private static final String TAG = "FoodMeal";
	private ListView mList;
	private JSONArray mData;
	private List<String> foodItems;
	private ArrayAdapter<String> foodItemAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		foodItems = new ArrayList<String>();
		foodItemAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, foodItems);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_food_meal, container, false);
		Bundle args = getArguments();
		mList = (ListView) v.findViewById(R.id.food_meal_list);
		
		getActivity().setTitle(args.getString("location"));
		
		Log.d(TAG, "Location: " + args.getString("location") + "; Meal: " + args.getString("meal"));
		setupList(args.getString("location"), args.getString("meal"));
		
		return v;
	}
	
	/**
	 * Grab data from dining API and fill up the list of menu items.
	 * @param location Dining hall to get items from
	 * @param meal Which meal is to be displayed
	 */
	private void setupList(final String location, final String meal) {
		
		mList.setAdapter(foodItemAdapter);
		
		if(location == null || meal == null) {
			Log.e(TAG, "null location/meal args to setupList");
			return;
		}
		
		Dining.getDiningLocation(location).done(new DoneCallback<JSONObject>() {

			@Override
			public void onDone(JSONObject dinLoc) {
				JSONArray mealGenres = Dining.getMealGenres(dinLoc, meal);			
				
				// Populate list
				if(mealGenres != null) {
					for(int i = 0; i < mealGenres.length(); i++) {
						try {
							JSONObject curGenre = mealGenres.getJSONObject(i);
							JSONArray mealItems = curGenre.getJSONArray("items");
							foodItemAdapter.add("CATEGORY - " + curGenre.getString("genre_name")); // Category title placeholder
							for(int j = 0; j < mealItems.length(); j++) {
								foodItemAdapter.add(mealItems.getString(j));
							}
						} catch (JSONException e) {
							Log.e(TAG, e.getMessage());
						}
					}
				}				
			}
			
		}).fail(new FailCallback<Exception>() {
		
			@Override
			public void onFail(Exception e) {
				Log.e(TAG, e.getMessage());
			}
			
		});
	
	}
	
}
