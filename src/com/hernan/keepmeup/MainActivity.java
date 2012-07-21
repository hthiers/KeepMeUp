package com.hernan.keepmeup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;

/**
 * MAIN ACTIVITY
 * @author hernan
 *
 */
public class MainActivity extends Activity {
	
    public static final String EXTRA_MESSAGE = "com.hernan/tresgscheduler";
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    protected void onResume() {
    	super.onResume();
    }
    
    protected void onDestroy(View view){
    	super.onDestroy();
    	
    	stopDataService(view);
    }
    
    public void onServiceButtonChange(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();
        
        if(on) {
        	startDataService(view);
        } else {
        	stopDataService(view);
        }
    }
    
    public void startDataService(View view) {
    	Intent intent = new Intent(this, MobileDataService.class);
    	startService(intent);
    }
    
    public void stopDataService(View view) {
    	Intent intent = new Intent(this, MobileDataService.class);
    	stopService(intent);
    }
}