package com.shankarlabs.sms.core;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MsgHelper 
{
	private final String LOGTAG = "SaveMySMS";
	
	/**
	 * <b>int getSMSCount(Context context)</b>
	 * Get the message count on the device
	 * @param context	For ContentResolver
	 * @return Returns the number of messages on the device, including both SMS and MMS
	 */
	public int getSMSCount(Context context)
	{
		Uri selectUri = Uri.parse("content://sms");
		String[] selectionCols = null; // {"_id", "thread_id", "address", "person", "date", "date_sent", "subject", "body"};
		String selectionClause = null; // "thread_id = ?";
		String[] selectionArgs = null; // {"DISTINCT"};
		String dataOrder = "THREAD_ID desc";
	
		Cursor cursor = context.getContentResolver().query(selectUri, selectionCols, selectionClause, selectionArgs, dataOrder);
		return cursor.getCount();
	}
	
	/**
	 * <b>void getMessages(Context context)</b>
	 * Get all SMS stored on the device
	 * @param context	For ContentResolver
	 * @return none
	 */
	public void getMessages(Context context) 
	{
		Uri selectUri = Uri.parse("content://sms");
		String[] selectionCols = null; // {"_id", "thread_id", "address", "person", "date", "date_sent", "subject", "body"};
		String selectionClause = null; // "thread_id = ?";
		String[] selectionArgs = null; // {"DISTINCT"};
		String dataOrder = "THREAD_ID desc";
	
		Cursor cursor = context.getContentResolver().query(selectUri, selectionCols, selectionClause, selectionArgs, dataOrder);
		String[] columnNames = cursor.getColumnNames(); // Get all column names
		cursor.moveToNext(); // Move cursor to the first row
		
		Log.d(LOGTAG, "MsgHelper : getMessages : Retrieved " + cursor.getCount() + " rows");
		// Log.d(LOGTAG, "# We have " + columnNames.length + " columns : ");
		
		/*
		do
		{	
			Log.d(LOGTAG, " "); // New line :)
			Log.d(LOGTAG, "  Processing thread id " + cursor.getString(1) + " now : ");
			// Print the column names. Types : null 0, int 1, float 2, String 3, blob 4
			for(int i = 0; i < columnNames.length; i++)
			{
				switch(cursor.getType(i))
				{
					case 1:
						Log.d(LOGTAG, "    " + columnNames[i] + " : " + cursor.getInt(i));
						break;
					case 3:
						Log.d(LOGTAG, "    " + columnNames[i] + " : " + cursor.getString(i));
						break;
					case 0:
						Log.d(LOGTAG, "    " + columnNames[i] + " : null");
						break;
					default:
						Log.d(LOGTAG, "## " + columnNames[i] + " " + cursor.getType(i));
						break;
					
				}
			}
		}
		while(cursor.moveToNext());
		*/ 
		
		/* Print sameple data
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

		cursor.close();
	}
}
