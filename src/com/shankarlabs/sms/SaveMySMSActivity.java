package com.shankarlabs.sms;

import com.example.android.actionbarcompat.ActionBarFragmentActivity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SaveMySMSActivity extends ActionBarFragmentActivity
{
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Add the two tabs on load
        ActionBar actionBar = getActionBar();
        
        final String settingsTabTag = "settingsTab";
        final String settingsTabTitle = "Settings";
        final int settingsTabId = R.layout.settings_fragment;
        actionBar.addTab(actionBar.newTab()
                .setText(settingsTabTitle) // The Tab Title
                .setTabListener(new TabListener(new TabFragment(settingsTabId, settingsTabTag))));
        
        final String viewSMSTabTag = "ViewSMSTab";
        final String viewSMSTabTitle = "View SMS";
        final int viewSMSTabId = R.layout.viewsms_fragment;
        actionBar.addTab(actionBar.newTab()
                .setText(viewSMSTabTitle) // The Tab Title
                .setTabListener(new TabListener(new TabFragment(viewSMSTabId, viewSMSTabTag))));
        
        // Time to enable the tabs
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE); //, ActionBar.DISPLAY_SHOW_TITLE); // Don't disable anything
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
    
	
	public void onAddTab(View v)
	{
        final ActionBar bar = getActionBar();
        final int tabCount = bar.getTabCount();
        final String text = "Tab " + tabCount;
        bar.addTab(bar.newTab()
                .setText(text)
                .setTabListener(new TabListener(new TabFragment(R.layout.settings_fragment, "Settings"))));
    }

    public void onRemoveTab(View v) {
        final ActionBar bar = getActionBar();
        bar.removeTabAt(bar.getTabCount() - 1);
    }
   
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Tapped home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_refresh:
                Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT).show();
                getActionBarHelper().setRefreshActionItemState(true);
                getWindow().getDecorView().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                getActionBarHelper().setRefreshActionItemState(false);
                            }
                        }, 1000);
                break;

            case R.id.menu_search:
                Toast.makeText(this, "Tapped search", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_share:
                Toast.makeText(this, "Tapped share", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * A TabListener receives event callbacks from the action bar as tabs
     * are deselected, selected, and reselected. A FragmentTransaction
     * is provided to each of these callbacks; if any operations are added
     * to it, it will be committed at the end of the full tab switch operation.
     * This lets tab switches be atomic without the app needing to track
     * the interactions between different tabs.
     *
     * NOTE: This is a very simple implementation that does not retain
     * fragment state of the non-visible tabs across activity instances.
     * Look at the FragmentTabs example for how to do a more complete
     * implementation.
     */
    private class TabListener implements ActionBar.TabListener {
        private TabFragment mTabFragment;

        public TabListener(TabFragment fragment) {
        	mTabFragment = fragment;
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.add(R.id.fragment_content, mTabFragment, mTabFragment.getTabTag());
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(mTabFragment);
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(SaveMySMSActivity.this, "Reselected!", Toast.LENGTH_SHORT).show();
        }

    }
    
    private class TabFragment extends Fragment
    {
    	private int mTabId;
        private String mTabTag;

        public TabFragment(int tabId, String tag) {
        	mTabId = tabId;
        	mTabTag = tag;
        }

        public String getTabTag() {
            return mTabTag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View fragmentView = inflater.inflate(mTabId, container, false);
            return fragmentView;
        }

    }
}