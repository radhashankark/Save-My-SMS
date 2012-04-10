package com.shankarlabs.sms.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.shankarlabs.sms.R;
import com.shankarlabs.sms.services.AuthHandlerService;

public class SettingsActivity extends SherlockPreferenceActivity
{
	private static final String LOGTAG = "SaveMySMS";
	private CheckBoxPreference smsGmailPref, smsDocsPref; // , mmsGmailPref, mmsDocsPref, mmsPicasaPref, logsGmailPref, logsDocsPref;
	private ListPreference scheduleIncomingPref, scheduleTimedPref;
	private final int ACCOUNTS_DIALOG = 12; // Random int
	private Account[] allAccounts;
	private int selectedUserIndex = 0;
	private String serviceName;
	
	@SuppressWarnings("deprecation") // The addPreferences and findPreference are deprecated.  
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGTAG, "SettingsActivity : onCreate : Loading prefs now");
        addPreferencesFromResource(R.xml.settings_prefs);
        
        // Get the Preference Items
        scheduleIncomingPref = (ListPreference) findPreference("scheduleIncomingPref");
        scheduleTimedPref = (ListPreference) findPreference("scheduleTimedPref");
        smsGmailPref = (CheckBoxPreference) findPreference("smsgmailbkpckbx");
        smsDocsPref = (CheckBoxPreference) findPreference("smsdocsbkpckbx");
        /* These are not used right now.
        mmsGmailPref = (CheckBoxPreference) findPreference("mmsgmailbkpckbx");
        mmsDocsPref = (CheckBoxPreference) findPreference("mmsdocsbkpckbx");
        mmsPicasaPref = (CheckBoxPreference) findPreference("mmspicasabkpckbx");
        logsGmailPref = (CheckBoxPreference) findPreference("logsgmailbkpckbx");
        logsDocsPref = (CheckBoxPreference) findPreference("logsdocsbkpckbx");
        */
        
        scheduleIncomingPref.setOnPreferenceChangeListener(scheduleIncCL);
        scheduleTimedPref.setOnPreferenceChangeListener(scheduleTimedCL);
        smsGmailPref.setOnPreferenceChangeListener(smsGmailCL);
        smsDocsPref.setOnPreferenceChangeListener(smsDocsCL);
        /* These are not used right now
        mmsGmailPref.setOnPreferenceChangeListener(mmsGmailCL);
        mmsDocsPref.setOnPreferenceChangeListener(mmsDocsCL);
        mmsPicasaPref.setOnPreferenceChangeListener(mmsPicasaCL);
        logsGmailPref.setOnPreferenceChangeListener(logsGmailCL);
        logsDocsPref.setOnPreferenceChangeListener(logsDocsCL);
        */
        
        Log.d(LOGTAG, "SettingsActivity : onCreate : Initialized all Listeners");
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
    
    /**
     * Gets all the Google Accounts on the Device and gets a token for one of them.
     * If no Account present, the user is shown the Add Accounts page.
     * If only one account is present, it uses that account.
     * If there are multiple, the user is asked to pick one from the Dialog.
     * @param	none
     * @return	none
     */
    private void getAccountAndToken()
    {
    	// Get Account manager
		AccountManager accountManager = AccountManager.get(getApplicationContext());
				
		// List all accounts
		allAccounts = accountManager.getAccountsByType("com.google");
		if(allAccounts.length == 0) // No accounts configured. Start the Add Accounts Activity
		{
			Log.d(LOGTAG, "SettingsActivity : getAccountAndToken : No Account found. Add an account. Exiting now.");
			startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
			finish(); // Since we have nothing else to do, end this activity
		}
		else if(allAccounts.length == 1) // There's just one account. Use it.
		{
			Account account = allAccounts[0]; // The only account available
			Log.d(LOGTAG, "SettingsActivity : getAccountAndToken : Using the only account existing, " + account.name);
			requestToken(account.name, serviceName);
			
			/* Start the Service which will get the token
			Context context = getApplicationContext();
			Intent newIntent = new Intent(context, AuthHandlerService.class);
			// Intent newIntent = new Intent(context, SaveMySMSActivity.class);
			newIntent.putExtra("com.shankarlabs.sms.accName", account.name);
			newIntent.putExtra("com.shankarlabs.sms.accType", account.type);
	        context.startService(newIntent);
	        */
		}
		else
		{
			// Show the Account Selection Dialog
			showDialog(ACCOUNTS_DIALOG);
		}
    }
    
	@Override
	protected Dialog onCreateDialog(int id)
	{
		  switch (id)
		  {
			  case ACCOUNTS_DIALOG: // The Dialog for Accounts
				  AlertDialog.Builder builder = new AlertDialog.Builder(this);
				  builder.setTitle("Select an account");
				  final int size = allAccounts.length;
				  String[] names = new String[size];
				  for (int i = 0; i < size; i++)
				  {
					  names[i] = allAccounts[i].name;
					  Log.d(LOGTAG, "SettingsActivity : onCreateDialog : Account Found : " + names[i]);
				  }
				  
				  builder.setItems(names, new DialogInterface.OnClickListener()
				  {
				    public void onClick(DialogInterface dialog, int which) 
				    {
				    	// Stuff to do when the account is selected by the user
				    	Account account = allAccounts[which]; // The only account available
				    	Log.d(LOGTAG, "SettingsActivity : onCreateDialog : Using the selected account, " + account.name);
				    	requestToken(account.name, serviceName);
				    	
						/* Start the Service which will get the token
						Context context = getApplicationContext();
						Intent newIntent = new Intent(context, AuthHandlerService.class);
						// Intent newIntent = new Intent(context, SaveMySMSActivity.class);
						newIntent.putExtra("com.shankarlabs.sms.accName", account.name);
						newIntent.putExtra("com.shankarlabs.sms.accType", account.type);
				        context.startService(newIntent);
				        */
				    }
				  });
				  
				  return builder.create();
		  }
		  return null;
	}
	
	/**
	 * Requests the token for the email ID passed
	 * @param	id	The email ID of the account we're requesting the token for
	 * @param	tokenType	The Service we're requesting a token for
	 * @returns	none
	 */
	@SuppressWarnings("deprecation") // getAuthToken is deprecated. Used for backward compatibility
	private void requestToken(String id, String tokenType)
	{
		AccountManagerFuture<Bundle> accMgrFuture;
		AccountManager accountManager = AccountManager.get(getApplicationContext());
		String ACCOUNT_TYPE = "com.google";
		String AUTH_TOKEN_TYPE = tokenType; // "oauth2:https://spreadsheets.google.com/feeds/"; // Spreadsheets
		Account account = new Account(id, ACCOUNT_TYPE);
		
		accMgrFuture = accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, false, new AccountManagerCallback<Bundle>()
				{
				@Override
				public void run(AccountManagerFuture<Bundle> future) 
				{
					// TODO Auto-generated method stub
					try
					{
						Bundle bundle = future.getResult();
                        Intent intent = (Intent)bundle.get(AccountManager.KEY_INTENT);
                        if(intent != null) {
                                // User input required
                                startActivity(intent);
                        } else {
                        	String authdName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
    						// String authdType = future.getResult().getString(AccountManager.KEY_ACCOUNT_TYPE);
    						String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
    						Log.d(LOGTAG, "AuthHandlerService : getAuthToken : Result after blocking call : Name : " + authdName + ", Token : " + token);
                        }
                        
												
						/* com.shankarlabs.sms.SMSActivity
						Log.d(LOGTAG, "AuthHandlerService : getAuthToken : Starting Activity SaveMySMSActivity");
						Context context = getApplicationContext();
						Intent smsActivityIntent = new Intent(context, SaveMySMSActivity.class);
						smsActivityIntent.putExtra("com.shankarlabs.sms.authdName", authdName);
						smsActivityIntent.putExtra("com.shankarlabs.sms.token", token);
						context.startService(smsActivityIntent);
						*/
					} 
					catch (OperationCanceledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} }, null);
	}
	
    // Here go all the ChangeListener CL methods
    OnPreferenceChangeListener scheduleIncCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		String scheduleIncDelay = String.valueOf(newValue);
    		// Log.d(LOGTAG, "SettingsActivity : onPrefChange : SMS backed up " + scheduleIncDelay + " minutes after they arrive");
    		scheduleIncomingPref.setSummary("SMS backed up " + scheduleIncDelay + " minutes after they arrive");
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener scheduleTimedCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		String scheduleTimes = String.valueOf(newValue);
    		// Log.d(LOGTAG, "SettingsActivity : onPrefChange : SMS backed up every " + scheduleTimes + " hours");
    		scheduleTimedPref.setSummary("SMS backed up every " + scheduleTimes + " hours");
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener smsGmailCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean smsGmailEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(smsGmailEnabled) {
    			// Try and get the token. Start the AuthHandlerService
    			serviceName = "oauth2:https://spreadsheets.google.com/feeds/"; //Spreadsheets
    			getAccountAndToken();
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener smsDocsCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean smsDocsEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(smsDocsEnabled) {
    			// Try and get the token. Start the AuthHandlerService
    			serviceName = "oauth2:https://spreadsheets.google.com/feeds/"; //Spreadsheets
    			getAccountAndToken();
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    
    /* The following Listeners are not used right now.
    OnPreferenceChangeListener mmsGmailCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean mmsGmailEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(mmsGmailEnabled) {
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener mmsDocsCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean mmsDocsEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(mmsDocsEnabled) {
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener mmsPicasaCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean mmsPicasaEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(mmsPicasaEnabled) {
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener logsGmailCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean logsGmailEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(logsGmailEnabled) {
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    
    OnPreferenceChangeListener logsDocsCL = new OnPreferenceChangeListener() {
    	@Override
    	public boolean onPreferenceChange(Preference preference, Object newValue) {
    		boolean logsDocsEnabled = Boolean.valueOf(String.valueOf(newValue));
    		if(logsDocsEnabled) {
    		} else {
    		}
			return true; // No one else needs to handle this
    	}
    };
    */
}
