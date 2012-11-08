package com.bluekulu.plugin.wifi;



/* Copyright (c) 2011 - Andago
 * 
 * author: Daniel Tiz—n, Idoia Olalde, I–igo Gonz‡lez
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import android.net.ConnectivityManager;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.api.PluginResult.Status;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiPlugin extends Plugin {

	public static final String ACTION_SCAN="Scan";
	private static final String LOG_TAG = "WifiPlugin";
	WifiManager mainWifi;
	
	List<ScanResult> wifiList;
	StringBuilder sb = new StringBuilder();
	boolean scanning = false;
	BroadcastReceiver receiver;
	
	private String wifiCallbackId = null;
	
	private Context context;
	JSONObject aps = new JSONObject();
	String devicesFound = "";
	/* (non-Javadoc)
	 * @see com.phonegap.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	@Override
	public PluginResult execute(String action, JSONArray arg1, String callbackId) {
		Log.d("WifiPlugin", "Plugin Called");
		PluginResult result = null;
		context = this.ctx;
		boolean waiting = false;
		IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		
		this.wifiCallbackId = callbackId;

	


		if (ACTION_SCAN.equals(action)) {
			try {
				
				Log.d("WifiPlugin", "We're in "+ACTION_SCAN);
				scanning=true;
		        Log.i("WifiPlugin","Scanning ...");        
				mainWifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				sb = new StringBuilder();
				mainWifi.startScan();
				
	            if (this.receiver == null) {
	                this.receiver = new BroadcastReceiver() {
	                    @Override
	                    public void onReceive(Context c, Intent intent){
							// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs

							wifiList = mainWifi.getScanResults();
							//aps = new JSONObject();
							int count=0;
							devicesFound="[";
							for(int j = 0; j < wifiList.size(); j++){
//								sb.append(new Integer(j+1).toString() + ".");
								//								sb.append((wifiList.get(j)).toString());
								//								Log.i("WifiPlugin",(wifiList.get(j)).toString());     
								//								sb.append("\n");
								//								
								ScanResult sr = wifiList.get(j);
								Log.i("WifiPlugin",wifiList.get(j).BSSID);
								Log.i("WifiPlugin",Integer.toString(wifiList.get(j).level));
								JSONObject obj = new JSONObject();


								devicesFound = devicesFound + " { \"ssid\" : \"" + wifiList.get(j).SSID + "\" ," +
										"\"address\" : \"" + wifiList.get(j).BSSID + "\" ," +
										"\"level\" : \"" + Integer.toString(wifiList.get(j).level) + "\" }";
								if (count<wifiList.size()-1) devicesFound = devicesFound + ",";

								count++;


								
								

							}
							devicesFound= devicesFound + "] ";	
							Log.i("WifiPlugin","JSON Object, " + devicesFound);
							updateBatteryInfo(intent);

						}
	                };
	                ctx.registerReceiver(this.receiver, intentFilter);
	            }        
		        
	            
/*				context.registerReceiver(new BroadcastReceiver(){
					@Override public void onReceive(Context c, Intent i){
						// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs
						mainWifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
						sb = new StringBuilder();
						wifiList = mainWifi.getScanResults();
						for(int j = 0; j < wifiList.size(); j++){
							sb.append(new Integer(j+1).toString() + ".");
							sb.append((wifiList.get(j)).toString());
							Log.i("WifiPlugin",(wifiList.get(j)).toString());     
							sb.append("\n");
						}
					} } ,filter);
	            */
	            
/*				context.registerReceiver(new BroadcastReceiver(){
					@Override public void onReceive(Context c, Intent i){
						// Code to execute when SCAN_RESULTS_AVAILABLE_ACTION event occurs
						mainWifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
						sb = new StringBuilder();
						wifiList = mainWifi.getScanResults();
						for(int j = 0; j < wifiList.size(); j++){
							sb.append(new Integer(j+1).toString() + ".");
							sb.append((wifiList.get(j)).toString());
							Log.i("WifiPlugin",(wifiList.get(j)).toString());     
							sb.append("\n");
						}
					} } ,filter);*/

				
				
				Log.i("WifiPlugin - "+ACTION_SCAN, "Returning: "+ sb.toString());
				  PluginResult pluginResult = new PluginResult(PluginResult.Status.NO_RESULT);
		            pluginResult.setKeepCallback(true);
		            return pluginResult;
			} catch (Exception Ex) {
				Log.i("WifiPlugin - "+ACTION_SCAN, "Got Exception "+ Ex.getMessage());
				result = new PluginResult(Status.ERROR);
			}
			
	
			
		
		
		} else {
			result = new PluginResult(Status.INVALID_ACTION);
			Log.d("WifiPlugin", "Invalid action : "+action+" passed");
		}
		return result;
	}
	

    /**
* Creates a JSONObject with the current battery information
*
* @param batteryIntent the current battery information
* @return a JSONObject containing the battery status information
*/
    private JSONObject getBatteryInfo(Intent batteryIntent) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("level","aaaaa" );
            obj.put("isPlugged","bbbb" );
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return obj;
    }
	
    /**
* Updates the JavaScript side whenever the battery changes
*
* @param batteryIntent the current battery information
* @return
*/
    private void updateBatteryInfo(Intent wifiIntent) {
        sendUpdate(this.aps , true);
    }
    
    /**
* Create a new plugin result and send it back to JavaScript
*
* @param connection the network info to set as navigator.connection
*/
    private void sendUpdate(JSONObject info, boolean keepCallback) {
     if (this.wifiCallbackId != null) {
    	 Log.i(LOG_TAG,info.toString());
     PluginResult result = new PluginResult(PluginResult.Status.OK, devicesFound);
     result.setKeepCallback(keepCallback);
     
     this.success(result, this.wifiCallbackId);
     }
    }
    
    
    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
    	Log.i("WifiPlugin","onDestroy "+this.getClass());
    	context.unregisterReceiver(receiver);
    	super.onDestroy();
	}
	

}
