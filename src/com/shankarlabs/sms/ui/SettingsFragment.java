package com.shankarlabs.sms.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.sms.R;

public class SettingsFragment extends SherlockFragment
{
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.settings_fragment, container, false);
        
        return fragmentView;
    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState)
	{
        super.onActivityCreated(savedInstanceState);
        
        // View settingsView = getActivity().findViewById(R.id.settings_fragment);
        
        View optionWhen = getActivity().findViewById(R.id.optionswhen);
        View optionWhat = getActivity().findViewById(R.id.optionswhat);
        View optionWhere = getActivity().findViewById(R.id.optionswhere);
        View optionRestore = getActivity().findViewById(R.id.optionsrestore);
        
        optionWhen.setOnClickListener(optWhenClickListener);
        optionWhat.setOnClickListener(optWhatClickListener);
        optionWhere.setOnClickListener(optWhereClickListener);
        optionRestore.setOnClickListener(optRestoreClickListener);
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
