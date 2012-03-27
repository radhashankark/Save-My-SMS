package com.shankarlabs.sms.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.shankarlabs.sms.R;

public class MsgThreadFragment extends SherlockFragment 
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
        View fragmentView = inflater.inflate(R.layout.msgthread_fragment, container, false);
        
        return fragmentView;
    }
}
