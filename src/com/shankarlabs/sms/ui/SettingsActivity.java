package com.shankarlabs.sms.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.sms.R;

public class SettingsActivity extends SherlockPreferenceActivity
{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_prefs);
        
        // setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.settings, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_done:
                // Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
            	// Save if we need to save the Prefs
            	finish(); // Finish this activity so we fall back on the old one.
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
}
