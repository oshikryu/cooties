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



public class FinishMeetingTwo extends Activity{
	
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
		setContentView(R.layout.finish_meeting_two);
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
				Intent intent = new Intent(FinishMeetingTwo.this,CootiesActivity.class);
				FinishMeetingTwo.this.startActivity(intent);
			}
		});
		
		Button home=(Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				Intent intent = new Intent(FinishMeetingTwo.this,CootiesActivity.class);
				FinishMeetingTwo.this.startActivity(intent);
			}
		});
		CootiesActivity.relAdapter.close();
		CootiesActivity.mySQLiteAdapter.close();
	}

    private String fetchInfector(){
		CootiesActivity.relAdapter.openRelToRead();
		Cursor relCursor = null;
		String name = "Nobody";
    	try{
    		relCursor = CootiesActivity.relAdapter.queueInfectorHIV();
    		relCursor.moveToFirst();
    		if(relCursor.getCount() == 0){
    			return name;
    		}
        	String last = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_LAST));
        	String first = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_FIRST));
        	name = first + " " + last;
    	}
    	catch(SQLiteException e) { return "Nobody";}
    	return name;   	
    }
    
    private int fetchTotal(){
		CootiesActivity.relAdapter.openRelToRead();
		Cursor relCursor = null;
		int total = 0;
    	try{
    		relCursor = CootiesActivity.relAdapter.countTotalHIV();
    		if(relCursor.getCount() == 0){
    			return total;
    		}
    		relCursor.moveToFirst();
    		total = relCursor.getCount();
    	}
    	catch(SQLiteException e) { return 0;}
    	return total;   	
    }
    
    private String fetchContractFrom(){
		CootiesActivity.relAdapter.openRelToRead();
		Cursor relCursor = null;
		String name = "Nobody";
    	try{
    		relCursor = CootiesActivity.relAdapter.queueContractFromHIV();
    		relCursor.moveToFirst();
    		if(relCursor.getCount() == 0){
    			return name;
    		}
        	String last = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_LAST));
        	String first = relCursor.getString(relCursor.getColumnIndex(RelAdapter.PARTNER_FIRST));
        	name = first + " " + last;
    	}
    	catch(SQLiteException e) { return "Nobody";}

    	return name; 
    }
    
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
