package com.shankarlabs.sms.services;

import com.shankarlabs.sms.R;
import com.shankarlabs.sms.ui.SaveMySMSActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AuthHandlerService extends Service
{
	public static final String LOGTAG = "SaveMySMS";
	private AccountManager accountManager;
	private final IBinder ahBinder = new AuthHandlerBinder();
	
    public class AuthHandlerBinder extends Binder
    {
    	AuthHandlerService getService() {
    		Log.d(LOGTAG,"AuthHandlerBinder : getService : Returning AuthHandlerService");
            return AuthHandlerService.this;
        }
    }
    
	@Override
    public void onCreate() 
	{
		Log.d(LOGTAG,"AuthHandlerService : onCreate : Doing nothing in onCreate");
    }

    @SuppressWarnings("deprecation")
	@Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {   
    	String accountName = intent.getStringExtra("com.shankarlabs.sms.accName");
    	String accountType = intent.getStringExtra("com.shankarlabs.sms.accType");
    	Account account = new Account(accountName, accountType);
    	Log.d(LOGTAG,"AuthHandlerService : onStartCommand : Requesting Token for " + accountName);
    	
		// Get the Auth Token
		AccountManagerFuture<Bundle> accMgrFuture;
		AccountManagerCallback<Bundle> accMgrCallback = new AccountManagerCallback<Bundle>()
				{
				@Override
				public void run(AccountManagerFuture<Bundle> future) 
				{
					// TODO Auto-generated method stub
					/*
					try
					{
						String authdName = future.getResult().getString(AccountManager.KEY_ACCOUNT_NAME);
						// String authdType = future.getResult().getString(AccountManager.KEY_ACCOUNT_TYPE);
						String token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);
						Log.d(LOGTAG, "AuthHandlerService : getAuthToken : Result after blocking call : Name : " + authdName + ", Token : " + token);
						
						// com.shankarlabs.sms.SMSActivity
						Log.d(LOGTAG, "AuthHandlerService : getAuthToken : Starting Activity SaveMySMSActivity");
						Context context = getApplicationContext();
						Intent smsActivityIntent = new Intent(context, SaveMySMSActivity.class);
						smsActivityIntent.putExtra("com.shankarlabs.sms.authdName", authdName);
						smsActivityIntent.putExtra("com.shankarlabs.sms.token", token);
						context.startService(smsActivityIntent);
						
						stopSelf(); // We're done here. Shut down the Service.
					} 
					catch (OperationCanceledException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
				} };
				
		String AUTH_TOKEN_TYPE = "oauth2:https://spreadsheets.google.com/feeds/";
		accMgrFuture = accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, true, accMgrCallback, null);
    	
		// Now to extract the token
		try
		{
			String authdName = accMgrFuture.getResult().getString(AccountManager.KEY_ACCOUNT_NAME);
			// String authdType = future.getResult().getString(AccountManager.KEY_ACCOUNT_TYPE);
			String token = accMgrFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
			Log.d(LOGTAG, "AuthHandlerService : getAuthToken : Result after blocking call : Name : " + authdName + ", Token : " + token);
			
			// com.shankarlabs.sms.SMSActivity
			Log.d(LOGTAG, "AuthHandlerService : getAuthToken : Starting Activity SaveMySMSActivity");
			Context context = getApplicationContext();
			Intent smsActivityIntent = new Intent(context, SaveMySMSActivity.class);
			smsActivityIntent.putExtra("com.shankarlabs.sms.authdName", authdName);
			smsActivityIntent.putExtra("com.shankarlabs.sms.token", token);
			context.startService(smsActivityIntent);
			
			stopSelf(); // We're done here. Shut down the Service.
		} 
		catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_NOT_STICKY; // START_STICKY;
    }

    @Override
    public void onDestroy() 
    {    
    	Log.d(LOGTAG,"AuthHandlerService : onDestroy : Done with Service");
    }
    
    @Override
    public IBinder onBind(Intent intent)
    {
    	Log.d(LOGTAG,"AuthHandlerService : onBind : Returning AuthHandlerBinder");
        return ahBinder;
    }

}
