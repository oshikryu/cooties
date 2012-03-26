package com.umich.cooties;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class FinishMeeting extends Activity{
	
	static protected String infector;
	protected String[] contractor;
	
    public void onCreate(Bundle savedInstanceState){

		/*
		 *This code cancels the alarm so it won't bother you 
		 *once you complete the activity
		 */
		Intent intent = new Intent(this, RandomReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this,
		               0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(sender);
		
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish_meeting);
		TextView tv1 = (TextView) findViewById(R.id.owner_speak);
		TextView tv2 = (TextView) findViewById(R.id.infector_speak);
		TextView tv3 = (TextView) findViewById(R.id.contractor_speak);
		tv1.append(String.valueOf(fetchTotal()));
		tv2.append(fetchContractFrom());
		tv3.append(fetchInfector());
		Button back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		Button next=(Button) findViewById(R.id.meetdone);
		next.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(FinishMeeting.this,CootiesActivity.class);
				FinishMeeting.this.startActivity(intent);
			}
		});
		
		Button home=(Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(FinishMeeting.this,CootiesActivity.class);
				FinishMeeting.this.startActivity(intent);
			}
		});
		Meeting.relAdapter.close();
		CootiesActivity.mySQLiteAdapter.close();
	}

    private String fetchInfector(){
		Meeting.relAdapter.openRelToRead();
		Cursor relCursor = null;
    	try{
    		relCursor = Meeting.relAdapter.queueInfector();
    		relCursor.moveToFirst();
    	}
    	catch(SQLiteException e) { return "FOREVER ALONE";}
    	String last = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_LAST));
    	String first = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_FIRST));
    	String name = first + " " + last;
    	toast(name);
    	return name;   	
    }
    
    private int fetchTotal(){
		Meeting.relAdapter.openRelToRead();
		Cursor relCursor = null;
    	try{
    		relCursor = Meeting.relAdapter.countTotal();
    		relCursor.moveToFirst();
    	}
    	catch(SQLiteException e) { return 0;}
    	int total = relCursor.getCount();
    	return total;   	
    }
    
    private String fetchContractFrom(){
		Meeting.relAdapter.openRelToRead();
		Cursor relCursor = null;
    	try{
    		relCursor = Meeting.relAdapter.queueContractFrom();
    		relCursor.moveToFirst();
    	}
    	catch(SQLiteException e) { return "FOREVER ALONE";}
    	String last = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_LAST));
    	String first = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_FIRST));
    	String name = first + " " + last;
    	return name; 
    }
    
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
