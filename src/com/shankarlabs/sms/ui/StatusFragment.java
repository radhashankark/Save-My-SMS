package com.shankarlabs.sms.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.sms.R;
import com.shankarlabs.sms.core.MsgHelper;

public class StatusFragment extends SherlockFragment
{
	private static final String LOGTAG = "SaveMySMS";
	private MsgHelper msgHelper = new MsgHelper(); 
	private SharedPreferences prefs;
	private SharedPreferences.Editor prefsEditor;
	private Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        mContext = getSherlockActivity().getApplicationContext();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.status_fragment, container, false);
        Log.d(LOGTAG, "SettingsFragment : onCreateView : Created SettingsFragment View");
        return fragmentView;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);
        
        /*
        // View settingsView = getActivity().findViewById(R.id.settings_fragment);
        
        View optionWhen = getActivity().findViewById(R.id.optionswhen);
        View optionWhat = getActivity().findViewById(R.id.optionswhat);
        View optionWhere = getActivity().findViewById(R.id.optionswhere);
        View optionRestore = getActivity().findViewById(R.id.optionsrestore);
        
        optionWhen.setOnClickListener(optWhenClickListener);
        optionWhat.setOnClickListener(optWhatClickListener);
        optionWhere.setOnClickListener(optWhereClickListener);
        optionRestore.setOnClickListener(optRestoreClickListener);
        */
        
        Log.d(LOGTAG, "SettingsFragment : onActivityCreated : All Listeners bound");
        
        // Init the Preferences and set the message count
        initPreferences();
        TextView msgCount = (TextView) getView().findViewById(R.id.statuscount);
        msgCount.setText("0/" + msgHelper.getSMSCount(mContext));
        
	}
	
	
	/**
	 * <b>void</b> initPreferences() <br>
	 * Go through all stored Preferences and initialize their values if required or missing
	 */
	private void initPreferences() {
		prefs = PreferenceManager.getDefaultSharedPreferences(mContext); // The default Prefs
		prefsEditor = prefs.edit();
		
		String lastBackupTime = "Never";
		if(prefs.getLong("lastbackup", -1) != -1) {
			lastBackupTime = "" + DateFormat.format("MM/dd/yy h:mmaa", prefs.getLong("lastbackup", -1));
		}
		
		String backedupSoFar = "No";
		if(prefs.getInt("backupsofar", -1) != -1) {
			lastBackupTime = "" + prefs.getInt("backupsofar", -1);
		}
		
		// Start checking all the Prefs
		Log.d(LOGTAG, "SaveMySMSActivity : initPreferences : Last Backup : " + lastBackupTime);
		Log.d(LOGTAG, "SaveMySMSActivity : initPreferences : Until Now : " + backedupSoFar);
		// Log.d(LOGTAG, "SaveMySMSActivity : initPreferences : smsgmailbkpckbx : " + prefs.getBoolean("smsgmailbkpckbx", false));
		// Log.d(LOGTAG, "SaveMySMSActivity : initPreferences : smsdocsbkpckbx : " + prefs.getBoolean("smsdocsbkpckbx", false));
		
		// Set the Backup Status
        TextView backupStatus = (TextView) getView().findViewById(R.id.backupstatus);
        if(backupStatus == null) {
        	Log.d(LOGTAG, "SaveMySMSActivity : initPreferences : TextView BackupStatus is null for some weird reson");
        } else {
        	backupStatus.setText("Last Backed up : " + lastBackupTime + ", " + backedupSoFar + " messages");
        }
	}

	OnClickListener optWhenClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			Toast.makeText(getActivity(), "When do you need the backups ?", Toast.LENGTH_SHORT).show();
		}
	};
	
	OnClickListener optWhatClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			Toast.makeText(getActivity(), "What do you need backed up ?", Toast.LENGTH_SHORT).show();
		}
	};
	
	OnClickListener optWhereClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			Toast.makeText(getActivity(), "Where do you need the backups ?", Toast.LENGTH_SHORT).show();
		}
	};
	
	OnClickListener optRestoreClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			Toast.makeText(getActivity(), "Restore all the things", Toast.LENGTH_SHORT).show();
		}
	};
}
