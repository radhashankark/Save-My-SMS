package com.shankarlabs.sms;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.shankarlabs.sms.services.AuthHandlerService;
import com.shankarlabs.sms.ui.SaveMySMSActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

public class MainActivity extends SherlockFragmentActivity
{
	public static final String LOGTAG = "SaveMySMS";
	private final int ACCOUNTS_DIALOG = 12; // Random int
	private Account[] allAccounts;
	private int selectedUserIndex = 0;
	
	/* The whole purpose of this Activity is to pick a user Account,
	* put it in the Intent, and start the AuthHandlerService
	*/ 
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
    {
		// Get Account manager
		AccountManager accountManager = AccountManager.get(getApplicationContext());
				
		// List all accounts
		allAccounts = accountManager.getAccountsByType("com.google");
		if(allAccounts.length == 0) // No accounts configured. Start the Add Accounts Activity
		{
			Log.d(LOGTAG, "MainActivity : onCreate : No Account found. Add an account. Exiting now.");
			startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));
			finish(); // Since we have nothing else to do, end this activity
		}
		else if(allAccounts.length == 1) // There's just one account. Use it.
		{
			Account account = allAccounts[0]; // The only account available
			Log.d(LOGTAG, "MainActivity : onCreate : Using the only one account existing, " + account.name);
			
			// Start the Service which will get the token and start SaveMySMSActivity
			Context context = getApplicationContext();
			// Intent newIntent = new Intent(context, AuthHandlerService.class);
			Intent newIntent = new Intent(context, SaveMySMSActivity.class);
			newIntent.putExtra("com.shankarlabs.sms.accName", account.name);
			newIntent.putExtra("com.shankarlabs.sms.accType", account.type);
	        context.startService(newIntent);
	        
	        finish(); // Now that we started the Service, we finish this Activity
		}
		else
		{
			// Show the Account Selection Dialog
			showDialog(ACCOUNTS_DIALOG);
		}
		
		super.onCreate(savedInstanceState);
		
		Log.d(LOGTAG, "MainActivity : onCreate : Started Service. Exiting now.");
		finish();
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
					  Log.d(LOGTAG, "MainActivity : onCreateDialog : Account Found : " + names[i]);
				  }
				  
				  builder.setItems(names, new DialogInterface.OnClickListener()
				  {
				    public void onClick(DialogInterface dialog, int which) 
				    {
				    	// Stuff to do when the account is selected by the user
				    	Account account = allAccounts[which]; // The only account available
				    	Log.d(LOGTAG, "MainActivity : onCreate : Using the selected account, " + account.name);
				    	
						// Start the Service which will get the token and start SaveMySMSActivity
						Context context = getApplicationContext();
						// Intent newIntent = new Intent(context, AuthHandlerService.class);
						Intent newIntent = new Intent(context, SaveMySMSActivity.class);
						newIntent.putExtra("com.shankarlabs.sms.accName", account.name);
						newIntent.putExtra("com.shankarlabs.sms.accType", account.type);
				        context.startService(newIntent);
				        
				        finish(); // Now that we started the Service, we finish this Activity
				    }
				  });
				  
				  return builder.create();
		  }
		  return null;
	}
	
}
