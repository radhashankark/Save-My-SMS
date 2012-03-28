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

	public void getAllAccounts(Context context)
	{
		// Get Account manager
		accountManager = AccountManager.get(context);
		
		// List all accounts
		Account[] allAccounts = accountManager.getAccounts();
		for(int i = 0; i < allAccounts.length; i++)
		{
			Account acc = allAccounts[i];
			Log.d(LOGTAG, "AuthHandler : getAllAccounts : Account " + i + " : " + acc.name + " : " + acc.type);
		}
		
		// Get details of Account4, krs824
		// Log.d(LOGTAG, "AuthHandler : getAllAccounts : User Data : " + accountManager.getUserData(allAccounts[4], "krs824@gmail.com"));
		// Log.d(LOGTAG, "AuthHandler : getAllAccounts : Password : " + accountManager.getPassword(allAccounts[4]));
		
		// Get the Auth Token for Account4
		AccountManagerFuture<Bundle> accMgrFuture;
		String AUTH_TOKEN_TYPE = "oauth2:https://spreadsheets.google.com/feeds/";
		accMgrFuture = accountManager.getAuthToken(allAccounts[4], AUTH_TOKEN_TYPE, null, true, 
					new AccountManagerCallback<Bundle>()
					{
						@Override
						public void run(AccountManagerFuture<Bundle> future) 
						{
							// TODO Auto-generated method stub
							try
							{
								String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
								Log.d(LOGTAG, "AuthHandler : getAllAccounts : We have token : " + token); 
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
