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
//		TextView tv1=(TextView) findViewById(R.id.owner_speak);
//		TextView tv2=(TextView) findViewById(R.id.infector_speak);
//		TextView tv3=(TextView) findViewById(R.id.contractor_speak);
//		tv1.append(fetchInfector());		
		Meeting.relAdapter.close();
		CootiesActivity.mySQLiteAdapter.close();
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
	}

//    private String fetchInfector(){
//    	Cursor cLName = null;
//    	Cursor cFName = null;
//    	try{
//		cLName = Meeting.relAdapter.relrelsqLiteDatabase.
//    			rawQuery("SELECT partner_last FROM rel_table WHERE spread = '1'",null);
//		cFName = Meeting.relAdapter.relrelsqLiteDatabase.
//    			rawQuery("SELECT partner_first FROM rel_table WHERE spread = '1'",null);
//    	}catch(SQLiteException e) {}
//    	String last = cLName.getString(cLName.getColumnIndex(Meeting.relAdapter.PARTNER_LAST));
//    	String first = cFName.getString(cFName.getColumnIndex(Meeting.relAdapter.PARTNER_FIRST));
//    	String name =last + ""+first;
//    	toast(name);
//    	return name;   	
//    }
    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
