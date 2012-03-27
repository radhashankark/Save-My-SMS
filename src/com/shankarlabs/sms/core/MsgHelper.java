package com.shankarlabs.sms.core;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MsgHelper {
	private final String LOGTAG = "SaveMySMS";
	
	// Method to get all SMS
	public void getMessages(Context context) {
	Uri selectUri = Uri.parse("content://sms");
	String selectionClause = "thread_id = ?";
	String[] selectionArgs = {""};
	String dataOrder = "DATE desc";

	// Cursor cursor = context.getContentResolver().query(selectUri, null, selectionClause, selectionArgs, dataOrder);
	Cursor cursor = context.getContentResolver().query(selectUri, null, null, null, "DATE desc");
	String[] columnNames = cursor.getColumnNames(); // Get all column names
	cursor.moveToNext(); // Move cursor to the first row
	
	Log.d(LOGTAG, "MsgHelper : getMessages : Retrieved " + cursor.getCount() + " rows");
	Log.d(LOGTAG, "# We have " + columnNames.length + " columns : ");
	
	// Print the column names. Types : null 0, int 1, float 2, String 3, blob 4
	for(int i = 0; i < columnNames.length; i++)
	{
		Log.d(LOGTAG, "## " + columnNames[i] + " is of type " + cursor.getType(i));
	}
	
	// Print sameple data
	for(int i = 0; i < columnNames.length; i++)
	{
		Log.d(LOGTAG, "#### " + columnNames[i] + " is of type " + cursor.getType(i));
	}
	
	/*
	while(cursor.moveToNext())
	{
		// Log.d(LOGTAG, "#### Processed Message " + (cursor.getPosition() + 1));
	}
	*/
	
	}
}
