package edu.rutgers.css.Rutgers;

import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;

// Convenience class for making requests
public class Request {
	
	private static final String API_BASE = "http://sauron.rutgers.edu/~rfranknj/newmobile/";
	private static AQuery aq;
	private static boolean mSetupDone = false;
	
	private static void setup () {
		if (!mSetupDone) {
			aq = new AQuery(MyApplication.getAppContext());
			
			mSetupDone = true;
		}
	}
	
	// Makes a call against the api. Expects a JSON object
	public static Promise<JSONObject, AjaxStatus, Double> api (String resource) {
		return json(API_BASE + resource);
	}
	
	// gets arbitrary json
	public static Promise<JSONObject, AjaxStatus, Double> json (String resource) {
		setup();
		final DeferredObject<JSONObject, AjaxStatus, Double> deferred = new DeferredObject<JSONObject, AjaxStatus, Double>();
		
		aq.ajax(resource, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {
				if (json != null) deferred.resolve(json);
				else deferred.reject(status);
			}
			
		});
		
		
		return deferred.promise();
	}
	
	// gets arbitrary xml
	public static Promise<XmlDom, AjaxStatus, Double> xml (String resource) {
		setup();
		final DeferredObject<XmlDom, AjaxStatus, Double> deferred = new DeferredObject<XmlDom, AjaxStatus, Double>();
		
		aq.ajax(resource, XmlDom.class, new AjaxCallback<XmlDom>() {

			@Override
			public void callback(String url, XmlDom xml, AjaxStatus status) {
				if (xml != null) deferred.resolve(xml);
				else deferred.reject(status);
			}
			
		});
		
		
		return deferred.promise();
	}
}
