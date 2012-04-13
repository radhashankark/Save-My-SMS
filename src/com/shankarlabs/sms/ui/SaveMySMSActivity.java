package com.shankarlabs.sms.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Window;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.shankarlabs.sms.R;
import com.shankarlabs.sms.R.id;
import com.shankarlabs.sms.R.layout;
import com.shankarlabs.sms.R.menu;
import com.shankarlabs.sms.core.AuthHandler;
import com.shankarlabs.sms.core.MsgHelper;
import com.shankarlabs.sms.services.AuthHandlerService;

public class SaveMySMSActivity extends SherlockFragmentActivity
{
	public static final String LOGTAG = "SaveMySMS";
	private MsgHelper msgHelper = new MsgHelper(); 
	private AuthHandler authHandler = new AuthHandler();
	
	public void onCreate(Bundle savedInstanceState)
    {
		/* Start the Service which will get the token and start back this Activity
		Context context = getApplicationContext();
		Intent newIntent = new Intent(context, AuthHandlerService.class);
        context.startService(newIntent);
        */
        
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set the progress spinner to false since it starts spinning on load
        setSupportProgressBarIndeterminateVisibility(false);
        
        //Add the two tabs on load
        ActionBar actionBar = getSupportActionBar();
        
        final String statusTabTag = "statusTab";
        final String statusTabTitle = "Status";
        actionBar.addTab(actionBar.newTab()
                .setText(statusTabTitle) // The Tab Title
                .setTabListener(new TabListener(new StatusFragment(), statusTabTag))); // TabFragment(statusTabId, statusTabTag))));
        
        /* We don't need this tab right now. We'll enable it later */
        final String msgThreadTabTag = "MsgThreadTab";
        final String msgThreadTabTitle = "Threads";
        actionBar.addTab(actionBar.newTab()
                .setText(msgThreadTabTitle) // The Tab Title
                .setTabListener(new TabListener(new MsgThreadFragment(), msgThreadTabTag))); // TabFragment(settingsTabId, settingsTabTag))));
        // */
        
        // Time to enable the tabs
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE); //, ActionBar.DISPLAY_SHOW_TITLE); // Don't disable anything
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Remove it later; This is for testing only
        // msgHelper.getMessages(getApplicationContext());
        // authHandler.getAllAccounts(getApplicationContext());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getSupportMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Log.d(LOGTAG, "Clicked on itemID " + item.getItemId());
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                // Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                setSupportProgressBarIndeterminateVisibility(true);
                msgHelper.getMessages(getApplicationContext());
                setSupportProgressBarIndeterminateVisibility(false);
                /* 
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                            	setSupportProgressBarIndeterminateVisibility(false);
                            }
                        }, 1000);
                */
                break;

            case R.id.menu_settings:
                // Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show();
            	// Start intent for Preferences
            	Intent prefsIntent = new Intent("com.shankarlabs.sms.Settings");
            	startActivity(prefsIntent);
                break;

            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class TabListener implements ActionBar.TabListener {
        private SherlockFragment mTabFragment;
        private String mTag;

        public TabListener(SherlockFragment fragment, String tag) {
        	mTabFragment = fragment;
        	mTag = tag;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	// There is an issue with compatibility library v4 that passes null FragmentTransaction. Handle that
        	if(ft == null)
        	{
        		Log.d(LOGTAG, "SMSActivity : onTabSelected : FragmentTransaction is null");
        		FragmentManager fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.add(R.id.fragment_content, mTabFragment, mTag); // mTabFragment.getTag());
                ft.commit();
        	}
        	else
        	{
        		// If not null, use the given ft
        		ft.add(R.id.fragment_content, mTabFragment, mTag); // mTabFragment.getTag());
        	}
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        	// There is an issue with compatibility library v4 that passes null FragmentTransaction. Handle that
        	if(ft == null)
        	{
        		Log.d(LOGTAG, "SMSActivity : onTabUnselected : FragmentTransaction is null");
        		FragmentManager fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.remove(mTabFragment);
                ft.commit();
        	}
        	else
        	{
        		// If not null, use the given ft
        		ft.remove(mTabFragment);
        	}
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        	// There is an issue with compatibility library v4 that passes null FragmentTransaction. Handle that
        	if(ft == null)
        	{
        		Log.d(LOGTAG, "SMSActivity : onTabReselected : FragmentTransaction is null");
        		FragmentManager fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
        	}
        	else
        	{
        		// If not null, use the given ft
        		Toast.makeText(SaveMySMSActivity.this, "Reselected!", Toast.LENGTH_SHORT).show();
        	}
        }

    }
}