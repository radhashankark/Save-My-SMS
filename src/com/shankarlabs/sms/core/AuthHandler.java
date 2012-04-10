package com.shankarlabs.sms.core;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AuthHandler
{
	public static final String LOGTAG = "SaveMySMS";
	private AccountManager accountManager;

	@SuppressWarnings("deprecation")
	public void getAllAccounts(Context context)
	{
		// Get Account manager
		accountManager = AccountManager.get(context);
		
		// List all accounts
		Account[] allAccounts = accountManager.getAccountsByType("com.google");
		for(int i = 0; i < allAccounts.length; i++)
		{
			Account acc = allAccounts[i];
			// Log.d(LOGTAG, "AuthHandler : getAllAccounts : Account " + i + " : " + acc.name + " : " + acc.type);
		}
		
		// Get details of Account4, krs824
		// Log.d(LOGTAG, "AuthHandler : getAllAccounts : User Data : " + accountManager.getUserData(allAccounts[4], "krs824@gmail.com"));
		// Log.d(LOGTAG, "AuthHandler : getAllAccounts : Password : " + accountManager.getPassword(allAccounts[4]));
		
		// Get the Auth Token for Account4
		AccountManagerFuture<Bundle> accMgrFuture;
		/* Scope URLs
		 * URL Shortener - https://www.googleapis.com/auth/urlshortener - Account Manager Fails
		 * Spreadsheets Read API - https://spreadsheets.google.com/feeds - wise - Manage your Spreadsheets
		 * Spreadsheets Write - https://docs.google.com/feeds - writely
		 * Tasks API - https://www.googleapis.com/auth/tasks - View and manage your tasks and task lists in Google Tasks
		 */
		String AUTH_TOKEN_TYPE = "wise";
		accMgrFuture = accountManager.getAuthToken(allAccounts[0], AUTH_TOKEN_TYPE, true, 
					new AccountManagerCallback<Bundle>()
					{
						@Override
						public void run(AccountManagerFuture<Bundle> future) 
						{
							// TODO Auto-generated method stub
							try
							{
								String accName = future.getResult().getString(AccountManager.KEY_ACCOUNT_NAME);
								String accType = future.getResult().getString(AccountManager.KEY_ACCOUNT_TYPE);
								String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
								Log.d(LOGTAG, "AuthHandler : getAuthTokenCallback : Account Name : " + accName); 
								Log.d(LOGTAG, "AuthHandler : getAuthTokenCallback : Account Type : " + accType); 
								Log.d(LOGTAG, "AuthHandler : getAuthTokenCallback : Token : " + token); 
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

}
